package com.stockmaster;

import javafx.beans.value.ChangeListener;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SearchController {
  @FXML
  private TableView<Item> itemTableView;
  @FXML
  private TableColumn<Item, String> nameColumn;
  @FXML
  private TableColumn<Item, String> categoryColumn;
  @FXML
  private TableColumn<Item, Integer> quantityColumn;
  @FXML
  private TableColumn<Item, Integer> soldColumn;
  @FXML
  private TableColumn<Item, Double> sellingPriceColumn;
  @FXML
  private TableColumn<Item, String> vendorColumn;
  @FXML
  private SharedSearchModel sharedModel;

  private static ObservableList<Item> searchRes;

  public SearchController() {
    this.sharedModel = SharedSearchModel.getInstance();
  }

  @FXML
  private void initialize() {
    // Set the percentage widths for the columns
    nameColumn.prefWidthProperty().bind(itemTableView.widthProperty().subtract(6).multiply(0.30));
    categoryColumn.prefWidthProperty().bind(itemTableView.widthProperty().subtract(6).multiply(0.20));
    quantityColumn.prefWidthProperty().bind(itemTableView.widthProperty().subtract(6).multiply(0.10));
    soldColumn.prefWidthProperty().bind(itemTableView.widthProperty().subtract(6).multiply(0.098));
    sellingPriceColumn.prefWidthProperty().bind(itemTableView.widthProperty().subtract(6).multiply(0.10));
    vendorColumn.prefWidthProperty().bind(itemTableView.widthProperty().subtract(6).multiply(0.20));

    // Set the cell value factories
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
    quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    soldColumn.setCellValueFactory(new PropertyValueFactory<>("sold"));
    sellingPriceColumn.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
    vendorColumn.setCellValueFactory(new PropertyValueFactory<>("vendor"));

    // Set the items in the TableView
    itemTableView.setItems(searchRes);

    sharedModel.searchQueryProperty().addListener((ChangeListener<? super String>) new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        ObservableList<Item> items = sharedModel.getItems();
        ObservableList<Item> filteredItems = InventoryController.searchItemsByName(newValue);
        itemTableView.setItems(filteredItems);
      }
    });
  }

  public static void setSearchRes(ObservableList<Item> newRes) {
    System.out.println(newRes);
    searchRes = newRes;
  }

}
