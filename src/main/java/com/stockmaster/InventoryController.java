package com.stockmaster;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
  private Button addItemBtn;
  @FXML
  private Button addAfterBtn;
  @FXML
  private Button addBeforeBtn;
  @FXML
  private Button addPurchaseBtn;
  @FXML
  private Button removeItemBtn;
  @FXML
  private ComboBox<String> categoryDropdown;
  @FXML
  private Label dataStructureInUse;

  String selectedCategory = "All";
  private int selectedListIndex = -1;
  ObservableList<Item> allItems = FXCollections.observableArrayList();
  protected static Item toAdd = Item.nullItem();

  // Instanciate Stacks
  private DBStack<Item> beverages = new DBStack<Item>(5, "beverages");
  private ObservableList<Item> beverageItems;
  private DBStack<Item> bakery = new DBStack<Item>(5, "bakery");
  private ObservableList<Item> bakeryItems;
  private DBStack<Item> canned = new DBStack<Item>(5, "canned");
  private ObservableList<Item> cannedItems;
  private DBStack<Item> dairy = new DBStack<Item>(5, "dairy");
  private ObservableList<Item> dairyItems;

  // Instantiate Queues
  private DBQueue<Item> dry = new DBQueue<Item>(5, "dry");
  private ObservableList<Item> dryItems;
  private DBQueue<Item> frozen = new DBQueue<Item>(5, "frozen");
  private ObservableList<Item> frozenItems;
  private DBQueue<Item> meat = new DBQueue<Item>(5, "meat");
  private ObservableList<Item> meatItems;

  // Instanciate Stacks
  private DBList<Item> produce = new DBList<Item>("produce");
  private ObservableList<Item> produceItems;
  private DBList<Item> cleaners = new DBList<Item>("cleaners");
  private ObservableList<Item> cleanersItems;
  private DBList<Item> paper = new DBList<Item>("paper");
  private ObservableList<Item> paperItems;
  private DBList<Item> personal = new DBList<Item>("personal");
  private ObservableList<Item> personalItems;

  public void initialize() {
    // Hide list buttons initially
    addAfterBtn.setVisible(false);
    addBeforeBtn.setVisible(false);

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

    // listen to TableView's selection changes
    itemTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        // Check the category of the selected item
        String category = newValue.getCategory();
        if (category.equals("Produce") || category.equals("Cleaners") || category.equals("Paper Goods")
            || category.equals("Personal Care")) {
          selectedListIndex = newValue.getId();
          addAfterBtn.setVisible(true);
          addBeforeBtn.setVisible(true);
        } else {
          addAfterBtn.setVisible(false);
          addBeforeBtn.setVisible(false);
        }
      } else {
        // No item is selected, hide the button
        selectedListIndex = -1;
        addAfterBtn.setVisible(false);
        addBeforeBtn.setVisible(false);
      }
    });

    addItemBtn.setOnAction(e -> openAddItemPopup(-1));
    addPurchaseBtn.setOnAction(e -> openAddPurchasePopup());
    removeItemBtn.setOnAction(e -> removeItem());
    addAfterBtn.setOnAction(e -> openAddItemPopup(selectedListIndex + 1));
    addBeforeBtn.setOnAction(e -> openAddItemPopup(selectedListIndex));
  }

  private void openAddItemPopup(int index) {
    try {
      // Load the addItem.fxml file
      FXMLLoader loader = new FXMLLoader(getClass().getResource("addItem.fxml"));
      Parent addItemRoot = loader.load();

      // Get the controller for addItem.fxml and set the selected category
      AddItemController addItemController = loader.getController();
      addItemController.setSelectedCategory(selectedCategory);

      // Create a new stage for the popup
      Stage addItemStage = new Stage();
      addItemStage.initModality(Modality.APPLICATION_MODAL);
      addItemStage.setTitle("Add New Item");
      addItemStage.setScene(new Scene(addItemRoot));
      addItemStage.setResizable(false);

      // Show the popup and wait for it to be closed
      addItemStage.showAndWait();
      if (!toAdd.isNull())
        addItem(index, toAdd);
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

  private void addItem(int index, Item item) {
    System.out.println("adding " + item.getName() + " to " + item.getCategory().toLowerCase().split("/")[0]);
    switch (item.getCategory().toLowerCase().split("/")[0]) {
      case "beverages":
        beverages.push(item);
        beverageItems.add(0, item);
        allItems.add(0, item);
        updateIndexes(beverageItems);
        break;
      case "bakery":
        bakery.push(item);
        bakeryItems.add(0, item);
        allItems.add(0, item);
        updateIndexes(bakeryItems);
        break;
      case "canned":
        canned.push(item);
        cannedItems.add(0, item);
        allItems.add(0, item);
        updateIndexes(cannedItems);
        break;
      case "dairy":
        dairy.push(item);
        dairyItems.add(0, item);
        allItems.add(0, item);
        updateIndexes(dairyItems);
        break;
      case "dry":
        dry.enqueue(item);
        dryItems.add(item);
        allItems.add(item);
        updateIndexes(dryItems);
        break;
      case "frozen":
        frozen.enqueue(item);
        frozenItems.add(item);
        allItems.add(item);
        updateIndexes(frozenItems);
        break;
      case "meat":
        meat.enqueue(item);
        meatItems.add(item);
        allItems.add(item);
        updateIndexes(meatItems);
        break;
      case "produce":
        if (index == -1) {
          produce.add(item);
          produceItems.add(item);
          allItems.add(item);
          updateIndexes(produceItems);
        } else {
          produce.add(index, item);
          produceItems.add(index, item);
          allItems.add(index, item);
          updateIndexes(produceItems);
        }
        break;
      case "cleaners":
        if (index == -1) {
          cleaners.add(item);
          cleanersItems.add(item);
          allItems.add(item);
          updateIndexes(cleanersItems);
        } else {
          cleaners.add(index, item);
          cleanersItems.add(index, item);
          allItems.add(index, item);
          updateIndexes(cleanersItems);
        }
        break;
      case "paper goods":
        if (index == -1) {
          paper.add(item);
          paperItems.add(item);
          allItems.add(item);
          updateIndexes(paperItems);
        } else {
          paper.add(index, item);
          paperItems.add(index, item);
          allItems.add(index, item);
          updateIndexes(paperItems);
        }
        break;
      case "personal care":
        if (index == -1) {
          personal.add(item);
          personalItems.add(item);
          allItems.add(item);
          updateIndexes(personalItems);
        } else {
          personal.add(index, item);
          personalItems.add(index, item);
          allItems.add(index, item);
          updateIndexes(personalItems);
        }
        break;
      default:
        System.out.println("Category not supported");
        break;
    }
    allItems.sorted();
    toAdd = Item.nullItem();
  }

  private void removeItem() {
    if (selectedCategory == "All" || allItems.size() < 1)
      return;
    System.out.println("removing item from " + selectedCategory.toLowerCase().toLowerCase().split("/")[0] + "...");
    Item removedItem = null;
    switch (selectedCategory.toLowerCase().toLowerCase().split("/")[0]) {
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
      case "dry":
        dry.dequeue();
        removedItem = dryItems.remove(0);
        break;
      case "frozen":
        frozen.dequeue();
        removedItem = frozenItems.remove(0);
        break;
      case "meat":
        meat.dequeue();
        removedItem = meatItems.remove(0);
      case "produce":
        produce.remove(selectedListIndex);
        removedItem = produceItems.remove(selectedListIndex);
        updateIndexes(produceItems);
      case "cleaners":
        cleaners.remove(selectedListIndex);
        removedItem = cleanersItems.remove(selectedListIndex);
        updateIndexes(cleanersItems);
      case "paper goods":
        paper.remove(selectedListIndex);
        removedItem = paperItems.remove(selectedListIndex);
        updateIndexes(paperItems);
      case "personal care":
        personal.remove(selectedListIndex);
        removedItem = personalItems.remove(selectedListIndex);
        updateIndexes(personalItems);
        break;
      default:
        break;
    }
    if (removedItem != null)
      allItems.remove(removedItem);
  }

  private void fetchData() {
    beverageItems = beverages.getItems();
    bakeryItems = bakery.getItems();
    cannedItems = canned.getItems();
    dairyItems = dairy.getItems();
    dryItems = dry.getItems();
    frozenItems = frozen.getItems();
    meatItems = meat.getItems();
    produceItems = produce.getItems();
    cleanersItems = cleaners.getItems();
    paperItems = paper.getItems();
    personalItems = personal.getItems();
    allItems.addAll(beverageItems);
    allItems.addAll(bakeryItems);
    allItems.addAll(cannedItems);
    allItems.addAll(dairyItems);
    allItems.addAll(dryItems);
    allItems.addAll(frozenItems);
    allItems.addAll(meatItems);
    allItems.addAll(produceItems);
    allItems.addAll(cleanersItems);
    allItems.addAll(paperItems);
    allItems.addAll(personalItems);
  }

  private void handleCategoryChange(String category) {
    selectedCategory = category;
    switch (category.toLowerCase().split("/")[0]) {
      case "beverages":
        itemTableView.setItems(beverageItems);
        dataStructureInUse.setText("Stack");
        break;
      case "bakery":
        itemTableView.setItems(bakeryItems);
        dataStructureInUse.setText("Stack");
        break;
      case "canned":
        itemTableView.setItems(cannedItems);
        dataStructureInUse.setText("Stack");
        break;
      case "dairy":
        itemTableView.setItems(dairyItems);
        dataStructureInUse.setText("Stack");
        break;
      case "dry":
        itemTableView.setItems(dryItems);
        dataStructureInUse.setText("Queue");
        break;
      case "frozen":
        itemTableView.setItems(frozenItems);
        dataStructureInUse.setText("Queue");
        break;
      case "meat":
        itemTableView.setItems(meatItems);
        dataStructureInUse.setText("Queue");
        break;
      case "produce":
        itemTableView.setItems(produceItems);
        dataStructureInUse.setText("List");
        break;
      case "cleaners":
        itemTableView.setItems(cleanersItems);
        dataStructureInUse.setText("List");
        break;
      case "paper goods":
        itemTableView.setItems(paperItems);
        dataStructureInUse.setText("List");
        break;
      case "personal care":
        itemTableView.setItems(personalItems);
        dataStructureInUse.setText("List");
        break;
      case "all":
        itemTableView.setItems(allItems);
        dataStructureInUse.setText("Multiple");
        break;
      default:
        itemTableView.setItems(null);
        dataStructureInUse.setText("Unknown");
        break;
    }
  }

  private void updateIndexes(ObservableList<Item> items) {
    for (int i = 0; i < items.size(); i++) {
      items.get(i).setId(i);
    }
  }
}
