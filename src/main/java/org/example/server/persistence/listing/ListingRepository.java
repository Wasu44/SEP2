package org.example.server.persistence.listing;

import org.example.server.model.listing.Listing;

import java.util.List;
import java.util.Optional;

public interface ListingRepository {

  Listing add(int providerId, String title, String description,
              int quantity, String pickupWindow, String status);

  Optional<Listing> findById(int id);

  List<Listing> findByProvider(int providerId);

  List<Listing> findAllAvailable();

  void updateStatus(int id, String newStatus);

  /** Atomic check-and-set: flips the listing from AVAILABLE to RESERVED.
   *  Returns true iff this caller is the one who actually moved it. */
  boolean reserveIfAvailable(int id);
}
