module com.stockmaster {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.stockmaster to javafx.fxml;
    exports com.stockmaster;
}
