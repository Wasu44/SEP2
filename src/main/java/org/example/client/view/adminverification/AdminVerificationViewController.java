package org.example.client.view.adminverification;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.client.core.ViewHandler;
import org.example.client.viewmodel.adminverification.AdminVerificationViewModel;
import org.example.shared.dto.AccountDTO;

public class AdminVerificationViewController {

  @FXML private TableView<AccountDTO> providersTable;
  @FXML private TableColumn<AccountDTO, Number> idColumn;
  @FXML private TableColumn<AccountDTO, String> emailColumn;
  @FXML private TableColumn<AccountDTO, String> nameColumn;
  @FXML private TableColumn<AccountDTO, String> statusColumn;
  @FXML private Label errorLabel;
  @FXML private Label infoLabel;

  private AdminVerificationViewModel viewModel;
  private ViewHandler viewHandler;

  public void init(AdminVerificationViewModel viewModel, ViewHandler viewHandler) {
    this.viewModel = viewModel;
    this.viewHandler = viewHandler;

    idColumn.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getId()));
    emailColumn.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getEmail()));
    nameColumn.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getName()));
    statusColumn.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getStatus()));

    providersTable.setItems(viewModel.getPending());
    errorLabel.textProperty().bind(viewModel.errorMessageProperty());
    infoLabel.textProperty().bind(viewModel.infoMessageProperty());

    viewModel.refresh();
  }

  @FXML
  private void handleVerify() {
    AccountDTO selected = providersTable.getSelectionModel().getSelectedItem();
    viewModel.verify(selected);
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
