package org.example.server.model.reservation;

import org.example.shared.dto.ReservationDTO;

public class Reservation {
  private final int id;
  private final int listingId;
  private final int customerId;
  private String status;

  public Reservation(int id, int listingId, int customerId, String status) {
    this.id = id;
    this.listingId = listingId;
    this.customerId = customerId;
    this.status = status;
  }

  public int getId() { return id; }
  public int getListingId() { return listingId; }
  public int getCustomerId() { return customerId; }
  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }

  public boolean canTransitionTo(String newStatus, String role) {
    if (newStatus == null || role == null) return false;
    if (status.equals(ReservationDTO.STATUS_PENDING)) {
      return role.equals("PROVIDER")
          && (newStatus.equals(ReservationDTO.STATUS_CONFIRMED)
              || newStatus.equals(ReservationDTO.STATUS_REJECTED));
    }
    if (status.equals(ReservationDTO.STATUS_CONFIRMED)) {
      return role.equals("CUSTOMER") && newStatus.equals(ReservationDTO.STATUS_COMPLETED);
    }
    return false;
  }

  public ReservationDTO toDto(String listingTitle, String customerName) {
    return new ReservationDTO(id, listingId, customerId, listingTitle, customerName, status);
  }
}
