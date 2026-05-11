package org.example.client.view.reservation;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.client.core.ViewHandler;
import org.example.client.viewmodel.reservation.ReservationViewModel;
import org.example.shared.dto.ReservationDTO;

public class ReservationViewController {

  @FXML private Label titleLabel;
  @FXML private TableView<ReservationDTO> reservationsTable;
  @FXML private TableColumn<ReservationDTO, Number> idColumn;
  @FXML private TableColumn<ReservationDTO, String> listingColumn;
  @FXML private TableColumn<ReservationDTO, String> customerColumn;
  @FXML private TableColumn<ReservationDTO, String> statusColumn;
  @FXML private Button confirmButton;
  @FXML private Button rejectButton;
  @FXML private Button pickupButton;
  @FXML private Label errorLabel;
  @FXML private Label infoLabel;

  private ReservationViewModel viewModel;
  private ViewHandler viewHandler;

  public void init(ReservationViewModel viewModel, ViewHandler viewHandler) {
    this.viewModel = viewModel;
    this.viewHandler = viewHandler;

    idColumn.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getId()));
    listingColumn.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getListingTitle()));
    customerColumn.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getCustomerName()));
    statusColumn.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getStatus()));

    reservationsTable.setItems(viewModel.getReservations());
    errorLabel.textProperty().bind(viewModel.errorMessageProperty());
    infoLabel.textProperty().bind(viewModel.infoMessageProperty());

    boolean isProvider = viewModel.isProvider();
    boolean isCustomer = viewModel.isCustomer();
    titleLabel.setText(isProvider ? "Incoming reservations" : "My reservations");
    confirmButton.setVisible(isProvider);  confirmButton.setManaged(isProvider);
    rejectButton.setVisible(isProvider);   rejectButton.setManaged(isProvider);
    pickupButton.setVisible(isCustomer);   pickupButton.setManaged(isCustomer);

    viewModel.refresh();
  }

  @FXML
  private void handleConfirm() {
    viewModel.confirm(reservationsTable.getSelectionModel().getSelectedItem());
  }

  @FXML
  private void handleReject() {
    viewModel.reject(reservationsTable.getSelectionModel().getSelectedItem());
  }

  @FXML
  private void handleCompletePickup() {
    viewModel.completePickup(reservationsTable.getSelectionModel().getSelectedItem());
  }

  @FXML
  private void handleRefresh() {
    viewModel.refresh();
  }

  @FXML
  private void handleBack() {
    viewHandler.openView("main");
  }
}
