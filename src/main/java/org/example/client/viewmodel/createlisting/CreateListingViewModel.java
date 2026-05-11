package org.example.client.viewmodel.createlisting;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.example.client.core.Session;
import org.example.client.networking.ServerProxy;
import org.example.shared.dto.AccountDTO;
import org.example.shared.protocol.Response;

public class CreateListingViewModel {

  private final ServerProxy serverProxy;
  private final Session session;

  private final StringProperty title = new SimpleStringProperty("");
  private final StringProperty description = new SimpleStringProperty("");
  private final StringProperty quantity = new SimpleStringProperty("");
  private final StringProperty pickupWindow = new SimpleStringProperty("");
  private final StringProperty errorMessage = new SimpleStringProperty("");
  private final StringProperty infoMessage = new SimpleStringProperty("");

  public CreateListingViewModel(ServerProxy serverProxy, Session session) {
    this.serverProxy = serverProxy;
    this.session = session;
  }

  public StringProperty titleProperty() { return title; }
  public StringProperty descriptionProperty() { return description; }
  public StringProperty quantityProperty() { return quantity; }
  public StringProperty pickupWindowProperty() { return pickupWindow; }
  public StringProperty errorMessageProperty() { return errorMessage; }
  public StringProperty infoMessageProperty() { return infoMessage; }

  public boolean create() {
    errorMessage.set("");
    infoMessage.set("");

    AccountDTO acc = session.getCurrentAccount();
    if (acc == null) {
      errorMessage.set("Not logged in");
      return false;
    }

    String t = title.get() == null ? "" : title.get().trim();
    String d = description.get() == null ? "" : description.get().trim();
    String pw = pickupWindow.get() == null ? "" : pickupWindow.get().trim();
    String qStr = quantity.get() == null ? "" : quantity.get().trim();

    if (t.isEmpty() || pw.isEmpty() || qStr.isEmpty()) {
      errorMessage.set("Title, quantity and pickup window are required");
      return false;
    }
    int q;
    try {
      q = Integer.parseInt(qStr);
    } catch (NumberFormatException ex) {
      errorMessage.set("Quantity must be a whole number");
      return false;
    }
    if (q <= 0) {
      errorMessage.set("Quantity must be greater than zero");
      return false;
    }

    Response resp = serverProxy.createListing(acc.getId(), t, d, q, pw);
    if (!resp.isSuccess()) {
      errorMessage.set(resp.getMessage());
      return false;
    }
    infoMessage.set("Listing created");
    title.set("");
    description.set("");
    quantity.set("");
    pickupWindow.set("");
    return true;
  }
}
