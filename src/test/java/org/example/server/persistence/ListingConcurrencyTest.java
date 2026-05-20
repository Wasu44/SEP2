package org.example.server.persistence;

import org.example.server.model.listing.Listing;
import org.example.server.persistence.listing.InMemoryListingRepository;
import org.example.server.persistence.listing.ListingRepository;
import org.example.shared.dto.ListingDTO;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ListingConcurrencyTest {

  @Test
  void onlyOneOfManyConcurrentReservesSucceeds() throws Exception {
    ListingRepository repo = new InMemoryListingRepository();
    Listing l = repo.add(1, "Bread", "fresh", 5, "now", ListingDTO.STATUS_AVAILABLE);

    final int threads = 20;
    final CountDownLatch start = new CountDownLatch(1);
    final CountDownLatch done = new CountDownLatch(threads);
    final AtomicInteger winners = new AtomicInteger();

    for (int i = 0; i < threads; i++) {
      new Thread(() -> {
        try {
          start.await();
          if (repo.reserveIfAvailable(l.getId())) winners.incrementAndGet();
        } catch (InterruptedException ignored) {
        } finally {
          done.countDown();
        }
      }).start();
    }

    start.countDown();          // release all threads at once
    done.await();

    assertEquals(1, winners.get(), "exactly one thread should win the race");
    assertEquals(ListingDTO.STATUS_RESERVED, repo.findById(l.getId()).get().getStatus());
  }

  @Test
  void reserveIfAvailableFailsWhenAlreadyReserved() {
    ListingRepository repo = new InMemoryListingRepository();
    Listing l = repo.add(1, "Soup", "warm", 1, "now", ListingDTO.STATUS_AVAILABLE);
    assertTrue(repo.reserveIfAvailable(l.getId()));
    assertFalse(repo.reserveIfAvailable(l.getId()));     // second attempt is rejected
  }

  @Test
  void reserveIfAvailableFailsWhenListingMissing() {
    ListingRepository repo = new InMemoryListingRepository();
    assertFalse(repo.reserveIfAvailable(99999));
  }
}
