package edu.wpi.MochaManticores.views;

import com.jfoenix.controls.*;
import edu.wpi.MochaManticores.App;
import edu.wpi.MochaManticores.Services.ServiceMap;
import edu.wpi.MochaManticores.Services.ServiceRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.image.ImageView;

public class FoodDelivery extends SceneController {

    @FXML
    private JFXComboBox<String> dietaryPreferences;

    @FXML
    private JFXTextArea allergiesField;

    @FXML
    private JFXComboBox<String> foodMenu;

    @FXML
    private StackPane dialogPane;

    @FXML
    private GridPane contentGrid;

    @FXML
    private ImageView backgroundIMG;

    @FXML
    private JFXButton backBtn;

    @FXML
    private JFXTextField empBox;


    @FXML
    private void goBack(ActionEvent e) {
        back();
    }

    private void loadHelpDialog(){
        dialogPane.toFront();
        dialogPane.setDisable(false);
        JFXDialogLayout message = new JFXDialogLayout();
        message.setMaxHeight(Region.USE_COMPUTED_SIZE);
        message.setMaxHeight(Region.USE_COMPUTED_SIZE);

        final Text hearder = new Text("Help Page");
        hearder.setStyle("-fx-font-weight: bold");
        hearder.setStyle("-fx-font-size: 60");
        hearder.setStyle("-fx-font-family: Roboto");
        hearder.setStyle("-fx-alignment: center");
        message.setHeading(hearder);

        final Text body = new Text("Dietary preferences: Dropdown menu with options for food.\n" +
                "Allergies: Indicate any allergies you may have with food or beverages.\n" +
                "Assign to Employee: Assign an employee to deliver you the food\n" +
                "Menu: This is the menu  ");

        body.setStyle("-fx-font-size: 40");
        body.setStyle("-fx-font-family: Roboto");
        body.setStyle("-fx-alignment: center");

        message.setBody(body);


        JFXDialog dialog = new JFXDialog(dialogPane, message,JFXDialog.DialogTransition.CENTER);

        JFXButton cont = new JFXButton("CONTINUE");
        cont.setOnAction(event -> {
            dialog.close();
            dialogPane.toBack();
        });

        dialog.setOnDialogClosed(event -> {
            dialogPane.toBack();
        });

        message.setActions(cont);
        dialog.show();

    }

    private void loadFullHelpDialogue() {
        dialogPane.toFront();
        loadHelpDialog();
    }


    public void initialize() {
        double height = App.getPrimaryStage().getScene().getHeight();
        double width = App.getPrimaryStage().getScene().getWidth();
        backgroundIMG.setFitHeight(height);
        backgroundIMG.setFitWidth(width);
        contentGrid.setPrefSize(width, height);

        backgroundIMG.fitWidthProperty().bind(App.getPrimaryStage().widthProperty());
        backgroundIMG.fitHeightProperty().bind(App.getPrimaryStage().heightProperty());

        System.out.println(width);

        dietaryPreferences.getItems().clear();
        dietaryPreferences.getItems().addAll("Vegan", "Vegetarian", "Gluten Free");

        // TODO Condition of menus depending of the dietary preference
        foodMenu.getItems().clear();
        foodMenu.getItems().addAll("Menu 0", "Menu 1", "Menu 2", "Menu 3");

        if(App.getClearenceLevel()<=0){
            empBox.setVisible(false);
        }

        //dialogPane.setDisable(false);
    }


    public void submitForm(ActionEvent e) {

        // changeSceneTo(e, "mainMenu");
        ServiceRequest.addRequest(new edu.wpi.MochaManticores.Services.FoodDelivery(App.getClearenceLevel()==1,
                false,
                0,
                dietaryPreferences.getSelectionModel().getSelectedItem(),
                allergiesField.getText(),
                foodMenu.getSelectionModel().getSelectedItem()), ServiceMap.FoodDelivery);
        dialogPane.setVisible(true);
        loadDialog();
    }

    public void loadDialog() {
        //TODO Center the text of it.

        dialogPane.toFront();
        dialogPane.setDisable(false);
        JFXDialogLayout message = new JFXDialogLayout();
        message.setMaxHeight(Region.USE_PREF_SIZE);
        message.setMaxHeight(Region.USE_PREF_SIZE);

        final Text hearder = new Text("Submited");
        hearder.setStyle("-fx-font-weight: bold");
        hearder.setStyle("-fx-font-size: 30");
        hearder.setStyle("-fx-font-family: Roboto");
        hearder.setStyle("-fx-alignment: center");
        message.setHeading(hearder);

        final Text body = new Text("Time estimated:");
        body.setStyle("-fx-font-size: 15");
        body.setStyle("-fx-font-family: Roboto");
        body.setStyle("-fx-alignment: center");
        message.setHeading(hearder);

        message.setBody(body);
        JFXDialog dialog = new JFXDialog(dialogPane, message, JFXDialog.DialogTransition.CENTER);
        JFXButton exit = new JFXButton("OK!");
        exit.setOnAction(event -> {
            back();
        });
        dialog.setOnDialogClosed(event -> {
            dialogPane.setDisable(true);
            dialogPane.toBack();
        });
        message.setActions(exit);
        dialog.show();

    }

    public void helpButton(MouseEvent mouseEvent) {
        loadHelpDialog();
    }
}
