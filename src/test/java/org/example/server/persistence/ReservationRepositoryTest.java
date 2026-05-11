package org.example.server.persistence;

import org.example.server.model.reservation.Reservation;
import org.example.server.persistence.reservation.InMemoryReservationRepository;
import org.example.server.persistence.reservation.ReservationRepository;
import org.example.shared.dto.ReservationDTO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationRepositoryTest {

  @Test
  void addAndFindByCustomer() {
    ReservationRepository repo = new InMemoryReservationRepository();
    repo.add(1, 100, ReservationDTO.STATUS_PENDING);
    repo.add(2, 100, ReservationDTO.STATUS_PENDING);
    repo.add(3, 200, ReservationDTO.STATUS_PENDING);
    assertEquals(2, repo.findByCustomer(100).size());
    assertEquals(1, repo.findByCustomer(200).size());
  }

  @Test
  void findByListingIds() {
    ReservationRepository repo = new InMemoryReservationRepository();
    repo.add(10, 1, ReservationDTO.STATUS_PENDING);
    repo.add(20, 1, ReservationDTO.STATUS_PENDING);
    repo.add(30, 2, ReservationDTO.STATUS_PENDING);
    List<Reservation> found = repo.findByListingIds(Arrays.asList(10, 20));
    assertEquals(2, found.size());
  }

  @Test
  void updateStatusFlipsValue() {
    ReservationRepository repo = new InMemoryReservationRepository();
    Reservation r = repo.add(1, 1, ReservationDTO.STATUS_PENDING);
    repo.updateStatus(r.getId(), ReservationDTO.STATUS_CONFIRMED);
    assertEquals(ReservationDTO.STATUS_CONFIRMED, repo.findById(r.getId()).get().getStatus());
  }
}
