package org.example.client.viewmodel;

import org.example.client.viewmodel.browselistings.BrowseListingsViewModel;
import org.example.shared.dto.ListingDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Unit tests for the package-private match helper. Avoids spinning up JavaFX. */
class BrowseFilterTest {

  private ListingDTO listing(String title, String desc, String provider) {
    return new ListingDTO(1, 1, provider, title, desc, 1, "now", "AVAILABLE");
  }

  @Test
  void matchesByTitle() {
    ListingDTO l = listing("Sourdough bread", "fresh today", "Bakery A");
    assertTrue(BrowseListingsViewModel.matches(l, "bread"));
    assertTrue(BrowseListingsViewModel.matches(l, "SOUR"));      // case-insensitive
  }

  @Test
  void matchesByDescription() {
    ListingDTO l = listing("Soup", "warm vegetable broth", "Cafe");
    assertTrue(BrowseListingsViewModel.matches(l, "vegetable"));
  }

  @Test
  void matchesByProviderName() {
    ListingDTO l = listing("Tea", "green", "Demo Provider");
    assertTrue(BrowseListingsViewModel.matches(l, "demo"));
  }

  @Test
  void doesNotMatchUnrelatedKeyword() {
    ListingDTO l = listing("Cake", "chocolate", "Bakery");
    assertFalse(BrowseListingsViewModel.matches(l, "noodles"));
  }
}
