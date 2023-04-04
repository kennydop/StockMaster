package com.stockmaster;

import java.util.List;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

public class DashboardController {
  @FXML
  private Label sales;
  @FXML
  private Label sRevenue;
  @FXML
  private Label spent;
  @FXML
  private Label profit;
  @FXML
  private Label purchase;
  @FXML
  private Label returns;
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
  @FXML
  LineChart<String, Number> salesLineChart;

  public void initialize() {

    Platform.runLater(() -> {
      // Create a new series to hold the sales data
      XYChart.Series<String, Number> series = new XYChart.Series<>();
      series.setName("Sales Data");

      // Get the sample sales data
      List<Sale> sales = IssuedGoodsController.getSalesData();

      // Add the sales data to the series
      for (Sale sale : sales) {
        series.getData().add(new XYChart.Data<>(sale.getDate().toString(), sale.getItemsSold()));
      }

      // Add the series to the line chart
      salesLineChart.getData().add(series);

      // Configure the x-axis and y-axis labels, if necessary
      salesLineChart.getXAxis().setLabel("Date");
      salesLineChart.getYAxis().setLabel("Items Sold");
      updateDashboard();
    });

  }

  public void updateDashboard() {
    sales.setText(String.valueOf(IssuedGoodsController.getSales()));
    sRevenue.setText(String.valueOf(IssuedGoodsController.getRevenue()));
    double _amountSpent = InventoryController.getAmountSpent();
    double _totalCost = IssuedGoodsController.getTotalCost();
    spent.setText(String.valueOf(_amountSpent));
    profit.setText(String.valueOf(IssuedGoodsController.getRevenue() - _amountSpent));
    cancelled.setText("0");
    returns.setText(String.valueOf(IssuedGoodsController.getRevenue() - _totalCost));
    pCost.setText(String.valueOf(_totalCost));
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
