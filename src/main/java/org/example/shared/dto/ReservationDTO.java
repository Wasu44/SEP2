package org.example.shared.dto;

import java.io.Serializable;

public class ReservationDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  public static final String STATUS_PENDING   = "PENDING";   // waiting for provider decision
  public static final String STATUS_CONFIRMED = "CONFIRMED"; // provider accepted, awaiting pickup
  public static final String STATUS_REJECTED  = "REJECTED";  // provider declined
  public static final String STATUS_COMPLETED = "COMPLETED"; // customer confirmed pickup
  public static final String STATUS_CANCELLED = "CANCELLED"; // (reserved for later)

  private int id;
  private int listingId;
  private int customerId;
  private String listingTitle;
  private String customerName;
  private String status;

  public ReservationDTO() {}

  public ReservationDTO(int id, int listingId, int customerId,
                        String listingTitle, String customerName, String status) {
    this.id = id;
    this.listingId = listingId;
    this.customerId = customerId;
    this.listingTitle = listingTitle;
    this.customerName = customerName;
    this.status = status;
  }

  public int getId() { return id; }
  public int getListingId() { return listingId; }
  public int getCustomerId() { return customerId; }
  public String getListingTitle() { return listingTitle; }
  public String getCustomerName() { return customerName; }
  public String getStatus() { return status; }

  public void setId(int id) { this.id = id; }
  public void setListingId(int listingId) { this.listingId = listingId; }
  public void setCustomerId(int customerId) { this.customerId = customerId; }
  public void setListingTitle(String t) { this.listingTitle = t; }
  public void setCustomerName(String n) { this.customerName = n; }
  public void setStatus(String status) { this.status = status; }
}
