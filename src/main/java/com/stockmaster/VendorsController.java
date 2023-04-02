package com.stockmaster;

import java.io.IOException;
import java.util.HashMap;

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

public class VendorsController {
  @FXML
  private TableView<Vendor> vendorsTableView;
  @FXML
  private TableColumn<Vendor, String> vendorNameColumn;
  @FXML
  private TableColumn<Vendor, String> vendorPhoneColumn;
  @FXML
  private TableColumn<Vendor, String> vendorAddressColumn;
  @FXML
  private TableColumn<Vendor, String> vendorEmailColumn;
  @FXML
  private Button addVendorBtn;
  @FXML
  private Button removeVendorBtn;

  private String selectedKey = "";

  public static ObservableList<String> vendorNames = FXCollections.observableArrayList();

  private DBHashMap<String, HashMap<String, String>> vendors = new DBHashMap<String, HashMap<String, String>>(
      "vendors");
  ObservableList<Vendor> vendorsList = FXCollections.observableArrayList();
  protected static Vendor toAdd = Vendor.nullVendor();

  public void initialize() {
    // Set the percentage widths for the columns
    vendorNameColumn.prefWidthProperty().bind(vendorsTableView.widthProperty().subtract(6).multiply(0.25));
    vendorPhoneColumn.prefWidthProperty().bind(vendorsTableView.widthProperty().subtract(6).multiply(0.25));
    vendorAddressColumn.prefWidthProperty().bind(vendorsTableView.widthProperty().subtract(6).multiply(0.25));
    vendorEmailColumn.prefWidthProperty().bind(vendorsTableView.widthProperty().subtract(6).multiply(0.25));

    // Set the cell value factories
    vendorNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    vendorPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
    vendorAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
    vendorEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

    // fetch all vendors
    fetchVendors();
    fetchVendorNames();

    // Set the items in the ListView
    vendorsTableView.setItems(vendorsList);

    // listen to TableView's selection changes
    vendorsTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        selectedKey = newValue.getName();
      } else {
        selectedKey = "";
      }
    });

    addVendorBtn.setOnAction(e -> openAddVendorPopup());
    removeVendorBtn.setOnAction(e -> removeVendor(selectedKey));

  }

  private void openAddVendorPopup() {
    try {
      // Load the addVendor.fxml file
      FXMLLoader loader = new FXMLLoader(getClass().getResource("addVendor.fxml"));
      Parent addVendorRoot = loader.load();

      // Create a new stage for the popup
      Stage addVendorStage = new Stage();
      addVendorStage.initModality(Modality.APPLICATION_MODAL);
      addVendorStage.setTitle("Add New Vendor");
      addVendorStage.setScene(new Scene(addVendorRoot));
      addVendorStage.setResizable(false);

      // Show the popup and wait for it to be closed
      addVendorStage.showAndWait();
      if (!toAdd.isNull())
        addVendor(toAdd.getName(), toAdd);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void addVendor(String name, Vendor vendor) {
    vendors.put(name, vendor.toHashMap());
    vendorsList.removeIf((element) -> element.getName().hashCode() == name.hashCode());
    vendorsList.add(vendor);
    vendorNames.add(name);
    toAdd = Vendor.nullVendor();
  }

  private void removeVendor(String name) {
    vendors.remove(name);
    vendorsList.removeIf((element) -> element.getName() == name);
    vendorNames.remove(name);
  }

  private void fetchVendors() {
    vendorsList = vendors.getVendors();
    // vendorsList.addAll(Vendor.getVendors());
  }

  public void fetchVendorNames() {
    vendorNames = vendors.getVendors(true);
  }
}
