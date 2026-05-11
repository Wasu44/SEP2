package org.example.client.viewmodel.browselistings;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.client.core.Session;
import org.example.client.networking.ServerProxy;
import org.example.shared.dto.AccountDTO;
import org.example.shared.dto.ListingDTO;
import org.example.shared.protocol.Response;

import java.util.List;

public class BrowseListingsViewModel {

  private final ServerProxy serverProxy;
  private final Session session;

  private final ObservableList<ListingDTO> listings = FXCollections.observableArrayList();
  private final StringProperty errorMessage = new SimpleStringProperty("");
  private final StringProperty infoMessage = new SimpleStringProperty("");

  public BrowseListingsViewModel(ServerProxy serverProxy, Session session) {
    this.serverProxy = serverProxy;
    this.session = session;
  }

  public ObservableList<ListingDTO> getListings() { return listings; }
  public StringProperty errorMessageProperty() { return errorMessage; }
  public StringProperty infoMessageProperty() { return infoMessage; }

  public void refresh() {
    errorMessage.set("");
    infoMessage.set("");
    AccountDTO acc = session.getCurrentAccount();
    if (acc == null) {
      errorMessage.set("Not logged in");
      listings.clear();
      return;
    }
    List<ListingDTO> latest = serverProxy.browseListings(acc.getId());
    listings.setAll(latest);
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
      return false;
    }
    infoMessage.set("Reserved \"" + listing.getTitle() + "\". Awaiting provider confirmation.");
    refresh();
    return true;
  }
}
