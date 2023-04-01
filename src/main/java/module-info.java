module com.stockmaster {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.stockmaster to javafx.fxml;
    exports com.stockmaster;
}
