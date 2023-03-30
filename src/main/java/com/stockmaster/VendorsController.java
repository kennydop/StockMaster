package com.stockmaster;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.scene.control.ScrollBar;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VendorsController {
    @FXML
    private ListView<Vendor> vendorsListView;
    @FXML
    private Label headerVendorNameLabel;
    @FXML
    private Label headerVendorPhoneLabel;
    @FXML
    private Label headerVendorEmailLabel;
    @FXML
    private Label headerVendorAddressLabel;
    @FXML
    private Button addVendorBtn;
    @FXML
    private Button removeVendorBtn;


    public void initialize() {
        // Other initialization code

        // Create sample data
        ObservableList<Vendor> vendors = FXCollections.observableArrayList(
                new Vendor("Vendor 1", "Ayawaso West", "026132748", "mdpot@coco.co"),
                new Vendor("Vendor 2", "Dzemeni", "0423629234", "opk@toure.xo"),
                new Vendor("Vendor 3", "Behind Lake Bosomtwe", "065526477", "v3@vendors.yat"),
                new Vendor("Vendor 4", "Philadelphia, Kumasi", "0123456789", "driller@yga.ken"));

        // Set the items in the ListView
        vendorsListView.setItems(vendors);

        // Set the custom cell factory for the ListView
        vendorsListView.setCellFactory(new Callback<ListView<Vendor>, ListCell<Vendor>>() {
            @Override
            public ListCell<Vendor> call(ListView<Vendor> listView) {
                return new ListCell<Vendor>() {
                    // Create labels for each cell
                    Label nameLabel = new Label();
                    Label addressLabel = new Label();
                    Label phonLabel = new Label();
                    Label emailLabel = new Label();

                    // Create the HBox layout for the cell
                    HBox cellLayout = new HBox(nameLabel, addressLabel, phonLabel, emailLabel);

                    {

                        // Find the vertical scrollbar of the ListView
                        ScrollBar scrollBar = (ScrollBar) vendorsListView.lookup(".scroll-bar:vertical");
                        if (scrollBar == null) {
                            vendorsListView.applyCss();
                            scrollBar = (ScrollBar) vendorsListView.lookup(".scroll-bar:vertical");
                        }

                        double scrollBarWidth = scrollBar.getWidth();

                        // Set the preferred width for the header labels
                        headerVendorNameLabel.prefWidthProperty()
                                .bind(vendorsListView.widthProperty().subtract(scrollBarWidth).multiply(0.25));
                        headerVendorPhoneLabel.prefWidthProperty()
                                .bind(vendorsListView.widthProperty().subtract(scrollBarWidth).multiply(0.25));
                        headerVendorEmailLabel.prefWidthProperty()
                                .bind(vendorsListView.widthProperty().subtract(scrollBarWidth).multiply(0.25));
                        headerVendorAddressLabel.prefWidthProperty()
                                .bind(vendorsListView.widthProperty().subtract(scrollBarWidth).multiply(0.25));

                        // Bind the preferred width of each label, accounting for the scrollbar's width
                        nameLabel.prefWidthProperty()
                                .bind(vendorsListView.widthProperty().subtract(scrollBarWidth).multiply(0.25));
                        addressLabel.prefWidthProperty()
                                .bind(vendorsListView.widthProperty().subtract(scrollBarWidth).multiply(0.25));
                        phonLabel.prefWidthProperty()
                                .bind(vendorsListView.widthProperty().subtract(scrollBarWidth).multiply(0.25));
                        emailLabel.prefWidthProperty()
                                .bind(vendorsListView.widthProperty().subtract(scrollBarWidth).multiply(0.25));

                        // Set the cell layout style
                        cellLayout.getStyleClass().add("custom-list-cell");

                    }

                    @Override
                    protected void updateItem(Vendor vendor, boolean empty) {
                        super.updateItem(vendor, empty);
                        if (vendor == null || empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            nameLabel.setText(vendor.getName());
                            phonLabel.setText(vendor.getPhone());
                            addressLabel.setText(vendor.getAddress());
                            emailLabel.setText(vendor.getEmail());

                            setGraphic(cellLayout);
                        }
                    }
                };
            }
        });

        addVendorBtn.setOnAction(e -> openAddVendorPopup());

    }

    private void openAddVendorPopup() {
        try {
            // Load the addVendor.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addVendor.fxml"));
            Parent addVendorRoot = loader.load();
    
            // Create a new stage for the popup
            Stage addVendorStage = new Stage();
            addVendorStage.initModality(Modality.APPLICATION_MODAL);
            addVendorStage.setTitle("Add New Item");
            addVendorStage.setScene(new Scene(addVendorRoot));
            addVendorStage.setResizable(false);
    
            // Show the popup and wait for it to be closed
            addVendorStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
