package com.stockmaster;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.beans.property.SimpleStringProperty;
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

public class IssuedGoodsController {
  @FXML
  private TableView<IssuedItem> issuedGoodsTableView;
  @FXML
  private TableColumn<IssuedItem, String> issuedGoodsNameColumn;
  @FXML
  private TableColumn<IssuedItem, Integer> issuedGoodsQuantityColumn;
  @FXML
  private TableColumn<IssuedItem, String> issuedGoodsCategoryColumn;
  @FXML
  private TableColumn<IssuedItem, String> issuedGoodsIssuedToColumn;
  @FXML
  private TableColumn<IssuedItem, LocalDate> issuedGoodsIssuedDateColumn;
  @FXML
  private TableColumn<IssuedItem, String> issuedGoodsSubTotalColumn;
  @FXML
  private Button addIssuedItemBtn;
  @FXML
  private Button removeIssuedItemBtn;

  public static DBList<IssuedItem> issuedGoods = new DBList<>("issued_goods", IssuedItem.class);
  public static ObservableList<IssuedItem> issuedGoodsList = FXCollections.observableArrayList();

  public void initialize() {
    // Set the percentage widths for the columns
    issuedGoodsNameColumn.prefWidthProperty().bind(issuedGoodsTableView.widthProperty().subtract(6).multiply(0.21));
    issuedGoodsQuantityColumn.prefWidthProperty().bind(issuedGoodsTableView.widthProperty().subtract(6).multiply(0.10));
    issuedGoodsCategoryColumn.prefWidthProperty().bind(issuedGoodsTableView.widthProperty().subtract(6).multiply(0.18));
    issuedGoodsIssuedToColumn.prefWidthProperty().bind(issuedGoodsTableView.widthProperty().subtract(6).multiply(0.21));
    issuedGoodsIssuedDateColumn.prefWidthProperty()
        .bind(issuedGoodsTableView.widthProperty().subtract(6).multiply(0.18));
    issuedGoodsSubTotalColumn.prefWidthProperty().bind(issuedGoodsTableView.widthProperty().subtract(6).multiply(0.11));

    // Set the cell value factories
    issuedGoodsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    issuedGoodsQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    issuedGoodsCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
    issuedGoodsIssuedToColumn.setCellValueFactory(new PropertyValueFactory<>("issuedTo"));
    issuedGoodsIssuedDateColumn.setCellValueFactory(new PropertyValueFactory<>("issuedDate"));
    issuedGoodsSubTotalColumn.setCellValueFactory(cellData -> {
      IssuedItem item = cellData.getValue();
      int quantity = item.getQuantity();
      double unitCost = item.getUnitCost();
      double subTotal = quantity * unitCost;
      return new SimpleStringProperty(String.valueOf(subTotal));
    });

    // fetch data
    issuedGoodsList = issuedGoods.getItems();
    issuedGoodsTableView.setItems(issuedGoodsList);

    addIssuedItemBtn.setOnAction(e -> openIssueItemPopup());
    removeIssuedItemBtn.setOnAction(e -> removeIssuedItem());

  }

  private void openIssueItemPopup() {
    try {
      // Load the issueItem.fxml file
      FXMLLoader loader = new FXMLLoader(getClass().getResource("issueItem.fxml"));
      Parent issueItemRoot = loader.load();

      // Create a new stage for the popup
      Stage issueItemStage = new Stage();
      issueItemStage.initModality(Modality.APPLICATION_MODAL);
      issueItemStage.setTitle("Issue Item");
      issueItemStage.setScene(new Scene(issueItemRoot));
      issueItemStage.setResizable(false);

      // Show the popup and wait for it to be closed
      issueItemStage.showAndWait();
      InventoryController.sell(null);
      InventoryController.toIssue = IssuedItem.nullItem();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void removeIssuedItem() {
    IssuedItem item = issuedGoodsTableView.getSelectionModel().getSelectedItem();
    if (item != null) {
      issuedGoodsList.remove(item.getId());
      issuedGoods.remove(item.getId());
    } else {
      issuedGoodsList.remove(0);
      issuedGoods.remove(0);
    }
    updateIndexes();
  }

  // public static void removeIssuedItem(int id) {
  // issuedGoodsList.remove(item.getId());
  // issuedGoods.remove(item.getId());
  // issuedGoodsList.remove(0);
  // issuedGoods.remove(0);
  // updateIndexes();
  // }

  public static void addIssuedItem(IssuedItem item) {
    issuedGoods.add(item);
  }

  private static void updateIndexes() {
    for (int i = 0; i < issuedGoodsList.size(); i++) {
      issuedGoodsList.get(i).setId(i);
    }
  }

  public static IssuedItem getIssuedItemById(int id) {
    return issuedGoods.get(id);
  }

  public static int getSales() {
    int sales = 0;
    for (IssuedItem item : issuedGoodsList) {
      sales += item.getQuantity();
    }
    return sales;
  }

  public static List<Sale> getSalesData() {
    List<Sale> sales = new ArrayList<>();
    Map<Date, Integer> salesMap = new HashMap<>();
    for (IssuedItem item : issuedGoodsList) {
      Integer prevSold = salesMap.get(item.getIssuedDate());
      if (prevSold == null)
        prevSold = 0;
      salesMap.put(item.getIssuedDate(), prevSold + item.getQuantity());
    }
    for (Map.Entry<Date, Integer> entry : salesMap.entrySet()) {
      Sale sale = new Sale(entry.getKey(), entry.getValue());
      sales.add(sale);
    }
    return sales;
  }

  public static double getRevenue() {
    double revenue = 0;
    for (IssuedItem item : issuedGoodsList) {
      revenue += item.getQuantity() * item.getUnitCost();
    }
    return revenue;
  }

  public static int getTotalCustomers() {
    Set<String> customers = new HashSet<>();
    for (IssuedItem item : issuedGoodsList) {
      customers.add(item.getIssuedTo());
    }
    return customers.size();
  }

  public static double getTotalCost() {
    double totalCost = 0;
    for (IssuedItem item : issuedGoodsList) {
      totalCost += item.getQuantity() * InventoryController.searchForItem(item.getName()).getCostPrice();
    }
    return totalCost;
  }
}

class Sale {
  private Date date;
  private int itemsSold;

  public Sale(Date date, int itemsSold) {
    this.date = date;
    this.itemsSold = itemsSold;
  }

  public Date getDate() {
    return date;
  }

  public int getItemsSold() {
    return itemsSold;
  }
}
