package com.stockmaster;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {
  @FXML
  private Label sales;
  @FXML
  private Label sRevenue;
  @FXML
  private Label sCost;
  @FXML
  private Label profit;
  @FXML
  private Label purchase;
  @FXML
  private Label pRevenue;
  @FXML
  private Label pCost;
  @FXML
  private Label cancelled;
  @FXML
  private Label totalStock;
  @FXML
  private Label hstockItem;
  @FXML
  private Label lstockItem;
  @FXML
  private Label expired;
  @FXML
  private Label totalVendor;
  @FXML
  private Label totalCustomers;
  @FXML
  private Label totalSuppliers;

  public void initialize() {
    Platform.runLater(() -> {
      updateDashboard();
    });

  }

  public void updateDashboard() {
    sales.setText(String.valueOf(IssuedGoodsController.getSales()));
    sRevenue.setText(String.valueOf(IssuedGoodsController.getRevenue()));
    sCost.setText("0");
    profit.setText("0");
    purchase.setText(String.valueOf(IssuedGoodsController.getSales()));
    sRevenue.setText(String.valueOf(IssuedGoodsController.getRevenue()));
    totalStock.setText(String.valueOf(InventoryController.getTotalStock()));
    hstockItem.setText(String.valueOf(InventoryController.getHighStockedItem()));
    lstockItem.setText(String.valueOf(InventoryController.getLowStockedItem()));
    expired.setText(String.valueOf(InventoryController.getTotalExpiredItems()));
    totalVendor.setText(String.valueOf(VendorsController.getTotalVendors()));
    totalCustomers.setText(String.valueOf(IssuedGoodsController.getTotalCustomers()));
    totalSuppliers.setText(String.valueOf(VendorsController.getTotalVendors()));
  }
}
