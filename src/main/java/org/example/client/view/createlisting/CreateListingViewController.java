package org.example.client.view.createlisting;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.client.core.ViewHandler;
import org.example.client.viewmodel.createlisting.CreateListingViewModel;

public class CreateListingViewController {

  @FXML private TextField titleField;
  @FXML private TextArea descriptionField;
  @FXML private TextField quantityField;
  @FXML private TextField pickupWindowField;
  @FXML private Label errorLabel;
  @FXML private Label infoLabel;

  private CreateListingViewModel viewModel;
  private ViewHandler viewHandler;

  public void init(CreateListingViewModel viewModel, ViewHandler viewHandler) {
    this.viewModel = viewModel;
    this.viewHandler = viewHandler;

    titleField.textProperty().bindBidirectional(viewModel.titleProperty());
    descriptionField.textProperty().bindBidirectional(viewModel.descriptionProperty());
    quantityField.textProperty().bindBidirectional(viewModel.quantityProperty());
    pickupWindowField.textProperty().bindBidirectional(viewModel.pickupWindowProperty());
    errorLabel.textProperty().bind(viewModel.errorMessageProperty());
    infoLabel.textProperty().bind(viewModel.infoMessageProperty());
  }

  @FXML
  private void handleCreate() {
    viewModel.create();
  }

  @FXML
  private void handleBack() {
    viewHandler.openView("main");
  }
}
