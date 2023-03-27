package com.stockmaster;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LayoutController implements Initializable {
  @FXML
  BorderPane rootPane;
  @FXML
  Button dashboardBtn;
  @FXML
  Button addItemBtn;
  @FXML
  Button inventoryBtn;
  @FXML
  Button addVendorBtn;
  @FXML
  Button vendorsBtn;
  @FXML
  Button addCustomerBtn;
  @FXML
  Button customersBtn;
  @FXML
  Button addPurchaseBtn;
  @FXML
  Button salesBtn;

  private Node currentCenterNode; // the default center node for the dashboard
  private VBox dashboardNode;
  private StackPane addItemNode;
  private Node inventoryNode;
  private Node addVendorNode;
  private Node vendorsNode;
  private Node addCustomerNode;
  private Node customersNode;
  private Node addPurchaseNode;
  private Node salesNode;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    try {
      // load the dashboard node
      FXMLLoader dasboardoader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
      dashboardNode = dasboardoader.load();

      // load the addItem node
      FXMLLoader addItemLoader = new FXMLLoader(getClass().getResource("addItem.fxml"));
      addItemNode = addItemLoader.load();

      // load the inventory node
      FXMLLoader inventoryLoader = new FXMLLoader(getClass().getResource("inventory.fxml"));
      inventoryNode = inventoryLoader.load();

      // load the addVendor node
      FXMLLoader addVendorLoader = new FXMLLoader(getClass().getResource("addVendor.fxml"));
      addVendorNode = addVendorLoader.load();

      // load the vendors node
      FXMLLoader vendorsLoader = new FXMLLoader(getClass().getResource("vendors.fxml"));
      vendorsNode = vendorsLoader.load();

      // load the addCustomer node
      FXMLLoader addCustomerLoader = new FXMLLoader(getClass().getResource("addCustomer.fxml"));
      addCustomerNode = addCustomerLoader.load();

      // load the customers node
      FXMLLoader customersLoader = new FXMLLoader(getClass().getResource("customers.fxml"));
      customersNode = customersLoader.load();

      // // load the addPurchase node
      // FXMLLoader addPurchaseLoader = new
      // FXMLLoader(getClass().getResource("addPurchase.fxml"));
      // addPurchaseNode = addPurchaseLoader.load();

      // // load the sales node
      // FXMLLoader salesLoader = new
      // FXMLLoader(getClass().getResource("sales.fxml"));
      // salesNode = salesLoader.load();

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // set the default center node
    rootPane.setCenter(dashboardNode);
    dashboardBtn.getStyleClass().add("clicked");
    currentCenterNode = dashboardNode;

    // set the center swap and highlight for nav buttons

    dashboardBtn.setOnAction(event -> {
      rootPane.setCenter(dashboardNode);
      currentCenterNode = dashboardNode;
      setClicked(dashboardBtn);
    });

    addItemBtn.setOnAction(event -> {
      rootPane.setCenter(addItemNode);
      currentCenterNode = addItemNode;
      setClicked(addItemBtn);
    });

    inventoryBtn.setOnAction(event -> {
      rootPane.setCenter(inventoryNode);
      currentCenterNode = inventoryNode;
      setClicked(inventoryBtn);
    });

    addVendorBtn.setOnAction(event -> {
      rootPane.setCenter(addVendorNode);
      currentCenterNode = addVendorNode;
      setClicked(addVendorBtn);
    });

    vendorsBtn.setOnAction(event -> {
      rootPane.setCenter(vendorsNode);
      currentCenterNode = vendorsNode;
      setClicked(vendorsBtn);
    });

    addCustomerBtn.setOnAction(event -> {
      rootPane.setCenter(addCustomerNode);
      currentCenterNode = addCustomerNode;
      setClicked(addCustomerBtn);
    });

    customersBtn.setOnAction(event -> {
      rootPane.setCenter(customersNode);
      currentCenterNode = customersNode;
      setClicked(customersBtn);
    });

    // addPurchaseBtn.setOnAction(event -> {
    // rootPane.setCenter(addPurchaseNode);
    // currentCenterNode = addPurchaseNode;
    // setClicked(addPurchaseBtn);
    // });

    // salesBtn.setOnAction(event -> {
    // rootPane.setCenter(salesNode);
    // currentCenterNode = salesNode;
    // setClicked(salesBtn);
    // });

    Platform.runLater(() -> rootPane.requestFocus());
  }

  private void setClicked(Button btn) {
    dashboardBtn.getStyleClass().remove("clicked");
    addItemBtn.getStyleClass().remove("clicked");
    inventoryBtn.getStyleClass().remove("clicked");
    addVendorBtn.getStyleClass().remove("clicked");
    vendorsBtn.getStyleClass().remove("clicked");
    addCustomerBtn.getStyleClass().remove("clicked");
    customersBtn.getStyleClass().remove("clicked");
    addPurchaseBtn.getStyleClass().remove("clicked");
    salesBtn.getStyleClass().remove("clicked");
    btn.getStyleClass().add("clicked");
  }
}
