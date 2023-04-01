package com.stockmaster;

import java.util.Date;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddItemController {
  @FXML
  private Button submitItemBtn;
  @FXML
  private TextField itemNameField;
  @FXML
  private ComboBox<String> addItemCategoryDropdown;
  @FXML
  private ComboBox<String> unitOfMeasurementDropdown;
  @FXML
  private TextField quantityField;
  @FXML
  private TextField costPriceField;
  @FXML
  private TextField sellingPriceField;
  @FXML
  private DatePicker expiryDatePicker;
  @FXML
  private ComboBox<String> vendorDropdown;

  public void initialize() {
    submitItemBtn.setOnAction(this::handleAddButtonClick);
  }

  @FXML
  private void handleAddButtonClick(ActionEvent event) {
    InventoryController.toAdd.setSold(0);
    InventoryController.toAdd.setName(itemNameField.getText());
    InventoryController.toAdd.setCategory(addItemCategoryDropdown.getValue().toString());
    InventoryController.toAdd.setUnitOfMeasurement(unitOfMeasurementDropdown.getValue().toString());
    InventoryController.toAdd.setCostPrice(Double.parseDouble(costPriceField.getText()));
    InventoryController.toAdd.setSellingPrice(Double.parseDouble(sellingPriceField.getText()));
    InventoryController.toAdd.setQuantity(Integer.parseInt(quantityField.getText()));
    InventoryController.toAdd.setExpiryDate(expiryDatePicker.getValue());
    InventoryController.toAdd.setVendor(vendorDropdown.getValue().toString());
    InventoryController.toAdd.setCreatedAt(new Date());

    // Close the window
    if (!InventoryController.toAdd.isNull()) {
      Stage stage = (Stage) itemNameField.getScene().getWindow();
      stage.close();
    }

    // TODO: parse negative values
  }

  public void setSelectedCategory(String category) {
    System.out.println("Selecting category " + category);
    System.out.println(category != null && addItemCategoryDropdown != null && category != "All");
    if (category != null && addItemCategoryDropdown != null && category != "All") {
      addItemCategoryDropdown.getSelectionModel().select(category);
    }
  }
}
