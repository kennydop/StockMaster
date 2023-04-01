package com.stockmaster;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class InventoryController {
  @FXML
  private TableView<Item> itemTableView;
  @FXML
  private TableColumn<Item, String> nameColumn;
  @FXML
  private TableColumn<Item, Integer> quantityColumn;
  @FXML
  private TableColumn<Item, Integer> soldColumn;
  @FXML
  private TableColumn<Item, Double> sellingPriceColumn;
  @FXML
  private TableColumn<Item, String> vendorColumn;
  @FXML
  private Button addItemBtn;
  @FXML
  private Button addPurchaseBtn;
  @FXML
  private Button removeItemBtn;
  @FXML
  private ComboBox<String> categoryDropdown;

  String selectedCategory = "All";
  ObservableList<Item> allItems = FXCollections.observableArrayList();
  protected static Item toAdd = new Item(-1, null, null, -1, -1, -1, null, null, 0, null, null);

  DBStack<Item> beverages = new DBStack<Item>(5, "beverages");
  ObservableList<Item> beverageItems;
  DBStack<Item> bakery = new DBStack<Item>(5, "bakery");
  ObservableList<Item> bakeryItems;
  DBStack<Item> canned = new DBStack<Item>(5, "canned");
  ObservableList<Item> cannedItems;
  DBStack<Item> dairy = new DBStack<Item>(5, "dairy");
  ObservableList<Item> dairyItems;

  public void initialize() {

    // Set the percentage widths for the columns
    nameColumn.prefWidthProperty().bind(itemTableView.widthProperty().subtract(6).multiply(0.38));
    quantityColumn.prefWidthProperty().bind(itemTableView.widthProperty().subtract(6).multiply(0.12));
    soldColumn.prefWidthProperty().bind(itemTableView.widthProperty().subtract(6).multiply(0.12));
    sellingPriceColumn.prefWidthProperty().bind(itemTableView.widthProperty().subtract(6).multiply(0.12));
    vendorColumn.prefWidthProperty().bind(itemTableView.widthProperty().subtract(6).multiply(0.26));

    // Set the cell value factories
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    soldColumn.setCellValueFactory(new PropertyValueFactory<>("sold"));
    sellingPriceColumn.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
    vendorColumn.setCellValueFactory(new PropertyValueFactory<>("vendor"));

    // fetch data
    fetchData();

    // set the default category and add a listener
    categoryDropdown.setValue("All");
    categoryDropdown.valueProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        handleCategoryChange(newValue);
      }
    });

    // Set the items in the TableView
    itemTableView.setItems(allItems);

    addItemBtn.setOnAction(e -> openAddItemPopup());
    addPurchaseBtn.setOnAction(e -> openAddPurchasePopup());
    removeItemBtn.setOnAction(e -> removeItem());
  }

  private void openAddItemPopup() {
    try {
      // Load the addItem.fxml file
      FXMLLoader loader = new FXMLLoader(getClass().getResource("addItem.fxml"));
      Parent addItemRoot = loader.load();

      // Create a new stage for the popup
      Stage addItemStage = new Stage();
      addItemStage.initModality(Modality.APPLICATION_MODAL);
      addItemStage.setTitle("Add New Item");
      addItemStage.setScene(new Scene(addItemRoot));
      addItemStage.setResizable(false);

      // Show the popup and wait for it to be closed
      addItemStage.showAndWait();
      addToDB(toAdd);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void openAddPurchasePopup() {
    try {
      // Load the addItem.fxml file
      FXMLLoader loader = new FXMLLoader(getClass().getResource("addPurchase.fxml"));
      Parent addItemRoot = loader.load();

      // Create a new stage for the popup
      Stage addItemStage = new Stage();
      addItemStage.initModality(Modality.APPLICATION_MODAL);
      addItemStage.setTitle("Add Purchase");
      addItemStage.setScene(new Scene(addItemRoot));
      addItemStage.setResizable(false);

      // Show the popup and wait for it to be closed
      addItemStage.showAndWait();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void addToDB(Item item) {
    System.out.println("adding " + item.getName() + " to " + item.getCategory().toLowerCase().split("/")[0]);
    switch (item.getCategory().toLowerCase().split("/")[0]) {
      case "beverages":
        beverages.push(item);
        beverageItems.add(0, item);
        allItems.add(0, item);
        break;
      case "bakery":
        bakery.push(item);
        bakeryItems.add(0, item);
        allItems.add(0, item);
        break;
      case "canned":
        canned.push(item);
        cannedItems.add(0, item);
        allItems.add(0, item);
        break;
      case "dairy":
        dairy.push(item);
        dairyItems.add(0, item);
        allItems.add(0, item);
        break;
      default:
        System.out.println("Category not supported");
        break;
    }
    allItems.sorted();
    resetToAdd();
  }

  private void removeItem() {
    if (allItems.size() < 1)
      return;
    System.out.println("removing item");
    Item removedItem = null;
    switch (selectedCategory.toLowerCase()) {
      case "beverages":
        beverages.pop();
        removedItem = beverageItems.remove(0);
        break;
      case "bakery":
        bakery.pop();
        removedItem = bakeryItems.remove(0);
        break;
      case "canned":
        canned.pop();
        removedItem = cannedItems.remove(0);
        break;
      case "dairy":
        dairy.pop();
        removedItem = dairyItems.remove(0);
        break;
      default:
        break;
    }
    if (removedItem != null)
      allItems.remove(removedItem);
  }

  private void resetToAdd() {
    toAdd = new Item(-1, null, null, -1, -1, -1, null, null, 0, null, null);
  }

  private void fetchData() {
    beverageItems = beverages.getItems();
    bakeryItems = bakery.getItems();
    cannedItems = canned.getItems();
    dairyItems = dairy.getItems();
    allItems.addAll(beverageItems);
    allItems.addAll(bakeryItems);
    allItems.addAll(cannedItems);
    allItems.addAll(dairyItems);
    allItems.sorted();
  }

  private void handleCategoryChange(String category) {
    selectedCategory = category;
    switch (category.toLowerCase().split("/")[0]) {
      case "beverages":
        itemTableView.setItems(beverageItems);
        break;
      case "bakery":
        itemTableView.setItems(bakeryItems);
        break;
      case "canned":
        itemTableView.setItems(cannedItems);
        break;
      case "dairy":
        itemTableView.setItems(dairyItems);
        break;
      case "all":
        itemTableView.setItems(allItems);
        break;
      default:
        itemTableView.setItems(null);
        break;
    }
  }
}
