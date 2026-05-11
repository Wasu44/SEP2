package org.example.client.networking;

import org.example.shared.dto.AccountDTO;
import org.example.shared.dto.ListingDTO;
import org.example.shared.dto.ReservationDTO;
import org.example.shared.protocol.Request;
import org.example.shared.protocol.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ServerProxy {

  private final String host;
  private final int port;

  private Socket socket;
  private ObjectOutputStream out;
  private ObjectInputStream in;

  public ServerProxy(String host, int port) {
    this.host = host;
    this.port = port;
  }

  private synchronized void ensureConnected() throws IOException {
    if (socket != null && socket.isConnected() && !socket.isClosed()) return;
    socket = new Socket(host, port);
    out = new ObjectOutputStream(socket.getOutputStream());
    in = new ObjectInputStream(socket.getInputStream());
  }

  private synchronized Response send(Request request) {
    try {
      ensureConnected();
      out.writeObject(request);
      out.reset();
      Object reply = in.readObject();
      if (reply instanceof Response) return (Response) reply;
      return Response.fail("Unexpected reply type");
    } catch (IOException | ClassNotFoundException e) {
      return Response.fail("Network error: " + e.getMessage());
    }
  }

  public Response register(String email, String name, String password, String role) {
    Request req = new Request(Request.REGISTER)
        .set("email", email)
        .set("name", name)
        .set("password", password)
        .set("role", role);
    return send(req);
  }

  public Response login(String email, String password) {
    Request req = new Request(Request.LOGIN)
        .set("email", email)
        .set("password", password);
    return send(req);
  }

  public Response updateAccount(int accountId, String name, String email, String password) {
    Request req = new Request(Request.UPDATE_ACCOUNT)
        .set("accountId", accountId)
        .set("name", name)
        .set("email", email)
        .set("password", password);
    return send(req);
  }

  public Response createListing(int accountId, String title, String description,
                                int quantity, String pickupWindow) {
    Request req = new Request(Request.CREATE_LISTING)
        .set("accountId", accountId)
        .set("title", title)
        .set("description", description)
        .set("quantity", quantity)
        .set("pickupWindow", pickupWindow);
    return send(req);
  }

  @SuppressWarnings("unchecked")
  public List<ListingDTO> myListings(int accountId) {
    Request req = new Request(Request.MY_LISTINGS).set("accountId", accountId);
    Response resp = send(req);
    if (resp.isSuccess() && resp.getData() instanceof List) {
      return (List<ListingDTO>) resp.getData();
    }
    return java.util.Collections.emptyList();
  }

  public Response updateListingStatus(int accountId, int listingId, String newStatus) {
    Request req = new Request(Request.UPDATE_LISTING_STATUS)
        .set("accountId", accountId)
        .set("listingId", listingId)
        .set("status", newStatus);
    return send(req);
  }

  public AccountDTO asAccount(Response r) {
    if (r.isSuccess() && r.getData() instanceof AccountDTO) return (AccountDTO) r.getData();
    return null;
  }

  // ---------- Sprint 3 ----------

  @SuppressWarnings("unchecked")
  public List<AccountDTO> listPendingProviders(int adminId) {
    Response resp = send(new Request(Request.LIST_PENDING_PROVIDERS).set("accountId", adminId));
    if (resp.isSuccess() && resp.getData() instanceof List) return (List<AccountDTO>) resp.getData();
    return java.util.Collections.emptyList();
  }

  public Response verifyProvider(int adminId, int providerId) {
    return send(new Request(Request.VERIFY_PROVIDER)
        .set("accountId", adminId)
        .set("providerId", providerId));
  }

  @SuppressWarnings("unchecked")
  public List<ListingDTO> browseListings(int accountId) {
    Response resp = send(new Request(Request.BROWSE_LISTINGS).set("accountId", accountId));
    if (resp.isSuccess() && resp.getData() instanceof List) return (List<ListingDTO>) resp.getData();
    return java.util.Collections.emptyList();
  }

  public Response createReservation(int customerId, int listingId) {
    return send(new Request(Request.CREATE_RESERVATION)
        .set("accountId", customerId)
        .set("listingId", listingId));
  }

  @SuppressWarnings("unchecked")
  public List<ReservationDTO> myReservations(int customerId) {
    Response resp = send(new Request(Request.MY_RESERVATIONS).set("accountId", customerId));
    if (resp.isSuccess() && resp.getData() instanceof List) return (List<ReservationDTO>) resp.getData();
    return java.util.Collections.emptyList();
  }

  @SuppressWarnings("unchecked")
  public List<ReservationDTO> providerReservations(int providerId) {
    Response resp = send(new Request(Request.LIST_PROVIDER_RESERVATIONS).set("accountId", providerId));
    if (resp.isSuccess() && resp.getData() instanceof List) return (List<ReservationDTO>) resp.getData();
    return java.util.Collections.emptyList();
  }

  public Response updateReservationStatus(int accountId, int reservationId, String newStatus) {
    return send(new Request(Request.UPDATE_RESERVATION_STATUS)
        .set("accountId", accountId)
        .set("reservationId", reservationId)
        .set("status", newStatus));
  }
}
