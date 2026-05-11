package org.example.client.view.browselistings;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.client.core.ViewHandler;
import org.example.client.viewmodel.browselistings.BrowseListingsViewModel;
import org.example.shared.dto.ListingDTO;

public class BrowseListingsViewController {

  @FXML private TableView<ListingDTO> listingsTable;
  @FXML private TableColumn<ListingDTO, Number> idColumn;
  @FXML private TableColumn<ListingDTO, String> titleColumn;
  @FXML private TableColumn<ListingDTO, Number> quantityColumn;
  @FXML private TableColumn<ListingDTO, String> pickupColumn;
  @FXML private TableColumn<ListingDTO, String> descColumn;
  @FXML private Label errorLabel;
  @FXML private Label infoLabel;

  private BrowseListingsViewModel viewModel;
  private ViewHandler viewHandler;

  public void init(BrowseListingsViewModel viewModel, ViewHandler viewHandler) {
    this.viewModel = viewModel;
    this.viewHandler = viewHandler;

    idColumn.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getId()));
    titleColumn.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getTitle()));
    quantityColumn.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getQuantity()));
    pickupColumn.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getPickupWindow()));
    descColumn.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getDescription()));

    listingsTable.setItems(viewModel.getListings());
    errorLabel.textProperty().bind(viewModel.errorMessageProperty());
    infoLabel.textProperty().bind(viewModel.infoMessageProperty());

    viewModel.refresh();
  }

  @FXML
  private void handleReserve() {
    ListingDTO selected = listingsTable.getSelectionModel().getSelectedItem();
    viewModel.reserve(selected);
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
