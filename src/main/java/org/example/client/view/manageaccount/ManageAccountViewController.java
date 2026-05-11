package org.example.client.view.manageaccount;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.client.core.ViewHandler;
import org.example.client.viewmodel.manageaccount.ManageAccountViewModel;

public class ManageAccountViewController {

  @FXML private TextField nameField;
  @FXML private TextField emailField;
  @FXML private PasswordField passwordField;
  @FXML private Label errorLabel;
  @FXML private Label infoLabel;

  private ManageAccountViewModel viewModel;
  private ViewHandler viewHandler;

  public void init(ManageAccountViewModel viewModel, ViewHandler viewHandler) {
    this.viewModel = viewModel;
    this.viewHandler = viewHandler;

    nameField.textProperty().bindBidirectional(viewModel.nameProperty());
    emailField.textProperty().bindBidirectional(viewModel.emailProperty());
    passwordField.textProperty().bindBidirectional(viewModel.newPasswordProperty());
    errorLabel.textProperty().bind(viewModel.errorMessageProperty());
    infoLabel.textProperty().bind(viewModel.infoMessageProperty());
  }

  @FXML
  private void handleSave() {
    viewModel.save();
  }

  @FXML
  private void handleBack() {
    viewHandler.openView("main");
  }
}
