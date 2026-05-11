package org.example.client.view.mylistings;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.client.core.ViewHandler;
import org.example.client.viewmodel.mylistings.MyListingsViewModel;
import org.example.shared.dto.ListingDTO;

public class MyListingsViewController {

  @FXML private TableView<ListingDTO> listingsTable;
  @FXML private TableColumn<ListingDTO, Number> idColumn;
  @FXML private TableColumn<ListingDTO, String> titleColumn;
  @FXML private TableColumn<ListingDTO, Number> quantityColumn;
  @FXML private TableColumn<ListingDTO, String> pickupColumn;
  @FXML private TableColumn<ListingDTO, String> statusColumn;
  @FXML private ChoiceBox<String> statusChoice;
  @FXML private Label errorLabel;
  @FXML private Label infoLabel;

  private MyListingsViewModel viewModel;
  private ViewHandler viewHandler;

  public void init(MyListingsViewModel viewModel, ViewHandler viewHandler) {
    this.viewModel = viewModel;
    this.viewHandler = viewHandler;

    idColumn.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getId()));
    titleColumn.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getTitle()));
    quantityColumn.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getQuantity()));
    pickupColumn.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getPickupWindow()));
    statusColumn.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getStatus()));

    listingsTable.setItems(viewModel.getListings());

    statusChoice.setItems(FXCollections.observableArrayList(
        ListingDTO.STATUS_RESERVED,
        ListingDTO.STATUS_COMPLETED,
        ListingDTO.STATUS_CANCELLED));
    statusChoice.setValue(ListingDTO.STATUS_RESERVED);

    errorLabel.textProperty().bind(viewModel.errorMessageProperty());
    infoLabel.textProperty().bind(viewModel.infoMessageProperty());

    viewModel.refresh();
  }

  @FXML
  private void handleUpdateStatus() {
    ListingDTO selected = listingsTable.getSelectionModel().getSelectedItem();
    String newStatus = statusChoice.getValue();
    viewModel.updateStatus(selected, newStatus);
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
