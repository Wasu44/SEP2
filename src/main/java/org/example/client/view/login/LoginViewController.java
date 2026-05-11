package org.example.client.view.login;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.client.core.ViewHandler;
import org.example.client.viewmodel.login.LoginViewModel;

public class LoginViewController {

  @FXML private TextField emailField;
  @FXML private PasswordField passwordField;
  @FXML private Label errorLabel;

  private LoginViewModel viewModel;
  private ViewHandler viewHandler;

  public void init(LoginViewModel viewModel, ViewHandler viewHandler) {
    this.viewModel = viewModel;
    this.viewHandler = viewHandler;

    emailField.textProperty().bindBidirectional(viewModel.emailProperty());
    passwordField.textProperty().bindBidirectional(viewModel.passwordProperty());
    errorLabel.textProperty().bind(viewModel.errorMessageProperty());
  }

  @FXML
  private void handleLogin() {
    if (viewModel.login()) {
      viewHandler.openView("main");
    }
  }

  @FXML
  private void handleGoToRegister() {
    viewHandler.openView("register");
  }
}
