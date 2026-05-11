package org.example.client.view.main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.client.core.ViewHandler;
import org.example.client.viewmodel.main.MainViewModel;
import org.example.shared.dto.AccountDTO;

public class MainViewController {

  @FXML private Label welcomeLabel;
  @FXML private Label roleLabel;
  @FXML private Button createListingButton;
  @FXML private Button myListingsButton;
  @FXML private Button providerReservationsButton;
  @FXML private Button browseListingsButton;
  @FXML private Button myReservationsButton;
  @FXML private Button adminVerifyButton;

  private MainViewModel viewModel;
  private ViewHandler viewHandler;

  public void init(MainViewModel viewModel, ViewHandler viewHandler) {
    this.viewModel = viewModel;
    this.viewHandler = viewHandler;

    welcomeLabel.textProperty().bind(viewModel.welcomeMessageProperty());
    roleLabel.textProperty().bind(viewModel.roleProperty());

    String role = viewModel.roleProperty().get();
    boolean provider = AccountDTO.ROLE_PROVIDER.equals(role);
    boolean customer = AccountDTO.ROLE_CUSTOMER.equals(role);
    boolean admin    = AccountDTO.ROLE_ADMIN.equals(role);

    show(createListingButton, provider);
    show(myListingsButton, provider);
    show(providerReservationsButton, provider);
    show(browseListingsButton, customer);
    show(myReservationsButton, customer);
    show(adminVerifyButton, admin);
  }

  private static void show(Button b, boolean visible) {
    b.setVisible(visible);
    b.setManaged(visible);
  }

  @FXML private void handleManageAccount()      { viewHandler.openView("manageAccount"); }
  @FXML private void handleCreateListing()      { viewHandler.openView("createListing"); }
  @FXML private void handleMyListings()         { viewHandler.openView("myListings"); }
  @FXML private void handleBrowseListings()     { viewHandler.openView("browseListings"); }
  @FXML private void handleReservations()       { viewHandler.openView("reservations"); }
  @FXML private void handleAdminVerification()  { viewHandler.openView("adminVerification"); }
  @FXML private void handleLogout()             { viewHandler.logout(); }
}
