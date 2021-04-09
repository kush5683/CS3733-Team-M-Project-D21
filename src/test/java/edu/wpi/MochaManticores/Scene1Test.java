package edu.wpi.MochaManticores;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;

import static org.testfx.api.FxAssert.verifyThat;

public class Scene1Test extends ApplicationTest {

  @Override
  public void start(Stage primaryStage) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("fxml/landingPage.fxml"));
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  @Test
  public void testButton() {
    verifyThat("Scene 1", Node::isVisible);
    clickOn("Advance Scene");
    verifyThat("Scene 2", Node::isVisible);
  }
}
