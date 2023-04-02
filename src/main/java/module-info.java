module com.stockmaster {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.sql;
  requires com.google.gson;

  opens com.stockmaster to javafx.fxml;

  exports com.stockmaster;
}
