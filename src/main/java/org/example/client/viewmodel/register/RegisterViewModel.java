package org.example.client.viewmodel.register;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.example.client.networking.ServerProxy;
import org.example.shared.dto.AccountDTO;
import org.example.shared.protocol.Response;

public class RegisterViewModel {

  private final ServerProxy serverProxy;

  private final StringProperty email = new SimpleStringProperty("");
  private final StringProperty name = new SimpleStringProperty("");
  private final StringProperty password = new SimpleStringProperty("");
  private final StringProperty role = new SimpleStringProperty(AccountDTO.ROLE_CUSTOMER);
  private final StringProperty errorMessage = new SimpleStringProperty("");
  private final StringProperty infoMessage = new SimpleStringProperty("");

  public RegisterViewModel(ServerProxy serverProxy) {
    this.serverProxy = serverProxy;
  }

  public StringProperty emailProperty() { return email; }
  public StringProperty nameProperty() { return name; }
  public StringProperty passwordProperty() { return password; }
  public StringProperty roleProperty() { return role; }
  public StringProperty errorMessageProperty() { return errorMessage; }
  public StringProperty infoMessageProperty() { return infoMessage; }

  public boolean register() {
    errorMessage.set("");
    infoMessage.set("");

    String e = email.get() == null ? "" : email.get().trim();
    String n = name.get() == null ? "" : name.get().trim();
    String p = password.get() == null ? "" : password.get();
    String r = role.get();

    if (e.isEmpty() || n.isEmpty() || p.isEmpty() || r == null || r.isEmpty()) {
      errorMessage.set("All fields are required");
      return false;
    }
    if (!e.contains("@") || !e.contains(".")) {
      errorMessage.set("Invalid email format");
      return false;
    }
    if (p.length() < 6) {
      errorMessage.set("Password must be at least 6 characters");
      return false;
    }

    Response resp = serverProxy.register(e, n, p, r);
    if (!resp.isSuccess()) {
      errorMessage.set(resp.getMessage());
      return false;
    }
    AccountDTO created = (AccountDTO) resp.getData();
    if (AccountDTO.STATUS_PENDING.equals(created.getStatus())) {
      infoMessage.set("Provider account created. Awaiting admin verification.");
    } else {
      infoMessage.set("Account created. You can now log in.");
    }
    return true;
  }
}
