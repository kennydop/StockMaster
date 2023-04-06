package com.stockmaster;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class BillsController {
  @FXML
  private TableView<Bill> billsTableView;
  @FXML
  private TableColumn<Bill, Integer> invoiceNumberColumn;
  @FXML
  private TableColumn<Bill, String> issuedToColumn;
  @FXML
  private TableColumn<Bill, Double> totalAmountColumn;
  @FXML
  private TableColumn<Bill, LocalDate> issuedDateColumn;
  @FXML
  private Button removeBillBtn;

  public static DBList<Bill> bills = new DBList<>("bills", Bill.class);
  public static ObservableList<Bill> billsList = FXCollections.observableArrayList();

  public void initialize() {
    // Set the percentage widths for the columns
    invoiceNumberColumn.prefWidthProperty().bind(billsTableView.widthProperty().subtract(6).multiply(0.25));
    issuedToColumn.prefWidthProperty().bind(billsTableView.widthProperty().subtract(6).multiply(0.25));
    totalAmountColumn.prefWidthProperty().bind(billsTableView.widthProperty().subtract(6).multiply(0.25));
    issuedDateColumn.prefWidthProperty().bind(billsTableView.widthProperty().subtract(6).multiply(0.25));

    invoiceNumberColumn.setCellValueFactory(new PropertyValueFactory<>("invoiceNumber"));
    issuedToColumn.setCellValueFactory(new PropertyValueFactory<>("issuedTo"));
    totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amountPayed"));
    issuedDateColumn.setCellValueFactory(new PropertyValueFactory<>("issuedDate"));

    // fetch data
    billsList = bills.getItems();
    billsTableView.setItems(billsList);

    removeBillBtn.setOnAction(e -> removeBill());

  }

  private void removeBill() {
    Bill item = billsTableView.getSelectionModel().getSelectedItem();
    if (item != null) {
      billsList.remove(item.getId());
      bills.remove(item.getId());
    } else {
      billsList.remove(0);
      bills.remove(0);
    }
    updateIndexes();
  }

  // Other fields and methods ...

  public static Bill generateBill(IssuedItem issuedItem) {
    String uniqueInvoiceNumber = UniqueRandomCodeGenerator.generateUniqueRandomCode();
    Bill bill = new Bill(-1, uniqueInvoiceNumber, issuedItem.getIssuedTo(), issuedItem.getIssuedDate(), issuedItem
        .getUnitCost() * issuedItem.getQuantity());
    billsList.add(0, bill);
    bills.add(0, bill);
    return bill;
  }

  private void updateIndexes() {
    for (int i = 0; i < billsList.size(); i++) {
      billsList.get(i).setId(i);
    }
  }
}
