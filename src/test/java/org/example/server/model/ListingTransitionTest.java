package org.example.server.model;

import org.example.server.model.listing.Listing;
import org.example.shared.dto.ListingDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ListingTransitionTest {

  private Listing listing(String status) {
    return new Listing(1, 1, "T", "D", 5, "now", status);
  }

  @Test
  void availableCanGoToReservedOrCancelled() {
    Listing l = listing(ListingDTO.STATUS_AVAILABLE);
    assertTrue(l.canTransitionTo(ListingDTO.STATUS_RESERVED));
    assertTrue(l.canTransitionTo(ListingDTO.STATUS_CANCELLED));
    assertFalse(l.canTransitionTo(ListingDTO.STATUS_COMPLETED));
    assertFalse(l.canTransitionTo(ListingDTO.STATUS_AVAILABLE));
  }

  @Test
  void reservedCanGoToCompletedOrCancelled() {
    Listing l = listing(ListingDTO.STATUS_RESERVED);
    assertTrue(l.canTransitionTo(ListingDTO.STATUS_COMPLETED));
    assertTrue(l.canTransitionTo(ListingDTO.STATUS_CANCELLED));
    assertFalse(l.canTransitionTo(ListingDTO.STATUS_AVAILABLE));
    assertFalse(l.canTransitionTo(ListingDTO.STATUS_RESERVED));
  }

  @Test
  void terminalStatesCannotTransition() {
    Listing completed = listing(ListingDTO.STATUS_COMPLETED);
    Listing cancelled = listing(ListingDTO.STATUS_CANCELLED);
    assertFalse(completed.canTransitionTo(ListingDTO.STATUS_AVAILABLE));
    assertFalse(completed.canTransitionTo(ListingDTO.STATUS_RESERVED));
    assertFalse(cancelled.canTransitionTo(ListingDTO.STATUS_AVAILABLE));
    assertFalse(cancelled.canTransitionTo(ListingDTO.STATUS_RESERVED));
  }
}
