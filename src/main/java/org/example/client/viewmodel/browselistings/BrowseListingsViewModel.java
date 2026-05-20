package org.example.client.viewmodel.browselistings;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import org.example.client.core.Session;
import org.example.client.networking.ServerProxy;
import org.example.shared.dto.AccountDTO;
import org.example.shared.dto.ListingDTO;
import org.example.shared.protocol.Response;

import java.util.List;

public class BrowseListingsViewModel {

  private final ServerProxy serverProxy;
  private final Session session;

  // Master list (everything loaded from the server) and a filtered view bound to the table.
  private final ObservableList<ListingDTO> allListings = FXCollections.observableArrayList();
  private final FilteredList<ListingDTO> visibleListings = new FilteredList<>(allListings, l -> true);

  private final StringProperty filterText = new SimpleStringProperty("");
  private final StringProperty errorMessage = new SimpleStringProperty("");
  private final StringProperty infoMessage = new SimpleStringProperty("");

  public BrowseListingsViewModel(ServerProxy serverProxy, Session session) {
    this.serverProxy = serverProxy;
    this.session = session;

    // Re-evaluate predicate whenever the search text changes.
    filterText.addListener((obs, oldV, newV) -> applyFilter());
  }

  public ObservableList<ListingDTO> getListings() { return visibleListings; }
  public StringProperty filterTextProperty() { return filterText; }
  public StringProperty errorMessageProperty() { return errorMessage; }
  public StringProperty infoMessageProperty() { return infoMessage; }

  public void refresh() {
    errorMessage.set("");
    infoMessage.set("");
    AccountDTO acc = session.getCurrentAccount();
    if (acc == null) {
      errorMessage.set("Not logged in");
      allListings.clear();
      return;
    }
    List<ListingDTO> latest = serverProxy.browseListings(acc.getId());
    allListings.setAll(latest);
    applyFilter();
  }

  public boolean reserve(ListingDTO listing) {
    errorMessage.set("");
    infoMessage.set("");
    AccountDTO acc = session.getCurrentAccount();
    if (acc == null || listing == null) {
      errorMessage.set("Nothing selected");
      return false;
    }
    Response resp = serverProxy.createReservation(acc.getId(), listing.getId());
    if (!resp.isSuccess()) {
      errorMessage.set(resp.getMessage());
      // Refresh to drop the now-unavailable item from view.
      refresh();
      return false;
    }
    infoMessage.set("Reserved \"" + listing.getTitle() + "\". Awaiting provider confirmation.");
    refresh();
    return true;
  }

  private void applyFilter() {
    String raw = filterText.get();
    final String q = raw == null ? "" : raw.trim().toLowerCase();
    if (q.isEmpty()) {
      visibleListings.setPredicate(l -> true);
    } else {
      visibleListings.setPredicate(l -> matches(l, q));
    }
  }

  public static boolean matches(ListingDTO l, String q) {
    if (l == null || q == null) return false;
    String needle = q.toLowerCase();
    if (contains(l.getTitle(), needle))        return true;
    if (contains(l.getDescription(), needle))  return true;
    if (contains(l.getProviderName(), needle)) return true;
    return false;
  }

  private static boolean contains(String s, String needle) {
    return s != null && s.toLowerCase().contains(needle);
  }
}
