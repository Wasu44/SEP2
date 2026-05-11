package org.example.server.model;

import org.example.server.model.reservation.Reservation;
import org.example.shared.dto.ReservationDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReservationTransitionTest {

  private Reservation reservation(String status) {
    return new Reservation(1, 10, 20, status);
  }

  @Test
  void providerCanConfirmOrRejectPending() {
    Reservation r = reservation(ReservationDTO.STATUS_PENDING);
    assertTrue(r.canTransitionTo(ReservationDTO.STATUS_CONFIRMED, "PROVIDER"));
    assertTrue(r.canTransitionTo(ReservationDTO.STATUS_REJECTED, "PROVIDER"));
  }

  @Test
  void customerCannotChangePending() {
    Reservation r = reservation(ReservationDTO.STATUS_PENDING);
    assertFalse(r.canTransitionTo(ReservationDTO.STATUS_CONFIRMED, "CUSTOMER"));
    assertFalse(r.canTransitionTo(ReservationDTO.STATUS_REJECTED, "CUSTOMER"));
    assertFalse(r.canTransitionTo(ReservationDTO.STATUS_COMPLETED, "CUSTOMER"));
  }

  @Test
  void onlyCustomerCanCompleteConfirmedReservation() {
    Reservation r = reservation(ReservationDTO.STATUS_CONFIRMED);
    assertTrue(r.canTransitionTo(ReservationDTO.STATUS_COMPLETED, "CUSTOMER"));
    assertFalse(r.canTransitionTo(ReservationDTO.STATUS_COMPLETED, "PROVIDER"));
  }

  @Test
  void terminalStatesCannotTransition() {
    for (String terminal : new String[]{
        ReservationDTO.STATUS_REJECTED,
        ReservationDTO.STATUS_COMPLETED,
        ReservationDTO.STATUS_CANCELLED}) {
      Reservation r = reservation(terminal);
      assertFalse(r.canTransitionTo(ReservationDTO.STATUS_PENDING, "PROVIDER"));
      assertFalse(r.canTransitionTo(ReservationDTO.STATUS_CONFIRMED, "PROVIDER"));
      assertFalse(r.canTransitionTo(ReservationDTO.STATUS_COMPLETED, "CUSTOMER"));
    }
  }
}
