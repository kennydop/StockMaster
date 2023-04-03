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
import javafx.scene.layout.BorderPane;

public class LayoutController implements Initializable {
  @FXML
  BorderPane rootPane;
  @FXML
  Button dashboardBtn;
  @FXML
  Button inventoryBtn;
  @FXML
  Button vendorsBtn;
  @FXML
  Button issuedGoodsBtn;
  @FXML
  Button billsBtn;

  private Node currentCenterNode; // the default center node for the dashboard
  private Node dashboardNode;
  private Node inventoryNode;
  private Node vendorsNode;
  private Node issuedGoodsNode;
  private Node billsNode;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    try {
      // load the dashboard node
      FXMLLoader dasboardoader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
      dashboardNode = dasboardoader.load();

      // load the inventory node
      FXMLLoader inventoryLoader = new FXMLLoader(getClass().getResource("inventory.fxml"));
      inventoryNode = inventoryLoader.load();

      // load the vendors node
      FXMLLoader vendorsLoader = new FXMLLoader(getClass().getResource("vendors.fxml"));
      vendorsNode = vendorsLoader.load();

      // load the issueGoods node
      FXMLLoader issuedGoodsLoader = new FXMLLoader(getClass().getResource("issuedGoods.fxml"));
      issuedGoodsNode = issuedGoodsLoader.load();

      // load the issueGoods node
      FXMLLoader billsLoader = new FXMLLoader(getClass().getResource("bills.fxml"));
      billsNode = billsLoader.load();

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

    inventoryBtn.setOnAction(event -> {
      rootPane.setCenter(inventoryNode);
      currentCenterNode = inventoryNode;
      setClicked(inventoryBtn);
    });

    vendorsBtn.setOnAction(event -> {
      rootPane.setCenter(vendorsNode);
      currentCenterNode = vendorsNode;
      setClicked(vendorsBtn);
    });

    issuedGoodsBtn.setOnAction(event -> {
      rootPane.setCenter(issuedGoodsNode);
      currentCenterNode = issuedGoodsNode;
      setClicked(issuedGoodsBtn);
    });

    billsBtn.setOnAction(event -> {
      rootPane.setCenter(billsNode);
      currentCenterNode = billsNode;
      setClicked(billsBtn);
    });

    Platform.runLater(() -> rootPane.requestFocus());
  }

  private void setClicked(Button btn) {
    dashboardBtn.getStyleClass().remove("clicked");
    inventoryBtn.getStyleClass().remove("clicked");
    vendorsBtn.getStyleClass().remove("clicked");
    issuedGoodsBtn.getStyleClass().remove("clicked");
    billsBtn.getStyleClass().remove("clicked");
    btn.getStyleClass().add("clicked");
  }
}
