package org.example.server.persistence.listing;

import org.example.server.database.Database;
import org.example.server.model.listing.Listing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcListingRepository implements ListingRepository {

  @Override
  public Listing add(int providerId, String title, String description,
                     int quantity, String pickupWindow, String status) {
    String sql = "INSERT INTO listings (provider_id, title, description, quantity, pickup_window, status) " +
                 "VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection c = Database.get();
         PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      ps.setInt(1, providerId);
      ps.setString(2, title);
      ps.setString(3, description);
      ps.setInt(4, quantity);
      ps.setString(5, pickupWindow);
      ps.setString(6, status);
      ps.executeUpdate();
      try (ResultSet keys = ps.getGeneratedKeys()) {
        keys.next();
        int id = keys.getInt(1);
        return new Listing(id, providerId, title, description, quantity, pickupWindow, status);
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to insert listing", e);
    }
  }

  @Override
  public Optional<Listing> findById(int id) {
    String sql = "SELECT id, provider_id, title, description, quantity, pickup_window, status " +
                 "FROM listings WHERE id = ?";
    try (Connection c = Database.get();
         PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) return Optional.of(read(rs));
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to find listing by id", e);
    }
  }

  @Override
  public List<Listing> findByProvider(int providerId) {
    String sql = "SELECT id, provider_id, title, description, quantity, pickup_window, status " +
                 "FROM listings WHERE provider_id = ? ORDER BY id";
    List<Listing> out = new ArrayList<>();
    try (Connection c = Database.get();
         PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setInt(1, providerId);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) out.add(read(rs));
        return out;
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to find listings by provider", e);
    }
  }

  @Override
  public List<Listing> findAllAvailable() {
    String sql = "SELECT id, provider_id, title, description, quantity, pickup_window, status " +
                 "FROM listings WHERE status = 'AVAILABLE' ORDER BY id";
    List<Listing> out = new ArrayList<>();
    try (Connection c = Database.get();
         PreparedStatement ps = c.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
      while (rs.next()) out.add(read(rs));
      return out;
    } catch (SQLException e) {
      throw new RuntimeException("Failed to find available listings", e);
    }
  }

  @Override
  public void updateStatus(int id, String newStatus) {
    String sql = "UPDATE listings SET status = ? WHERE id = ?";
    try (Connection c = Database.get();
         PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, newStatus);
      ps.setInt(2, id);
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Failed to update listing status", e);
    }
  }

  private static Listing read(ResultSet rs) throws SQLException {
    return new Listing(
        rs.getInt("id"),
        rs.getInt("provider_id"),
        rs.getString("title"),
        rs.getString("description"),
        rs.getInt("quantity"),
        rs.getString("pickup_window"),
        rs.getString("status"));
  }
}
