package org.example.client.viewmodel.login;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.example.client.core.Session;
import org.example.client.networking.ServerProxy;
import org.example.shared.dto.AccountDTO;
import org.example.shared.protocol.Response;

public class LoginViewModel {

  private final ServerProxy serverProxy;
  private final Session session;

  private final StringProperty email = new SimpleStringProperty("");
  private final StringProperty password = new SimpleStringProperty("");
  private final StringProperty errorMessage = new SimpleStringProperty("");

  public LoginViewModel(ServerProxy serverProxy, Session session) {
    this.serverProxy = serverProxy;
    this.session = session;
  }

  public StringProperty emailProperty() { return email; }
  public StringProperty passwordProperty() { return password; }
  public StringProperty errorMessageProperty() { return errorMessage; }

  public boolean login() {
    errorMessage.set("");
    String e = email.get() == null ? "" : email.get().trim();
    String p = password.get() == null ? "" : password.get();
    if (e.isEmpty() || p.isEmpty()) {
      errorMessage.set("Email and password are required");
      return false;
    }
    Response resp = serverProxy.login(e, p);
    if (!resp.isSuccess()) {
      errorMessage.set(resp.getMessage());
      return false;
    }
    AccountDTO account = (AccountDTO) resp.getData();
    session.setCurrentAccount(account);
    return true;
  }
}
