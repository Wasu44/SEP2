package org.example.server.persistence;

import org.example.server.model.listing.Listing;
import org.example.server.persistence.listing.InMemoryListingRepository;
import org.example.server.persistence.listing.ListingRepository;
import org.example.shared.dto.ListingDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListingRepositoryTest {

  @Test
  void addsAndFindsByProvider() {
    ListingRepository repo = new InMemoryListingRepository();
    repo.add(1, "Bread", "fresh", 5, "now", ListingDTO.STATUS_AVAILABLE);
    repo.add(1, "Soup", "warm", 2, "later", ListingDTO.STATUS_AVAILABLE);
    repo.add(2, "Other", "x", 1, "now", ListingDTO.STATUS_AVAILABLE);

    List<Listing> mine = repo.findByProvider(1);
    assertEquals(2, mine.size());
  }

  @Test
  void findByIdReturnsCorrectListing() {
    ListingRepository repo = new InMemoryListingRepository();
    Listing l = repo.add(1, "Bread", "x", 1, "now", ListingDTO.STATUS_AVAILABLE);
    assertTrue(repo.findById(l.getId()).isPresent());
    assertFalse(repo.findById(99999).isPresent());
  }
}
