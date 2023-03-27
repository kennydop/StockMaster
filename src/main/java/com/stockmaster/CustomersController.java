package com.stockmaster;

import com.stockmaster.Customer.CustomerType;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.scene.control.ScrollBar;

public class CustomersController {
    @FXML
    private ListView<Customer> customersListView;
    @FXML
    private Label headerCustomerNameLabel;
    @FXML
    private Label headerCustomerTypeLabel;
    @FXML
    private Label headerCustomerPhoneLabel;
    @FXML
    private Label headerCustomerEmailLabel;
    @FXML
    private Label headerCustomerAddressLabel;

    public void initialize() {
        // Other initialization code

        // Create sample data
        ObservableList<Customer> customers = FXCollections.observableArrayList(
                new Customer("Customer 1", "Ayawaso West", "026132748", "mdpot@coco.co", CustomerType.Wholesale),
                new Customer("Customer 2", "Dzemeni", "0423629234", "opk@toure.xo", CustomerType.Online),
                new Customer("Customer 3", "Behind Lake Bosomtwe", "065526477", "v3@customers.yat", CustomerType.Retail),
                new Customer("Customer 4", "Philadelphia, Kumasi", "0123456789", "driller@yga.ken", CustomerType.Online));

        // Set the items in the ListView
        customersListView.setItems(customers);

        // Set the custom cell factory for the ListView
        customersListView.setCellFactory(new Callback<ListView<Customer>, ListCell<Customer>>() {
            @Override
            public ListCell<Customer> call(ListView<Customer> listView) {
                return new ListCell<Customer>() {
                    // Create labels for each cell
                    Label nameLabel = new Label();
                    Label typeLabel = new Label();
                    Label addressLabel = new Label();
                    Label phonLabel = new Label();
                    Label emailLabel = new Label();

                    // Create the HBox layout for the cell
                    HBox cellLayout = new HBox(nameLabel, typeLabel, addressLabel, phonLabel, emailLabel);

                    {

                        // Find the vertical scrollbar of the ListView
                        ScrollBar scrollBar = (ScrollBar) customersListView.lookup(".scroll-bar:vertical");
                        if (scrollBar == null) {
                            customersListView.applyCss();
                            scrollBar = (ScrollBar) customersListView.lookup(".scroll-bar:vertical");
                        }

                        double scrollBarWidth = scrollBar.getWidth();

                        // Set the preferred width for the header labels
                        headerCustomerNameLabel.prefWidthProperty()
                                .bind(customersListView.widthProperty().subtract(scrollBarWidth).multiply(0.20));
                        headerCustomerTypeLabel.prefWidthProperty()
                                .bind(customersListView.widthProperty().subtract(scrollBarWidth).multiply(0.20));
                        headerCustomerPhoneLabel.prefWidthProperty()
                                .bind(customersListView.widthProperty().subtract(scrollBarWidth).multiply(0.20));
                        headerCustomerEmailLabel.prefWidthProperty()
                                .bind(customersListView.widthProperty().subtract(scrollBarWidth).multiply(0.20));
                        headerCustomerAddressLabel.prefWidthProperty()
                                .bind(customersListView.widthProperty().subtract(scrollBarWidth).multiply(0.20));

                        // Bind the preferred width of each label, accounting for the scrollbar's width
                        nameLabel.prefWidthProperty()
                                .bind(customersListView.widthProperty().subtract(scrollBarWidth).multiply(0.20));
                                typeLabel.prefWidthProperty()
                                .bind(customersListView.widthProperty().subtract(scrollBarWidth).multiply(0.20));
                        addressLabel.prefWidthProperty()
                                .bind(customersListView.widthProperty().subtract(scrollBarWidth).multiply(0.20));
                        phonLabel.prefWidthProperty()
                                .bind(customersListView.widthProperty().subtract(scrollBarWidth).multiply(0.20));
                        emailLabel.prefWidthProperty()
                                .bind(customersListView.widthProperty().subtract(scrollBarWidth).multiply(0.20));

                        // Set the cell layout style
                        cellLayout.getStyleClass().add("custom-list-cell");

                    }

                    @Override
                    protected void updateItem(Customer customer, boolean empty) {
                        super.updateItem(customer, empty);
                        if (customer == null || empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            nameLabel.setText(customer.getName());
                            typeLabel.setText(customer.getTypeAString());
                            phonLabel.setText(customer.getPhone());
                            addressLabel.setText(customer.getAddress());
                            emailLabel.setText(customer.getEmail());

                            setGraphic(cellLayout);
                        }
                    }
                };
            }
        });

    }

}
