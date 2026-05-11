package org.example.client.core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.example.client.view.adminverification.AdminVerificationViewController;
import org.example.client.view.browselistings.BrowseListingsViewController;
import org.example.client.view.createlisting.CreateListingViewController;
import org.example.client.view.login.LoginViewController;
import org.example.client.view.main.MainViewController;
import org.example.client.view.manageaccount.ManageAccountViewController;
import org.example.client.view.mylistings.MyListingsViewController;
import org.example.client.view.register.RegisterViewController;
import org.example.client.view.reservation.ReservationViewController;

public class ViewHandler {

  private final ViewModelFactory viewModelFactory;
  private final Session session;
  private Stage primaryStage;
  private Scene currentScene;

  public ViewHandler(ViewModelFactory viewModelFactory, Session session) {
    this.viewModelFactory = viewModelFactory;
    this.session = session;
    this.currentScene = new Scene(new Region());
  }

  public void start(Stage stage) {
    this.primaryStage = stage;
    openView("login");
  }

  public void openView(String viewId) {
    try {
      FXMLLoader loader = new FXMLLoader();
      Region root;

      switch (viewId) {
        case "login": {
          loader.setLocation(getClass().getResource("/client/view/login/LoginView.fxml"));
          root = loader.load();
          LoginViewController c = loader.getController();
          c.init(viewModelFactory.getLoginViewModel(), this);
          break;
        }
        case "register": {
          loader.setLocation(getClass().getResource("/client/view/register/RegisterView.fxml"));
          root = loader.load();
          RegisterViewController c = loader.getController();
          c.init(viewModelFactory.getRegisterViewModel(), this);
          break;
        }
        case "main": {
          loader.setLocation(getClass().getResource("/client/view/main/MainView.fxml"));
          root = loader.load();
          MainViewController c = loader.getController();
          c.init(viewModelFactory.getMainViewModel(), this);
          break;
        }
        case "manageAccount": {
          loader.setLocation(getClass().getResource("/client/view/manageaccount/ManageAccountView.fxml"));
          root = loader.load();
          ManageAccountViewController c = loader.getController();
          c.init(viewModelFactory.getManageAccountViewModel(), this);
          break;
        }
        case "createListing": {
          loader.setLocation(getClass().getResource("/client/view/createlisting/CreateListingView.fxml"));
          root = loader.load();
          CreateListingViewController c = loader.getController();
          c.init(viewModelFactory.getCreateListingViewModel(), this);
          break;
        }
        case "myListings": {
          loader.setLocation(getClass().getResource("/client/view/mylistings/MyListingsView.fxml"));
          root = loader.load();
          MyListingsViewController c = loader.getController();
          c.init(viewModelFactory.getMyListingsViewModel(), this);
          break;
        }
        case "adminVerification": {
          loader.setLocation(getClass().getResource("/client/view/adminverification/AdminVerificationView.fxml"));
          root = loader.load();
          AdminVerificationViewController c = loader.getController();
          c.init(viewModelFactory.getAdminVerificationViewModel(), this);
          break;
        }
        case "browseListings": {
          loader.setLocation(getClass().getResource("/client/view/browselistings/BrowseListingsView.fxml"));
          root = loader.load();
          BrowseListingsViewController c = loader.getController();
          c.init(viewModelFactory.getBrowseListingsViewModel(), this);
          break;
        }
        case "reservations": {
          loader.setLocation(getClass().getResource("/client/view/reservation/ReservationView.fxml"));
          root = loader.load();
          ReservationViewController c = loader.getController();
          c.init(viewModelFactory.getReservationViewModel(), this);
          break;
        }
        default:
          throw new IllegalArgumentException("Unknown view: " + viewId);
      }

      currentScene.setRoot(root);
      primaryStage.setScene(currentScene);
      primaryStage.setTitle("Food Waste Redistribution");
      primaryStage.show();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void logout() {
    session.clear();
    openView("login");
  }
}
