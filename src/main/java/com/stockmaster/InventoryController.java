package com.stockmaster;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;

public class InventoryController {
  @FXML
  private ListView<Item> itemListView;

  @FXML
  private Label headerNameLabel;
  @FXML
  private Label headerQuantityLabel;
  @FXML
  private Label headerSellingPriceLabel;
  @FXML
  private Label headerVendorLabel;

  public void initialize() {
    // Other initialization code

    // Create sample data
    ObservableList<Item> items = FXCollections.observableArrayList(
        new Item("Item 1", 5, 10.99, "Vendor A"),
        new Item("Item 2", 10, 15.99, "Vendor B"),
        new Item("Item 3", 7, 8.99, "Vendor A"));

    // Set the items in the ListView
    itemListView.setItems(items);

    // Set the custom cell factory for the ListView
    itemListView.setCellFactory(new Callback<ListView<Item>, ListCell<Item>>() {
      @Override
      public ListCell<Item> call(ListView<Item> listView) {
        return new ListCell<Item>() {
          // Create labels for each cell
          Label nameLabel = new Label();
          Label quantityLabel = new Label();
          Label sellingPriceLabel = new Label();
          Label vendorLabel = new Label();

          // Create the HBox layout for the cell
          HBox cellLayout = new HBox(nameLabel, quantityLabel, sellingPriceLabel, vendorLabel);

          {

            // Find the vertical scrollbar of the ListView
            ScrollBar scrollBar = (ScrollBar) itemListView.lookup(".scroll-bar:vertical");
            if (scrollBar == null) {
              itemListView.applyCss();
              scrollBar = (ScrollBar) itemListView.lookup(".scroll-bar:vertical");
            }

            double scrollBarWidth = scrollBar.getWidth();

            // Set the preferred width for the header labels
            headerNameLabel.prefWidthProperty()
                .bind(itemListView.widthProperty().subtract(scrollBarWidth).multiply(0.35));
            headerQuantityLabel.prefWidthProperty()
                .bind(itemListView.widthProperty().subtract(scrollBarWidth).multiply(0.18));
            headerSellingPriceLabel.prefWidthProperty()
                .bind(itemListView.widthProperty().subtract(scrollBarWidth).multiply(0.18));
            headerVendorLabel.prefWidthProperty()
                .bind(itemListView.widthProperty().subtract(scrollBarWidth).multiply(0.29));

            // Bind the preferred width of each label, accounting for the scrollbar's width
            nameLabel.prefWidthProperty().bind(itemListView.widthProperty().subtract(scrollBarWidth).multiply(0.35));
            quantityLabel.prefWidthProperty()
                .bind(itemListView.widthProperty().subtract(scrollBarWidth).multiply(0.18));
            sellingPriceLabel.prefWidthProperty()
                .bind(itemListView.widthProperty().subtract(scrollBarWidth).multiply(0.18));
            vendorLabel.prefWidthProperty().bind(itemListView.widthProperty().subtract(scrollBarWidth).multiply(0.29));

            // Set the cell layout style
            cellLayout.getStyleClass().add("custom-list-cell");

          }

          @Override
          protected void updateItem(Item item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
              setText(null);
              setGraphic(null);
            } else {
              nameLabel.setText(item.getName());
              quantityLabel.setText(String.valueOf(item.getQuantity()));
              sellingPriceLabel.setText("$" + String.format("%.2f", item.getSellingPrice()));
              vendorLabel.setText(item.getVendor());

              setGraphic(cellLayout);
            }
          }
        };
      }
    });

  }

}
