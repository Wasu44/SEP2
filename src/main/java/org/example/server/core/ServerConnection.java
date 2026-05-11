package org.example.server.core;

import org.example.server.model.account.Account;
import org.example.server.model.listing.Listing;
import org.example.server.model.reservation.Reservation;
import org.example.server.persistence.account.AccountRepository;
import org.example.server.persistence.listing.ListingRepository;
import org.example.server.persistence.reservation.ReservationRepository;
import org.example.shared.dto.AccountDTO;
import org.example.shared.dto.ListingDTO;
import org.example.shared.dto.ReservationDTO;
import org.example.shared.protocol.Request;
import org.example.shared.protocol.Response;
import org.example.shared.util.PasswordHasher;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServerConnection implements Runnable {

  private final Socket socket;
  private final ObjectInputStream in;
  private final ObjectOutputStream out;
  private final AccountRepository accountRepository;
  private final ListingRepository listingRepository;
  private final ReservationRepository reservationRepository;

  public ServerConnection(Socket socket,
                          AccountRepository accountRepository,
                          ListingRepository listingRepository,
                          ReservationRepository reservationRepository) throws IOException {
    this.socket = socket;
    this.out = new ObjectOutputStream(socket.getOutputStream());
    this.in = new ObjectInputStream(socket.getInputStream());
    this.accountRepository = accountRepository;
    this.listingRepository = listingRepository;
    this.reservationRepository = reservationRepository;
  }

  @Override
  public void run() {
    try {
      while (true) {
        Object incoming = in.readObject();
        if (!(incoming instanceof Request)) {
          out.writeObject(Response.fail("Bad request"));
          continue;
        }
        Request req = (Request) incoming;
        Response resp = handle(req);
        out.writeObject(resp);
        out.reset();
      }
    } catch (IOException | ClassNotFoundException e) {
      System.out.println("Client disconnected: " + e.getMessage());
    } finally {
      try { socket.close(); } catch (IOException ignored) {}
    }
  }

  private Response handle(Request req) {
    try {
      switch (req.getType()) {
        case Request.REGISTER:        return handleRegister(req);
        case Request.LOGIN:           return handleLogin(req);
        case Request.UPDATE_ACCOUNT:  return handleUpdateAccount(req);
        case Request.CREATE_LISTING:  return handleCreateListing(req);
        case Request.MY_LISTINGS:     return handleMyListings(req);
        case Request.UPDATE_LISTING_STATUS: return handleUpdateListingStatus(req);
        case Request.LIST_PENDING_PROVIDERS: return handleListPendingProviders(req);
        case Request.VERIFY_PROVIDER:        return handleVerifyProvider(req);
        case Request.BROWSE_LISTINGS:        return handleBrowseListings(req);
        case Request.CREATE_RESERVATION:     return handleCreateReservation(req);
        case Request.MY_RESERVATIONS:        return handleMyReservations(req);
        case Request.LIST_PROVIDER_RESERVATIONS: return handleProviderReservations(req);
        case Request.UPDATE_RESERVATION_STATUS:  return handleUpdateReservationStatus(req);
        default: return Response.fail("Unknown request type: " + req.getType());
      }
    } catch (Exception e) {
      return Response.fail("Server error: " + e.getMessage());
    }
  }

  private Response handleRegister(Request req) {
    String email = req.getString("email");
    String name = req.getString("name");
    String password = req.getString("password");
    String role = req.getString("role");

    if (isBlank(email) || isBlank(name) || isBlank(password) || isBlank(role)) {
      return Response.fail("All fields are required");
    }
    if (!email.contains("@") || !email.contains(".")) {
      return Response.fail("Invalid email format");
    }
    if (password.length() < 6) {
      return Response.fail("Password must be at least 6 characters");
    }
    if (!role.equals(AccountDTO.ROLE_CUSTOMER) && !role.equals(AccountDTO.ROLE_PROVIDER)) {
      return Response.fail("Invalid role");
    }
    if (accountRepository.existsByEmail(email)) {
      return Response.fail("Email already in use");
    }

    String status = role.equals(AccountDTO.ROLE_PROVIDER)
        ? AccountDTO.STATUS_PENDING : AccountDTO.STATUS_ACTIVE;

    Account account = accountRepository.add(email, name, PasswordHasher.hash(password), role, status);
    return Response.ok(account.toDto());
  }

  private Response handleLogin(Request req) {
    String email = req.getString("email");
    String password = req.getString("password");

    if (isBlank(email) || isBlank(password)) {
      return Response.fail("Email and password are required");
    }
    Optional<Account> found = accountRepository.findByEmail(email);
    if (found.isEmpty()) {
      return Response.fail("Invalid email or password");
    }
    Account account = found.get();
    if (!account.getPasswordHash().equals(PasswordHasher.hash(password))) {
      return Response.fail("Invalid email or password");
    }
    if (!account.getStatus().equals(AccountDTO.STATUS_ACTIVE)) {
      return Response.fail("Account is not active (status: " + account.getStatus() + ")");
    }
    return Response.ok(account.toDto());
  }

  private Response handleUpdateAccount(Request req) {
    Integer accountId = (Integer) req.get("accountId");
    if (accountId == null) return Response.fail("Not authenticated");

    Optional<Account> found = accountRepository.findById(accountId);
    if (found.isEmpty()) return Response.fail("Account not found");
    Account account = found.get();

    String newName = req.getString("name");
    String newEmail = req.getString("email");
    String newPassword = req.getString("password");

    String finalName = isBlank(newName) ? account.getName() : newName;
    String finalEmail = account.getEmail();
    if (!isBlank(newEmail)) {
      if (!newEmail.contains("@") || !newEmail.contains(".")) {
        return Response.fail("Invalid email format");
      }
      Optional<Account> existing = accountRepository.findByEmail(newEmail);
      if (existing.isPresent() && existing.get().getId() != account.getId()) {
        return Response.fail("Email already in use");
      }
      finalEmail = newEmail;
    }
    String finalHash = account.getPasswordHash();
    if (!isBlank(newPassword)) {
      if (newPassword.length() < 6) {
        return Response.fail("Password must be at least 6 characters");
      }
      finalHash = PasswordHasher.hash(newPassword);
    }

    accountRepository.update(account.getId(), finalName, finalEmail, finalHash);
    Account updated = accountRepository.findById(account.getId()).orElse(account);
    return Response.ok(updated.toDto());
  }

  private Response handleCreateListing(Request req) {
    Integer accountId = (Integer) req.get("accountId");
    if (accountId == null) return Response.fail("Not authenticated");

    Optional<Account> found = accountRepository.findById(accountId);
    if (found.isEmpty()) return Response.fail("Account not found");
    Account account = found.get();
    if (!account.getRole().equals(AccountDTO.ROLE_PROVIDER)) {
      return Response.fail("Only providers can create listings");
    }
    if (!account.getStatus().equals(AccountDTO.STATUS_ACTIVE)) {
      return Response.fail("Provider account is not verified");
    }

    String title = req.getString("title");
    String description = req.getString("description");
    Integer quantity = (Integer) req.get("quantity");
    String pickupWindow = req.getString("pickupWindow");

    if (isBlank(title) || isBlank(pickupWindow) || quantity == null) {
      return Response.fail("Title, quantity and pickup window are required");
    }
    if (quantity <= 0) {
      return Response.fail("Quantity must be greater than zero");
    }

    Listing listing = listingRepository.add(account.getId(),
        title, description == null ? "" : description,
        quantity, pickupWindow, ListingDTO.STATUS_AVAILABLE);
    return Response.ok(listing.toDto());
  }

  private Response handleMyListings(Request req) {
    Integer accountId = (Integer) req.get("accountId");
    if (accountId == null) return Response.fail("Not authenticated");

    List<Listing> mine = listingRepository.findByProvider(accountId);
    ArrayList<ListingDTO> dtos = new ArrayList<>();
    for (Listing l : mine) dtos.add(l.toDto());
    return Response.ok(dtos);
  }

  private Response handleUpdateListingStatus(Request req) {
    Integer accountId = (Integer) req.get("accountId");
    if (accountId == null) return Response.fail("Not authenticated");

    Integer listingId = (Integer) req.get("listingId");
    String newStatus = req.getString("status");
    if (listingId == null || isBlank(newStatus)) {
      return Response.fail("listingId and status are required");
    }

    Optional<Listing> found = listingRepository.findById(listingId);
    if (found.isEmpty()) return Response.fail("Listing not found");
    Listing listing = found.get();
    if (listing.getProviderId() != accountId) {
      return Response.fail("You do not own this listing");
    }
    if (!listing.canTransitionTo(newStatus)) {
      return Response.fail("Cannot change status from "
          + listing.getStatus() + " to " + newStatus);
    }
    listingRepository.updateStatus(listing.getId(), newStatus);
    Listing updated = listingRepository.findById(listing.getId()).orElse(listing);
    return Response.ok(updated.toDto());
  }

  // ---------- Sprint 3: Admin verification ----------

  private Response handleListPendingProviders(Request req) {
    Account admin = requireRole(req, AccountDTO.ROLE_ADMIN);
    if (admin == null) return Response.fail("Admin access required");
    List<Account> pending = accountRepository.findByRoleAndStatus(
        AccountDTO.ROLE_PROVIDER, AccountDTO.STATUS_PENDING);
    ArrayList<AccountDTO> dtos = new ArrayList<>();
    for (Account a : pending) dtos.add(a.toDto());
    return Response.ok(dtos);
  }

  private Response handleVerifyProvider(Request req) {
    Account admin = requireRole(req, AccountDTO.ROLE_ADMIN);
    if (admin == null) return Response.fail("Admin access required");
    Integer providerId = (Integer) req.get("providerId");
    if (providerId == null) return Response.fail("providerId required");
    Optional<Account> found = accountRepository.findById(providerId);
    if (found.isEmpty()) return Response.fail("Provider not found");
    Account p = found.get();
    if (!p.getRole().equals(AccountDTO.ROLE_PROVIDER)) {
      return Response.fail("Account is not a provider");
    }
    accountRepository.updateStatus(providerId, AccountDTO.STATUS_ACTIVE);
    return Response.ok(accountRepository.findById(providerId).get().toDto());
  }

  // ---------- Sprint 3: Browse listings & reservations ----------

  private Response handleBrowseListings(Request req) {
    if (req.get("accountId") == null) return Response.fail("Not authenticated");
    List<ListingDTO> available = new ArrayList<>();
    for (Listing l : listingRepository.findAllAvailable()) available.add(l.toDto());
    return Response.ok(new ArrayList<>(available));
  }

  private Response handleCreateReservation(Request req) {
    Account customer = requireRole(req, AccountDTO.ROLE_CUSTOMER);
    if (customer == null) return Response.fail("Customer access required");
    Integer listingId = (Integer) req.get("listingId");
    if (listingId == null) return Response.fail("listingId required");
    Optional<Listing> found = listingRepository.findById(listingId);
    if (found.isEmpty()) return Response.fail("Listing not found");
    Listing l = found.get();
    if (!l.getStatus().equals(ListingDTO.STATUS_AVAILABLE)) {
      return Response.fail("Listing is not available");
    }
    Reservation r = reservationRepository.add(l.getId(), customer.getId(), ReservationDTO.STATUS_PENDING);
    listingRepository.updateStatus(l.getId(), ListingDTO.STATUS_RESERVED);
    return Response.ok(toDto(r, l, customer));
  }

  private Response handleMyReservations(Request req) {
    Account customer = requireRole(req, AccountDTO.ROLE_CUSTOMER);
    if (customer == null) return Response.fail("Customer access required");
    List<Reservation> mine = reservationRepository.findByCustomer(customer.getId());
    ArrayList<ReservationDTO> dtos = new ArrayList<>();
    for (Reservation r : mine) {
      Listing l = listingRepository.findById(r.getListingId()).orElse(null);
      dtos.add(toDto(r, l, customer));
    }
    return Response.ok(dtos);
  }

  private Response handleProviderReservations(Request req) {
    Account provider = requireRole(req, AccountDTO.ROLE_PROVIDER);
    if (provider == null) return Response.fail("Provider access required");
    List<Listing> myListings = listingRepository.findByProvider(provider.getId());
    ArrayList<Integer> ids = new ArrayList<>();
    for (Listing l : myListings) ids.add(l.getId());
    List<Reservation> reservations = reservationRepository.findByListingIds(ids);
    ArrayList<ReservationDTO> dtos = new ArrayList<>();
    for (Reservation r : reservations) {
      Listing l = listingRepository.findById(r.getListingId()).orElse(null);
      Account c = accountRepository.findById(r.getCustomerId()).orElse(null);
      dtos.add(toDto(r, l, c));
    }
    return Response.ok(dtos);
  }

  private Response handleUpdateReservationStatus(Request req) {
    Integer accountId = (Integer) req.get("accountId");
    if (accountId == null) return Response.fail("Not authenticated");
    Optional<Account> who = accountRepository.findById(accountId);
    if (who.isEmpty()) return Response.fail("Account not found");
    Account caller = who.get();

    Integer reservationId = (Integer) req.get("reservationId");
    String newStatus = req.getString("status");
    if (reservationId == null || isBlank(newStatus)) {
      return Response.fail("reservationId and status are required");
    }
    Optional<Reservation> rOpt = reservationRepository.findById(reservationId);
    if (rOpt.isEmpty()) return Response.fail("Reservation not found");
    Reservation r = rOpt.get();
    Optional<Listing> lOpt = listingRepository.findById(r.getListingId());
    if (lOpt.isEmpty()) return Response.fail("Listing missing");
    Listing l = lOpt.get();

    if (caller.getRole().equals(AccountDTO.ROLE_PROVIDER)) {
      if (l.getProviderId() != caller.getId()) return Response.fail("Not your listing");
    } else if (caller.getRole().equals(AccountDTO.ROLE_CUSTOMER)) {
      if (r.getCustomerId() != caller.getId()) return Response.fail("Not your reservation");
    } else {
      return Response.fail("Role cannot change reservation status");
    }

    if (!r.canTransitionTo(newStatus, caller.getRole())) {
      return Response.fail("Cannot change reservation from " + r.getStatus() + " to " + newStatus);
    }

    reservationRepository.updateStatus(r.getId(), newStatus);
    // Side-effects on the listing:
    if (newStatus.equals(ReservationDTO.STATUS_REJECTED)) {
      listingRepository.updateStatus(l.getId(), ListingDTO.STATUS_AVAILABLE);
    } else if (newStatus.equals(ReservationDTO.STATUS_COMPLETED)) {
      listingRepository.updateStatus(l.getId(), ListingDTO.STATUS_COMPLETED);
    }

    Reservation updated = reservationRepository.findById(r.getId()).orElse(r);
    Account customer = accountRepository.findById(r.getCustomerId()).orElse(null);
    return Response.ok(toDto(updated, l, customer));
  }

  // ---------- helpers ----------

  private Account requireRole(Request req, String role) {
    Integer accountId = (Integer) req.get("accountId");
    if (accountId == null) return null;
    Optional<Account> found = accountRepository.findById(accountId);
    if (found.isEmpty()) return null;
    Account a = found.get();
    if (!a.getRole().equals(role)) return null;
    if (!a.getStatus().equals(AccountDTO.STATUS_ACTIVE)) return null;
    return a;
  }

  private ReservationDTO toDto(Reservation r, Listing l, Account customer) {
    return new ReservationDTO(
        r.getId(),
        r.getListingId(),
        r.getCustomerId(),
        l != null ? l.getTitle() : "(unknown)",
        customer != null ? customer.getName() : "(unknown)",
        r.getStatus());
  }

  private static boolean isBlank(String s) {
    return s == null || s.trim().isEmpty();
  }
}
