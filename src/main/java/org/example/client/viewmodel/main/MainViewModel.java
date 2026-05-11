package org.example.client.viewmodel.main;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.example.client.core.Session;
import org.example.shared.dto.AccountDTO;

public class MainViewModel {

  private final Session session;
  private final StringProperty welcomeMessage = new SimpleStringProperty("");
  private final StringProperty role = new SimpleStringProperty("");

  public MainViewModel(Session session) {
    this.session = session;
    refresh();
  }

  public void refresh() {
    AccountDTO acc = session.getCurrentAccount();
    if (acc != null) {
      welcomeMessage.set("Welcome, " + acc.getName());
      role.set(acc.getRole());
    } else {
      welcomeMessage.set("Welcome");
      role.set("");
    }
  }

  public StringProperty welcomeMessageProperty() { return welcomeMessage; }
  public StringProperty roleProperty() { return role; }

  public boolean isProvider() {
    return AccountDTO.ROLE_PROVIDER.equals(role.get());
  }
}
