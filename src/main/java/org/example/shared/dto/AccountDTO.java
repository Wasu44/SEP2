package org.example.shared.dto;

import java.io.Serializable;

public class AccountDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  public static final String ROLE_CUSTOMER = "CUSTOMER";
  public static final String ROLE_PROVIDER = "PROVIDER";
  public static final String ROLE_ADMIN    = "ADMIN";

  public static final String STATUS_ACTIVE = "ACTIVE";
  public static final String STATUS_PENDING = "PENDING";

  private int id;
  private String email;
  private String name;
  private String role;
  private String status;

  public AccountDTO() {}

  public AccountDTO(int id, String email, String name, String role, String status) {
    this.id = id;
    this.email = email;
    this.name = name;
    this.role = role;
    this.status = status;
  }

  public int getId() { return id; }
  public String getEmail() { return email; }
  public String getName() { return name; }
  public String getRole() { return role; }
  public String getStatus() { return status; }

  public void setId(int id) { this.id = id; }
  public void setEmail(String email) { this.email = email; }
  public void setName(String name) { this.name = name; }
  public void setRole(String role) { this.role = role; }
  public void setStatus(String status) { this.status = status; }
}
