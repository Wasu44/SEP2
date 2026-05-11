package org.example.server.model.listing;

import org.example.shared.dto.ListingDTO;

public class Listing {
  private final int id;
  private final int providerId;
  private String title;
  private String description;
  private int quantity;
  private String pickupWindow;
  private String status;

  public Listing(int id, int providerId, String title, String description,
                 int quantity, String pickupWindow, String status) {
    this.id = id;
    this.providerId = providerId;
    this.title = title;
    this.description = description;
    this.quantity = quantity;
    this.pickupWindow = pickupWindow;
    this.status = status;
  }

  public int getId() { return id; }
  public int getProviderId() { return providerId; }
  public String getTitle() { return title; }
  public String getDescription() { return description; }
  public int getQuantity() { return quantity; }
  public String getPickupWindow() { return pickupWindow; }
  public String getStatus() { return status; }

  public void setStatus(String status) { this.status = status; }

  public boolean canTransitionTo(String newStatus) {
    if (newStatus == null) return false;
    switch (status) {
      case ListingDTO.STATUS_AVAILABLE:
        return newStatus.equals(ListingDTO.STATUS_RESERVED)
            || newStatus.equals(ListingDTO.STATUS_CANCELLED);
      case ListingDTO.STATUS_RESERVED:
        return newStatus.equals(ListingDTO.STATUS_COMPLETED)
            || newStatus.equals(ListingDTO.STATUS_CANCELLED);
      default:
        return false;
    }
  }

  public ListingDTO toDto() {
    return new ListingDTO(id, providerId, title, description, quantity, pickupWindow, status);
  }
}
