package org.example.client.viewmodel.adminverification;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.client.core.Session;
import org.example.client.networking.ServerProxy;
import org.example.shared.dto.AccountDTO;
import org.example.shared.protocol.Response;

import java.util.List;

public class AdminVerificationViewModel {

  private final ServerProxy serverProxy;
  private final Session session;

  private final ObservableList<AccountDTO> pending = FXCollections.observableArrayList();
  private final StringProperty errorMessage = new SimpleStringProperty("");
  private final StringProperty infoMessage = new SimpleStringProperty("");

  public AdminVerificationViewModel(ServerProxy serverProxy, Session session) {
    this.serverProxy = serverProxy;
    this.session = session;
  }

  public ObservableList<AccountDTO> getPending() { return pending; }
  public StringProperty errorMessageProperty() { return errorMessage; }
  public StringProperty infoMessageProperty() { return infoMessage; }

  public void refresh() {
    errorMessage.set("");
    infoMessage.set("");
    AccountDTO admin = session.getCurrentAccount();
    if (admin == null) {
      errorMessage.set("Not logged in");
      pending.clear();
      return;
    }
    List<AccountDTO> latest = serverProxy.listPendingProviders(admin.getId());
    pending.setAll(latest);
  }

  public boolean verify(AccountDTO provider) {
    errorMessage.set("");
    infoMessage.set("");
    AccountDTO admin = session.getCurrentAccount();
    if (admin == null || provider == null) {
      errorMessage.set("Nothing selected");
      return false;
    }
    Response resp = serverProxy.verifyProvider(admin.getId(), provider.getId());
    if (!resp.isSuccess()) {
      errorMessage.set(resp.getMessage());
      return false;
    }
    infoMessage.set("Verified " + provider.getEmail());
    refresh();
    return true;
  }
}
