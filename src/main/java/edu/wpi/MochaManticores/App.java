package edu.wpi.MochaManticores;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

import edu.wpi.MochaManticores.Algorithms.AStar2;
import edu.wpi.MochaManticores.Algorithms.PathPlanning;
import edu.wpi.MochaManticores.Services.ServiceMap;
import edu.wpi.MochaManticores.Services.ServiceRequest;
import edu.wpi.MochaManticores.database.EdgeManager;
import edu.wpi.MochaManticores.database.EmployeeManager;
import edu.wpi.MochaManticores.database.Mdb;
import edu.wpi.MochaManticores.database.NodeManager;
import edu.wpi.MochaManticores.database.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class App extends Application {
  private static Stage primaryStage;
  private static int clearenceLevel;
  private static NodeManager nodeManager;
  private static EdgeManager edgeManager;
  private static EmployeeManager employeeManager;
  private static PathPlanning algoType = new AStar2();
  private static String currentUsername;

  public static String getCurrentUsername() {
    return currentUsername;
  }

  public static void setCurrentUsername(String currentUsername) {
    App.currentUsername = currentUsername;
  }

  public static PathPlanning getAlgoType() {
    return App.algoType;
  }

  public static void setAlgoType(PathPlanning algoType) {
    App.algoType = algoType;
  }

  public static NodeManager getNodeManager() {
    return nodeManager;
  }

  public static void setNodeManager(NodeManager nodeManager) {
    App.nodeManager = nodeManager;
  }

  public static EdgeManager getEdgeManager() {
    return edgeManager;
  }

  public static void setEdgeManager(EdgeManager edgeManager) {
    App.edgeManager = edgeManager;
  }

  public static EmployeeManager getEmployeeManager() {
    return employeeManager;
  }

  public static void setEmployeeManager(EmployeeManager employeeManager) {
    App.employeeManager = employeeManager;
  }

  public static int getClearenceLevel() {
    return clearenceLevel;
  }

  public static void setClearenceLevel(int clearenceLevel) {
    App.clearenceLevel = clearenceLevel;
  }

  @Override
  public void init() throws InterruptedException, FileNotFoundException, SQLException {
    System.out.println("Starting Up");
    System.out.println("Starting Database");
    DatabaseManager.startup();
  }

  @Override
  public void start(Stage primaryStage) {


    App.primaryStage = primaryStage;
    try {
      Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml/loadingPage.fxml")));
      Scene scene = new Scene(root);
      //primaryStage.setMaximized(true);
      //primaryStage.setFullScreen(true);
      primaryStage.setScene(scene);
      primaryStage.setMinHeight(800);
      primaryStage.setMinWidth(1280);
      primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
      primaryStage.show();
      root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml/loginPage.fxml")));
      App.getPrimaryStage().getScene().setRoot(root);

    } catch (IOException e) {
      e.printStackTrace();
      Platform.exit();
    }
  }

  public static Stage getPrimaryStage(){
    return primaryStage;
  }


  @Override
  public void stop() {
    System.out.println("Shutting Down");
    DatabaseManager.shutdown();
  }
}
