package com.stockmaster;

import java.io.IOException;

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

    // Create sample data
    ObservableList<Vendor> vendors = FXCollections.observableArrayList(
        new Vendor("Vendor 1", "Ayawaso West", "026132748", "mdpot@coco.co"),
        new Vendor("Vendor 2", "Dzemeni", "0423629234", "opk@toure.xo"),
        new Vendor("Vendor 3", "Behind Lake Bosomtwe", "065526477", "v3@vendors.yat"),
        new Vendor("Vendor 4", "Philadelphia, Kumasi", "0123456789", "driller@yga.ken"));

    // Set the items in the ListView
    vendorsTableView.setItems(vendors);

    addVendorBtn.setOnAction(e -> openAddVendorPopup());

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
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
