package org.example.server.persistence.account;

import org.example.server.database.Database;
import org.example.server.model.account.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcAccountRepository implements AccountRepository {

  @Override
  public Account add(String email, String name, String passwordHash, String role, String status) {
    String sql = "INSERT INTO accounts (email, name, password_hash, role, status) VALUES (?, ?, ?, ?, ?)";
    try (Connection c = Database.get();
         PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, email);
      ps.setString(2, name);
      ps.setString(3, passwordHash);
      ps.setString(4, role);
      ps.setString(5, status);
      ps.executeUpdate();
      try (ResultSet keys = ps.getGeneratedKeys()) {
        keys.next();
        int id = keys.getInt(1);
        return new Account(id, email, name, passwordHash, role, status);
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to insert account", e);
    }
  }

  @Override
  public Optional<Account> findByEmail(String email) {
    String sql = "SELECT id, email, name, password_hash, role, status FROM accounts WHERE LOWER(email) = LOWER(?)";
    try (Connection c = Database.get();
         PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, email);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) return Optional.of(read(rs));
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to find account by email", e);
    }
  }

  @Override
  public Optional<Account> findById(int id) {
    String sql = "SELECT id, email, name, password_hash, role, status FROM accounts WHERE id = ?";
    try (Connection c = Database.get();
         PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) return Optional.of(read(rs));
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to find account by id", e);
    }
  }

  @Override
  public boolean existsByEmail(String email) {
    String sql = "SELECT 1 FROM accounts WHERE LOWER(email) = LOWER(?)";
    try (Connection c = Database.get();
         PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, email);
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next();
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to check email", e);
    }
  }

  @Override
  public void update(int id, String name, String email, String passwordHash) {
    String sql = "UPDATE accounts SET name = ?, email = ?, password_hash = ? WHERE id = ?";
    try (Connection c = Database.get();
         PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, name);
      ps.setString(2, email);
      ps.setString(3, passwordHash);
      ps.setInt(4, id);
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Failed to update account", e);
    }
  }

  @Override
  public void updateStatus(int id, String newStatus) {
    String sql = "UPDATE accounts SET status = ? WHERE id = ?";
    try (Connection c = Database.get();
         PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, newStatus);
      ps.setInt(2, id);
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Failed to update account status", e);
    }
  }

  @Override
  public List<Account> findByRoleAndStatus(String role, String status) {
    String sql = "SELECT id, email, name, password_hash, role, status FROM accounts " +
                 "WHERE role = ? AND status = ? ORDER BY id";
    List<Account> out = new ArrayList<>();
    try (Connection c = Database.get();
         PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, role);
      ps.setString(2, status);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) out.add(read(rs));
      }
      return out;
    } catch (SQLException e) {
      throw new RuntimeException("Failed to find accounts by role and status", e);
    }
  }

  @Override
  public List<Account> all() {
    String sql = "SELECT id, email, name, password_hash, role, status FROM accounts ORDER BY id";
    List<Account> out = new ArrayList<>();
    try (Connection c = Database.get();
         PreparedStatement ps = c.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
      while (rs.next()) out.add(read(rs));
      return out;
    } catch (SQLException e) {
      throw new RuntimeException("Failed to list accounts", e);
    }
  }

  private static Account read(ResultSet rs) throws SQLException {
    return new Account(
        rs.getInt("id"),
        rs.getString("email"),
        rs.getString("name"),
        rs.getString("password_hash"),
        rs.getString("role"),
        rs.getString("status"));
  }
}
