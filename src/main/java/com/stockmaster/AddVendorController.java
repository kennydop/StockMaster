package com.stockmaster;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddVendorController {
  @FXML
  private Button submitVendorBtn;
  @FXML
  private TextField vendorNameField;
  @FXML
  private TextField vendorAddressField;
  @FXML
  private TextField vendorPhoneField;
  @FXML
  private TextField vendorEmailField;

  public void initialize() {
    submitVendorBtn.setOnAction(this::handleAddButtonClick);
  }

  @FXML
  private void handleAddButtonClick(ActionEvent event) {
    VendorsController.toAdd.setName(vendorNameField.getText());
    VendorsController.toAdd.setAddress(vendorAddressField.getText());
    VendorsController.toAdd.setPhone(vendorPhoneField.getText());
    VendorsController.toAdd.setEmail(vendorEmailField.getText());

    // Close the window
    if (!VendorsController.toAdd.isNull()) {
      Stage stage = (Stage) vendorNameField.getScene().getWindow();
      stage.close();
    }

  }
}
