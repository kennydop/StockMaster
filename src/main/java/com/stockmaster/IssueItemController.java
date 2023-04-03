package com.stockmaster;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class IssueItemController {
  @FXML
  private Button issueItemBtn;
  @FXML
  private DatePicker issuedDatePicker;
  @FXML
  private TextField issuedToField;
  @FXML
  private TextField issuedQuantityField;
  @FXML
  private ComboBox<String> itemToIssue;
  @FXML
  private Label itemToIssueLabel;

  private double unitCost = 0;

  public void initialize() {
    issueItemBtn.setOnAction(this::handleAddButtonClick);
    itemToIssue.setItems(InventoryController.getAllItemNames());
  }

  @FXML
  private void handleAddButtonClick(ActionEvent event) {

    if (InventoryController.toIssue.getQuantity() > InventoryController
        .searchForItemQuantity(itemToIssue.getValue().toString())) {
      System.out.println("Cannot issue more than available quantity");
      return;
    }

    // Set the values of the item to be issued
    InventoryController.toIssue.setIssuedDate(issuedDatePicker.getValue());
    InventoryController.toIssue.setIssuedTo(issuedToField.getText());
    InventoryController.toIssue.setQuantity(Integer.parseInt(issuedQuantityField.getText()));
    if (InventoryController.toIssue.getName() == null)
      InventoryController.toIssue.setName(itemToIssue.getValue().toString());
    if (InventoryController.toIssue.getCategory() == null)
      InventoryController.toIssue
          .setCategory(InventoryController.searchForItemCategory(itemToIssue.getValue().toString()));
    if (unitCost <= 0)
      unitCost = InventoryController.searchForItemSellingPrice(itemToIssue.getValue().toString());
    InventoryController.toIssue.setUnitCost(unitCost);

    // Close the window
    if (!InventoryController.toIssue.isNull()) {
      Stage stage = (Stage) issuedDatePicker.getScene().getWindow();
      stage.close();
      unitCost = 0;
    }

  }

  public void itemAlreadySelected(Item item) {
    if (item.isNull())
      return;
    // disable fields
    itemToIssueLabel.setVisible(false);
    itemToIssue.setVisible(false);

    // set item values
    itemToIssue.setValue(item.getName());
    unitCost = item.getSellingPrice();
  }
}
