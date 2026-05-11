package org.example.client.viewmodel.manageaccount;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.example.client.core.Session;
import org.example.client.networking.ServerProxy;
import org.example.shared.dto.AccountDTO;
import org.example.shared.protocol.Response;

public class ManageAccountViewModel {

  private final ServerProxy serverProxy;
  private final Session session;

  private final StringProperty name = new SimpleStringProperty("");
  private final StringProperty email = new SimpleStringProperty("");
  private final StringProperty newPassword = new SimpleStringProperty("");
  private final StringProperty errorMessage = new SimpleStringProperty("");
  private final StringProperty infoMessage = new SimpleStringProperty("");

  public ManageAccountViewModel(ServerProxy serverProxy, Session session) {
    this.serverProxy = serverProxy;
    this.session = session;
    loadCurrent();
  }

  public void loadCurrent() {
    AccountDTO acc = session.getCurrentAccount();
    if (acc != null) {
      name.set(acc.getName());
      email.set(acc.getEmail());
    }
    newPassword.set("");
    errorMessage.set("");
    infoMessage.set("");
  }

  public StringProperty nameProperty() { return name; }
  public StringProperty emailProperty() { return email; }
  public StringProperty newPasswordProperty() { return newPassword; }
  public StringProperty errorMessageProperty() { return errorMessage; }
  public StringProperty infoMessageProperty() { return infoMessage; }

  public boolean save() {
    errorMessage.set("");
    infoMessage.set("");
    AccountDTO acc = session.getCurrentAccount();
    if (acc == null) {
      errorMessage.set("Not logged in");
      return false;
    }
    String n = name.get() == null ? "" : name.get().trim();
    String e = email.get() == null ? "" : email.get().trim();
    String p = newPassword.get() == null ? "" : newPassword.get();
    if (n.isEmpty() || e.isEmpty()) {
      errorMessage.set("Name and email cannot be empty");
      return false;
    }
    Response resp = serverProxy.updateAccount(acc.getId(), n, e, p);
    if (!resp.isSuccess()) {
      errorMessage.set(resp.getMessage());
      return false;
    }
    session.setCurrentAccount((AccountDTO) resp.getData());
    infoMessage.set("Account updated");
    newPassword.set("");
    return true;
  }
}
