package org.example.client.viewmodel.reservation;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.client.core.Session;
import org.example.client.networking.ServerProxy;
import org.example.shared.dto.AccountDTO;
import org.example.shared.dto.ReservationDTO;
import org.example.shared.protocol.Response;

import java.util.List;

public class ReservationViewModel {

  private final ServerProxy serverProxy;
  private final Session session;

  private final ObservableList<ReservationDTO> reservations = FXCollections.observableArrayList();
  private final StringProperty errorMessage = new SimpleStringProperty("");
  private final StringProperty infoMessage = new SimpleStringProperty("");

  public ReservationViewModel(ServerProxy serverProxy, Session session) {
    this.serverProxy = serverProxy;
    this.session = session;
  }

  public ObservableList<ReservationDTO> getReservations() { return reservations; }
  public StringProperty errorMessageProperty() { return errorMessage; }
  public StringProperty infoMessageProperty() { return infoMessage; }

  public boolean isProvider() {
    AccountDTO acc = session.getCurrentAccount();
    return acc != null && AccountDTO.ROLE_PROVIDER.equals(acc.getRole());
  }

  public boolean isCustomer() {
    AccountDTO acc = session.getCurrentAccount();
    return acc != null && AccountDTO.ROLE_CUSTOMER.equals(acc.getRole());
  }

  public void refresh() {
    errorMessage.set("");
    infoMessage.set("");
    AccountDTO acc = session.getCurrentAccount();
    if (acc == null) { errorMessage.set("Not logged in"); reservations.clear(); return; }

    List<ReservationDTO> latest;
    if (isProvider())      latest = serverProxy.providerReservations(acc.getId());
    else if (isCustomer()) latest = serverProxy.myReservations(acc.getId());
    else                   latest = java.util.Collections.emptyList();
    reservations.setAll(latest);
  }

  public boolean confirm(ReservationDTO r) {
    return changeStatus(r, ReservationDTO.STATUS_CONFIRMED);
  }

  public boolean reject(ReservationDTO r) {
    return changeStatus(r, ReservationDTO.STATUS_REJECTED);
  }

  public boolean completePickup(ReservationDTO r) {
    return changeStatus(r, ReservationDTO.STATUS_COMPLETED);
  }

  private boolean changeStatus(ReservationDTO r, String newStatus) {
    errorMessage.set("");
    infoMessage.set("");
    AccountDTO acc = session.getCurrentAccount();
    if (acc == null || r == null) { errorMessage.set("Nothing selected"); return false; }
    Response resp = serverProxy.updateReservationStatus(acc.getId(), r.getId(), newStatus);
    if (!resp.isSuccess()) { errorMessage.set(resp.getMessage()); return false; }
    infoMessage.set("Reservation set to " + newStatus);
    refresh();
    return true;
  }
}
