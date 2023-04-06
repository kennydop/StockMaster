package com.stockmaster;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SharedSearchModel {
  private final ObservableList<Item> items = FXCollections.observableArrayList();
  private final StringProperty searchQuery = new SimpleStringProperty("");

  private static SharedSearchModel instance;

  private SharedSearchModel() {
    // Initialize properties and other fields
  }

  public static SharedSearchModel getInstance() {
    if (instance == null) {
      instance = new SharedSearchModel();
    }
    return instance;
  }

  public ObservableList<Item> getItems() {
    return items;
  }

  public String getSearchQuery() {
    return searchQuery.get();
  }

  public void setSearchQuery(String searchQuery) {
    this.searchQuery.set(searchQuery);
  }

  public StringProperty searchQueryProperty() {
    return searchQuery;
  }
}
