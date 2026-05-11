package org.example.server.persistence.listing;

import org.example.server.model.listing.Listing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryListingRepository implements ListingRepository {

  private final List<Listing> listings = new ArrayList<>();
  private final AtomicInteger idGenerator = new AtomicInteger(1);

  @Override
  public synchronized Listing add(int providerId, String title, String description,
                                  int quantity, String pickupWindow, String status) {
    Listing l = new Listing(idGenerator.getAndIncrement(),
        providerId, title, description, quantity, pickupWindow, status);
    listings.add(l);
    return l;
  }

  @Override
  public synchronized Optional<Listing> findById(int id) {
    for (Listing l : listings) {
      if (l.getId() == id) return Optional.of(l);
    }
    return Optional.empty();
  }

  @Override
  public synchronized List<Listing> findByProvider(int providerId) {
    List<Listing> result = new ArrayList<>();
    for (Listing l : listings) {
      if (l.getProviderId() == providerId) result.add(l);
    }
    return result;
  }

  @Override
  public synchronized List<Listing> findAllAvailable() {
    List<Listing> out = new ArrayList<>();
    for (Listing l : listings) {
      if ("AVAILABLE".equals(l.getStatus())) out.add(l);
    }
    return out;
  }

  @Override
  public synchronized void updateStatus(int id, String newStatus) {
    for (Listing l : listings) {
      if (l.getId() == id) {
        l.setStatus(newStatus);
        return;
      }
    }
  }
}
