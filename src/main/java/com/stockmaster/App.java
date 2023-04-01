package com.stockmaster;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * JavaFX App
 */
public class App extends Application {

  private static Scene scene;
  private Connection connection;

  @Override
  public void init() {
    DBConnection dbConnection = new DBConnection();
    connection = dbConnection.setupDatabaseConnection();
  }

  @Override
  public void start(Stage stage) throws IOException {
    scene = new Scene(loadFXML("layout"), 800, 600);
    scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
    stage.setScene(scene);
    stage.setTitle("StockMaster");
    stage.isMaximized();
    stage.show();
  }

  static void setRoot(String fxml) throws IOException {
    scene.setRoot(loadFXML(fxml));
  }

  private static Parent loadFXML(String fxml) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
    return fxmlLoader.load();
  }

  public static void main(String[] args) {
    launch();
  }

  @Override
  public void stop() {
    if (connection != null) {
      try {
        connection.close();
        System.out.println("Database connection closed.");
      } catch (SQLException e) {
        System.err.println("Error closing database connection: " + e.getMessage());
      }
    }
  }

}
