package org.example.shared.dto;

import java.io.Serializable;

public class ListingDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  public static final String STATUS_AVAILABLE = "AVAILABLE";
  public static final String STATUS_RESERVED = "RESERVED";
  public static final String STATUS_COMPLETED = "COMPLETED";
  public static final String STATUS_CANCELLED = "CANCELLED";

  private int id;
  private int providerId;
  private String providerName;
  private String title;
  private String description;
  private int quantity;
  private String pickupWindow;
  private String status;

  public ListingDTO() {}

  public ListingDTO(int id, int providerId, String title, String description,
                    int quantity, String pickupWindow, String status) {
    this(id, providerId, null, title, description, quantity, pickupWindow, status);
  }

  public ListingDTO(int id, int providerId, String providerName, String title, String description,
                    int quantity, String pickupWindow, String status) {
    this.id = id;
    this.providerId = providerId;
    this.providerName = providerName;
    this.title = title;
    this.description = description;
    this.quantity = quantity;
    this.pickupWindow = pickupWindow;
    this.status = status;
  }

  public int getId() { return id; }
  public int getProviderId() { return providerId; }
  public String getProviderName() { return providerName; }
  public String getTitle() { return title; }
  public String getDescription() { return description; }
  public int getQuantity() { return quantity; }
  public String getPickupWindow() { return pickupWindow; }
  public String getStatus() { return status; }

  public void setId(int id) { this.id = id; }
  public void setProviderId(int providerId) { this.providerId = providerId; }
  public void setProviderName(String providerName) { this.providerName = providerName; }
  public void setTitle(String title) { this.title = title; }
  public void setDescription(String description) { this.description = description; }
  public void setQuantity(int quantity) { this.quantity = quantity; }
  public void setPickupWindow(String pickupWindow) { this.pickupWindow = pickupWindow; }
  public void setStatus(String status) { this.status = status; }
}
