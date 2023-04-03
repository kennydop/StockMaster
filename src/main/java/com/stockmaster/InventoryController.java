package com.stockmaster;

import java.io.IOException;
import java.util.Date;

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
  private Button issueItemBtn;
  @FXML
  private Button removeItemBtn;
  @FXML
  private ComboBox<String> categoryDropdown;
  @FXML
  private Label dataStructureInUse;

  String selectedCategory = "All";
  public static Item selectedItem = Item.nullItem();
  private static ObservableList<Item> allItems = FXCollections.observableArrayList();
  protected static Item toAdd = Item.nullItem();
  protected static IssuedItem toIssue = IssuedItem.nullItem();

  // Instanciate Stacks
  private DBStack<Item> beverages = new DBStack<Item>(5, "beverages");
  private static ObservableList<Item> beverageItems;
  private DBStack<Item> bakery = new DBStack<Item>(5, "bakery");
  private static ObservableList<Item> bakeryItems;
  private DBStack<Item> canned = new DBStack<Item>(5, "canned");
  private static ObservableList<Item> cannedItems;
  private DBStack<Item> dairy = new DBStack<Item>(5, "dairy");
  private static ObservableList<Item> dairyItems;

  // Instantiate Queues
  private DBQueue<Item> dry = new DBQueue<Item>(5, "dry");
  private static ObservableList<Item> dryItems;
  private DBQueue<Item> frozen = new DBQueue<Item>(5, "frozen");
  private static ObservableList<Item> frozenItems;
  private DBQueue<Item> meat = new DBQueue<Item>(5, "meat");
  private static ObservableList<Item> meatItems;

  // Instanciate Stacks
  private DBList<Item> produce = new DBList<Item>("produce", Item.class);
  private static ObservableList<Item> produceItems;
  private DBList<Item> cleaners = new DBList<Item>("cleaners", Item.class);
  private static ObservableList<Item> cleanersItems;
  private DBList<Item> paper = new DBList<Item>("paper", Item.class);
  private static ObservableList<Item> paperItems;
  private DBList<Item> personal = new DBList<Item>("personal", Item.class);
  private static ObservableList<Item> personalItems;

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
        selectedItem = newValue;
        String category = newValue.getCategory();
        if (selectedCategory == "All")
          return;
        if (category.equals("Produce") || category.equals("Cleaners") || category.equals("Paper Goods")
            || category.equals("Personal Care")) {
          addAfterBtn.setVisible(true);
          addBeforeBtn.setVisible(true);
        } else {
          addAfterBtn.setVisible(false);
          addBeforeBtn.setVisible(false);
        }
      } else {
        // No item is selected
        selectedItem = Item.nullItem();
        addAfterBtn.setVisible(false);
        addBeforeBtn.setVisible(false);
      }
    });

    addItemBtn.setOnAction(e -> openAddItemPopup(-1));
    issueItemBtn.setOnAction(e -> openIssueItemPopup());
    removeItemBtn.setOnAction(e -> removeItem());
    addAfterBtn.setOnAction(e -> openAddItemPopup(selectedItem.getId() + 1));
    addBeforeBtn.setOnAction(e -> openAddItemPopup(selectedItem.getId()));
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

  private void openIssueItemPopup() {
    try {
      // Load the issueItem.fxml file
      FXMLLoader loader = new FXMLLoader(getClass().getResource("issueItem.fxml"));
      Parent issueItemRoot = loader.load();

      IssueItemController issueItemController = loader.getController();
      // System.out.println("selected item: \n" + selectedItem.toString());
      issueItemController.itemAlreadySelected(selectedItem);
      toIssue.setName(selectedItem.getName());
      toIssue.setCategory(selectedItem.getCategory());
      toIssue.setUnitCost(selectedItem.getSellingPrice());

      // Create a new stage for the popup
      Stage issueItemStage = new Stage();
      issueItemStage.initModality(Modality.APPLICATION_MODAL);
      issueItemStage.setTitle("Issue " + selectedItem.getName());
      issueItemStage.setScene(new Scene(issueItemRoot));
      issueItemStage.setResizable(false);

      // Show the popup and wait for it to be closed
      issueItemStage.showAndWait();
      sell();
      toIssue = IssuedItem.nullItem();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void addItem(int index, Item item) {
    System.out.println("adding " + item.getName() + " to " + item.getCategory().toLowerCase().split("/")[0]
        .split(" ")[0]);
    switch (item.getCategory().toLowerCase().split("/")[0].split(" ")[0]) {
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
        System.out.println("running...");
        canned.push(item);
        System.out.println("pushed!");
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
      case "paper":
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
      case "personal":
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
    System.out.println(
        "removing item from " + selectedCategory.toLowerCase().toLowerCase().split("/")[0].split(" ")[0] + "...");
    Item removedItem = null;
    switch (selectedCategory.toLowerCase().toLowerCase().split("/")[0].split(" ")[0]) {
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
        produce.remove(selectedItem.getId());
        removedItem = produceItems.remove(selectedItem.getId());
        updateIndexes(produceItems);
      case "cleaners":
        cleaners.remove(selectedItem.getId());
        removedItem = cleanersItems.remove(selectedItem.getId());
        updateIndexes(cleanersItems);
      case "paper":
        paper.remove(selectedItem.getId());
        removedItem = paperItems.remove(selectedItem.getId());
        updateIndexes(paperItems);
      case "personal":
        personal.remove(selectedItem.getId());
        removedItem = personalItems.remove(selectedItem.getId());
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
    switch (category.toLowerCase().split("/")[0].split(" ")[0]) {
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
      case "paper":
        itemTableView.setItems(paperItems);
        dataStructureInUse.setText("List");
        break;
      case "personal":
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

  public void sell() {
    sell(selectedItem);
  }

  public static void sell(Item selectedItem) {
    if (selectedItem == null || selectedItem.isNull()) {
      selectedItem = searchForItem(toIssue.getName());
    }
    if (selectedItem == null)
      return;
    Item updatedItem = selectedItem;
    updatedItem.setSold((selectedItem.getSold() + toIssue.getQuantity()));
    updatedItem.setQuantity((selectedItem.getQuantity() - toIssue.getQuantity()));
    String table = selectedItem.getCategory().toLowerCase().split("/")[0].split(" ")[0];
    switch (table) {
      case "beverages":
        beverageItems.set(selectedItem.getId(), updatedItem);
        table = "beverages";
        break;
      case "bakery":
        bakeryItems.set(selectedItem.getId(), updatedItem);
        table = "bakery";
        break;
      case "canned":
        cannedItems.set(selectedItem.getId(), updatedItem);
        table = "canned";
        break;
      case "dairy":
        dairyItems.set(selectedItem.getId(), updatedItem);
        table = "dairy";
        break;
      case "dry":
        dryItems.set(selectedItem.getId(), updatedItem);
        table = "dry";
        break;
      case "frozen":
        frozenItems.set(selectedItem.getId(), updatedItem);
        table = "frozen";
        break;
      case "meat":
        meatItems.set(selectedItem.getId(), updatedItem);
        table = "meat";
        break;
      case "produce":
        produceItems.set(selectedItem.getId(), updatedItem);
        table = "produce";
        break;
      case "cleaners":
        cleanersItems.set(selectedItem.getId(), updatedItem);
        table = "cleaners";
        break;
      case "paper":
        paperItems.set(selectedItem.getId(), updatedItem);
        table = "paper";
        break;
      case "personal":
        personalItems.set(selectedItem.getId(), updatedItem);
        table = "personal";
        break;
      default:
        break;
    }
    for (int i = 0; i < allItems.size(); i++) {
      if (allItems.get(i) == selectedItem) {
        allItems.set(i, updatedItem);
        break;
      }
    }
    IssuedGoodsController.issuedGoodsList.add(0, toIssue);
    IssuedGoodsController.issuedGoods.add(0, toIssue);
    BillsController.generateBill(toIssue);

    DBConnection dbConnection = DBConnection.getInstance();
    dbConnection
        .execSQL("UPDATE " + table + " SET sold = " + (selectedItem.getSold() + toIssue.getQuantity()) + ", quantity = "
            + (selectedItem.getQuantity() - toIssue.getQuantity()) + " WHERE id = " + selectedItem.getId());
  }

  public static String searchForItemCategory(String name) {
    for (int i = 0; i < allItems.size(); i++) {
      Item item = allItems.get(i);
      if (item.getName().equals(name))
        return item.getCategory();
    }
    return null;
  }

  public static double searchForItemSellingPrice(String name) {
    for (int i = 0; i < allItems.size(); i++) {
      Item item = allItems.get(i);
      if (item.getName().equals(name))
        return item.getSellingPrice();
    }
    return -1;
  }

  public static int searchForItemQuantity(String name) {
    for (int i = 0; i < allItems.size(); i++) {
      Item item = allItems.get(i);
      if (item.getName().equals(name))
        return item.getQuantity();
    }
    return -1;
  }

  public static Item searchForItem(String name) {
    for (int i = 0; i < allItems.size(); i++) {
      Item item = allItems.get(i);
      if (item.getName().equals(name))
        return item;
    }
    return null;
  }

  public static ObservableList<String> getAllItemNames() {
    ObservableList<String> names = FXCollections.observableArrayList();
    for (int i = 0; i < allItems.size(); i++) {
      names.add(allItems.get(i).getName());
    }
    return names;
  }

  public static int getTotalStock() {
    int total = 0;
    for (int i = 0; i < allItems.size(); i++) {
      total += allItems.get(i).getQuantity();
    }
    return total;
  }

  public static int getLowStockedItem() {
    int total = Integer.MAX_VALUE;
    for (int i = 0; i < allItems.size(); i++) {
      if (allItems.get(i).getQuantity() < total)
        total = allItems.get(i).getQuantity();
    }
    return total;
  }

  public static int getHighStockedItem() {
    int total = 0;
    for (int i = 0; i < allItems.size(); i++) {
      if (allItems.get(i).getQuantity() > total)
        total = allItems.get(i).getQuantity();
    }
    return total;
  }

  public static int getTotalExpiredItems() {
    int total = 0;
    Date currentDate = new Date();
    for (int i = 0; i < allItems.size(); i++) {
      if (allItems.get(i).getExpiryDate().before(currentDate))
        total += allItems.get(i).getQuantity();
    }
    return total;
  }
}
