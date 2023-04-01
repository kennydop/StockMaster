package com.stockmaster;

import java.io.IOException;

import com.stockmaster.Customer.CustomerType;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;

public class CustomersController {
  @FXML
  private TableView<Customer> customersTableView;
  @FXML
  private TableColumn<Customer, String> customerNameColumn;
  @FXML
  private TableColumn<Customer, String> customerTypeColumn;
  @FXML
  private TableColumn<Customer, String> customerPhoneColumn;
  @FXML
  private TableColumn<Customer, String> customerAddressColumn;
  @FXML
  private TableColumn<Customer, String> customerEmailColumn;
  @FXML
  private Button addCustomerBtn;
  @FXML
  private Button removeVendorBtn;

  public void initialize() {
    // Set the percentage widths for the columns
    customerNameColumn.prefWidthProperty().bind(customersTableView.widthProperty().subtract(6).multiply(0.20));
    customerTypeColumn.prefWidthProperty().bind(customersTableView.widthProperty().subtract(6).multiply(0.20));
    customerPhoneColumn.prefWidthProperty().bind(customersTableView.widthProperty().subtract(6).multiply(0.20));
    customerAddressColumn.prefWidthProperty().bind(customersTableView.widthProperty().subtract(6).multiply(0.20));
    customerEmailColumn.prefWidthProperty().bind(customersTableView.widthProperty().subtract(6).multiply(0.20));

    // Set the cell value factories
    customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    customerTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
    customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
    customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
    customerEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

    // Create sample data
    ObservableList<Customer> customers = FXCollections.observableArrayList(
        new Customer("Customer 1", "Ayawaso West", "026132748", "mdpot@coco.co",
            CustomerType.Wholesale),
        new Customer("Customer 2", "Dzemeni", "0423629234", "opk@toure.xo",
            CustomerType.Online),
        new Customer("Customer 3", "Behind Lake Bosomtwe", "065526477",
            "v3@customers.yat", CustomerType.Retail),
        new Customer("Customer 4", "Philadelphia, Kumasi", "0123456789",
            "driller@yga.ken", CustomerType.Online));

    // Set the items in the ListView
    customersTableView.setItems(customers);

    addCustomerBtn.setOnAction(e -> openAddCustomerPopup());

  }

  private void openAddCustomerPopup() {
    try {
      // Load the addCustomer.fxml file
      FXMLLoader loader = new FXMLLoader(getClass().getResource("addCustomer.fxml"));
      Parent addCustomerRoot = loader.load();

      // Create a new stage for the popup
      Stage addCustomerStage = new Stage();
      addCustomerStage.initModality(Modality.APPLICATION_MODAL);
      addCustomerStage.setTitle("Add New Customer");
      addCustomerStage.setScene(new Scene(addCustomerRoot));
      addCustomerStage.setResizable(false);

      // Show the popup and wait for it to be closed
      addCustomerStage.showAndWait();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
