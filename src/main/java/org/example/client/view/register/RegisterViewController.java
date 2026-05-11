package org.example.client.view.register;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.client.core.ViewHandler;
import org.example.client.viewmodel.register.RegisterViewModel;

public class RegisterViewController {

  @FXML private TextField nameField;
  @FXML private TextField emailField;
  @FXML private PasswordField passwordField;
  @FXML private ChoiceBox<String> roleChoice;
  @FXML private Label errorLabel;
  @FXML private Label infoLabel;

  private RegisterViewModel viewModel;
  private ViewHandler viewHandler;

  public void init(RegisterViewModel viewModel, ViewHandler viewHandler) {
    this.viewModel = viewModel;
    this.viewHandler = viewHandler;

    nameField.textProperty().bindBidirectional(viewModel.nameProperty());
    emailField.textProperty().bindBidirectional(viewModel.emailProperty());
    passwordField.textProperty().bindBidirectional(viewModel.passwordProperty());
    roleChoice.valueProperty().bindBidirectional(viewModel.roleProperty());
    if (roleChoice.getValue() == null) roleChoice.setValue("CUSTOMER");

    errorLabel.textProperty().bind(viewModel.errorMessageProperty());
    infoLabel.textProperty().bind(viewModel.infoMessageProperty());
  }

  @FXML
  private void handleRegister() {
    viewModel.register();
  }

  @FXML
  private void handleGoToLogin() {
    viewHandler.openView("login");
  }
}
