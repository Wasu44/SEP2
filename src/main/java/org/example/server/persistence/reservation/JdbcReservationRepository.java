package org.example.server.persistence.reservation;

import org.example.server.database.Database;
import org.example.server.model.reservation.Reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcReservationRepository implements ReservationRepository {

  @Override
  public Reservation add(int listingId, int customerId, String status) {
    String sql = "INSERT INTO reservations (listing_id, customer_id, status) VALUES (?, ?, ?)";
    try (Connection c = Database.get();
         PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      ps.setInt(1, listingId);
      ps.setInt(2, customerId);
      ps.setString(3, status);
      ps.executeUpdate();
      try (ResultSet keys = ps.getGeneratedKeys()) {
        keys.next();
        return new Reservation(keys.getInt(1), listingId, customerId, status);
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to insert reservation", e);
    }
  }

  @Override
  public Optional<Reservation> findById(int id) {
    String sql = "SELECT id, listing_id, customer_id, status FROM reservations WHERE id = ?";
    try (Connection c = Database.get();
         PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) return Optional.of(read(rs));
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to find reservation by id", e);
    }
  }

  @Override
  public List<Reservation> findByCustomer(int customerId) {
    String sql = "SELECT id, listing_id, customer_id, status FROM reservations " +
                 "WHERE customer_id = ? ORDER BY id";
    List<Reservation> out = new ArrayList<>();
    try (Connection c = Database.get();
         PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setInt(1, customerId);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) out.add(read(rs));
      }
      return out;
    } catch (SQLException e) {
      throw new RuntimeException("Failed to find reservations by customer", e);
    }
  }

  @Override
  public List<Reservation> findByListingIds(List<Integer> listingIds) {
    if (listingIds.isEmpty()) return new ArrayList<>();
    StringBuilder placeholders = new StringBuilder();
    for (int i = 0; i < listingIds.size(); i++) {
      if (i > 0) placeholders.append(',');
      placeholders.append('?');
    }
    String sql = "SELECT id, listing_id, customer_id, status FROM reservations " +
                 "WHERE listing_id IN (" + placeholders + ") ORDER BY id";
    List<Reservation> out = new ArrayList<>();
    try (Connection c = Database.get();
         PreparedStatement ps = c.prepareStatement(sql)) {
      for (int i = 0; i < listingIds.size(); i++) ps.setInt(i + 1, listingIds.get(i));
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) out.add(read(rs));
      }
      return out;
    } catch (SQLException e) {
      throw new RuntimeException("Failed to find reservations by listing ids", e);
    }
  }

  @Override
  public void updateStatus(int id, String newStatus) {
    String sql = "UPDATE reservations SET status = ? WHERE id = ?";
    try (Connection c = Database.get();
         PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, newStatus);
      ps.setInt(2, id);
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Failed to update reservation status", e);
    }
  }

  private static Reservation read(ResultSet rs) throws SQLException {
    return new Reservation(
        rs.getInt("id"),
        rs.getInt("listing_id"),
        rs.getInt("customer_id"),
        rs.getString("status"));
  }
}
