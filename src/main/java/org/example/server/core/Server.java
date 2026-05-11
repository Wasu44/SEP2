package org.example.server.core;

import org.example.server.database.Database;
import org.example.server.persistence.account.AccountRepository;
import org.example.server.persistence.account.JdbcAccountRepository;
import org.example.server.persistence.listing.JdbcListingRepository;
import org.example.server.persistence.listing.ListingRepository;
import org.example.server.persistence.reservation.JdbcReservationRepository;
import org.example.server.persistence.reservation.ReservationRepository;
import org.example.shared.dto.AccountDTO;
import org.example.shared.util.PasswordHasher;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

  public static final int PORT = 2910;

  public static void main(String[] args) {
    Database.bootstrapSchema();

    AccountRepository accountRepository = new JdbcAccountRepository();
    ListingRepository listingRepository = new JdbcListingRepository();
    ReservationRepository reservationRepository = new JdbcReservationRepository();

    seed(accountRepository);

    System.out.println("Starting server on port " + PORT + "...");
    try (ServerSocket welcomeSocket = new ServerSocket(PORT)) {
      while (true) {
        Socket socket = welcomeSocket.accept();
        System.out.println("Client connected: " + socket.getRemoteSocketAddress());
        ServerConnection connection = new ServerConnection(
            socket, accountRepository, listingRepository, reservationRepository);
        new Thread(connection).start();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void seed(AccountRepository repo) {
    if (!repo.existsByEmail("admin@demo.com")) {
      repo.add("admin@demo.com", "Demo Admin",
          PasswordHasher.hash("password"),
          AccountDTO.ROLE_ADMIN, AccountDTO.STATUS_ACTIVE);
      System.out.println("Seeded admin@demo.com (password: password)");
    }
    if (!repo.existsByEmail("provider@demo.com")) {
      repo.add("provider@demo.com", "Demo Provider",
          PasswordHasher.hash("password"),
          AccountDTO.ROLE_PROVIDER, AccountDTO.STATUS_ACTIVE);
      System.out.println("Seeded provider@demo.com (password: password)");
    }
    if (!repo.existsByEmail("customer@demo.com")) {
      repo.add("customer@demo.com", "Demo Customer",
          PasswordHasher.hash("password"),
          AccountDTO.ROLE_CUSTOMER, AccountDTO.STATUS_ACTIVE);
      System.out.println("Seeded customer@demo.com (password: password)");
    }
  }
}
