package org.example.server.persistence.reservation;

import org.example.server.model.reservation.Reservation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryReservationRepository implements ReservationRepository {

  private final List<Reservation> reservations = new ArrayList<>();
  private final AtomicInteger idGenerator = new AtomicInteger(1);

  @Override
  public synchronized Reservation add(int listingId, int customerId, String status) {
    Reservation r = new Reservation(idGenerator.getAndIncrement(), listingId, customerId, status);
    reservations.add(r);
    return r;
  }

  @Override
  public synchronized Optional<Reservation> findById(int id) {
    for (Reservation r : reservations) {
      if (r.getId() == id) return Optional.of(r);
    }
    return Optional.empty();
  }

  @Override
  public synchronized List<Reservation> findByCustomer(int customerId) {
    List<Reservation> out = new ArrayList<>();
    for (Reservation r : reservations) {
      if (r.getCustomerId() == customerId) out.add(r);
    }
    return out;
  }

  @Override
  public synchronized List<Reservation> findByListingIds(List<Integer> listingIds) {
    List<Reservation> out = new ArrayList<>();
    for (Reservation r : reservations) {
      if (listingIds.contains(r.getListingId())) out.add(r);
    }
    return out;
  }

  @Override
  public synchronized void updateStatus(int id, String newStatus) {
    for (Reservation r : reservations) {
      if (r.getId() == id) {
        r.setStatus(newStatus);
        return;
      }
    }
  }
}
