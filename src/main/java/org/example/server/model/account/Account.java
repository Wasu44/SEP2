package org.example.server.model.account;

import org.example.shared.dto.AccountDTO;

public class Account {
  private final int id;
  private String email;
  private String name;
  private String passwordHash;
  private final String role;
  private String status;

  public Account(int id, String email, String name, String passwordHash, String role, String status) {
    this.id = id;
    this.email = email;
    this.name = name;
    this.passwordHash = passwordHash;
    this.role = role;
    this.status = status;
  }

  public int getId() { return id; }
  public String getEmail() { return email; }
  public String getName() { return name; }
  public String getPasswordHash() { return passwordHash; }
  public String getRole() { return role; }
  public String getStatus() { return status; }

  public void setEmail(String email) { this.email = email; }
  public void setName(String name) { this.name = name; }
  public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
  public void setStatus(String status) { this.status = status; }

  public AccountDTO toDto() {
    return new AccountDTO(id, email, name, role, status);
  }
}
