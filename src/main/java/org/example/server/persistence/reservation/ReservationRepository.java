package org.example.server.persistence.reservation;

import org.example.server.model.reservation.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

  Reservation add(int listingId, int customerId, String status);

  Optional<Reservation> findById(int id);

  List<Reservation> findByCustomer(int customerId);

  List<Reservation> findByListingIds(List<Integer> listingIds);

  void updateStatus(int id, String newStatus);
}
