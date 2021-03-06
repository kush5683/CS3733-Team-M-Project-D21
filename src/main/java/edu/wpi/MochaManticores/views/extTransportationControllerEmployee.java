package edu.wpi.MochaManticores.views;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.MochaManticores.App;
import edu.wpi.MochaManticores.Services.ExternalTransportation;
import edu.wpi.MochaManticores.database.DatabaseManager;
import edu.wpi.MochaManticores.database.sel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class extTransportationControllerEmployee extends SceneController {


    ObservableList<String> transportationMethod = FXCollections.observableArrayList("Ambulance", "Helicopter", "Plane");



    @FXML
    private GridPane contentGrid;

    @FXML
    private GridPane requestPage;

    @FXML
    private GridPane managerPage;

    @FXML
    private ImageView backgroundIMG;
    @FXML
    private StackPane dialogPane;
    @FXML
    private JFXTextField empBox;
    @FXML
    private JFXTextField patientRoom, currentRoom, externalRoom;
    @FXML
    private ComboBox<String> transportationMethods;



    @FXML
    private JFXComboBox employeeAssigned;

    private void createFilterListener(JFXComboBox comboBox) {

        // Create the listener to filter the list as user enters search terms
        FilteredList<String> filteredList = new FilteredList<>(comboBox.getItems());

        // Add listener to our ComboBox textfield to filter the list
        comboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            comboBox.show();
            filteredList.setPredicate(item -> {


                // If the TextField is empty, return all items in the original list
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Check if the search term is contained anywhere in our list
                return item.toLowerCase().contains(newValue.toLowerCase().trim());

            });
        });

        // Finally, let's add the filtered list to our ComboBox
        comboBox.setItems(filteredList);

    }

    @FXML
    private void initialize() {
        employeeAssigned.setEditable(true);
        //fromLocation.setOnKeyTyped(new AutoCompleteComboBoxListener<>(fromLocation));
        ObservableList<String> items = FXCollections.observableArrayList();
        items.addAll(DatabaseManager.getEmpManager().getEmployeeNames());
        employeeAssigned.setItems(items);
        createFilterListener(employeeAssigned);


        double height = App.getPrimaryStage().getScene().getHeight();
        double width = App.getPrimaryStage().getScene().getWidth();
        backgroundIMG.setFitHeight(height);
        backgroundIMG.setFitWidth(width);

        backgroundIMG.fitWidthProperty().bind(App.getPrimaryStage().widthProperty());
        backgroundIMG.fitHeightProperty().bind(App.getPrimaryStage().heightProperty());

        transportationMethods.setItems(transportationMethod);

        dialogPane.setDisable(false);

        if(App.getClearenceLevel()<=0){
            employeeAssigned.setVisible(false);
        }

        changeToRequest(null);


//        externalTable.getSelectionModel().cellSelectionEnabledProperty().set(true);
//        patientRoomColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//        patientRoomColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//        currentRoomColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//        externalRoomColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//        transportationMethodColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//        employeeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//        completedColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    }





    public void submitEvent() {
        if (!externalRoom.getText().isEmpty() && !currentRoom.getText().isEmpty() && !patientRoom.getText().isEmpty() && !employeeAssigned.getSelectionModel().isEmpty()) {
            sel s = sel.ExternalTransportation;
            ExternalTransportation toAdd = new edu.wpi.MochaManticores.Services.ExternalTransportation(
                    "",
                    (String) employeeAssigned.getSelectionModel().getSelectedItem(),
                    false,
                    patientRoom.getText(),
                    currentRoom.getText(),
                    externalRoom.getText(),
                    transportationMethods.getSelectionModel().getSelectedItem());
            DatabaseManager.addRequest(s, toAdd);
            toAdd.send(toAdd.getRequestID());
            System.out.println("runned");
            loadSubmitDialog();
        } else if (patientRoom.getText().isEmpty()) {
            RequiredFieldValidator missingInput = new RequiredFieldValidator();
            patientRoom.getValidators().add(missingInput);
            missingInput.setMessage("Patient room is required");
            patientRoom.validate();
        } else if (currentRoom.getText().isEmpty()) {
            RequiredFieldValidator missingInput = new RequiredFieldValidator();
            currentRoom.getValidators().add(missingInput);
            missingInput.setMessage("Current room is required");
            currentRoom.validate();
        } else if (externalRoom.getText().isEmpty()) {
            RequiredFieldValidator missingInput = new RequiredFieldValidator();
            externalRoom.getValidators().add(missingInput);
            missingInput.setMessage("External room is required");
            externalRoom.validate();
        } else if (employeeAssigned.getSelectionModel().isEmpty()) {
            RequiredFieldValidator missingInput = new RequiredFieldValidator();
            employeeAssigned.getValidators().add(missingInput);
            missingInput.setMessage("Please assign an employee");
            employeeAssigned.validate();
        }
    }

    public void helpButton(){loadHelpDialogue();}

    public void changeToRequest(ActionEvent actionEvent) {
        requestPage.setVisible(true);
        managerPage.setVisible(false);
        requestPage.toFront();
    }


    private void loadDialog(){
        JFXDialogLayout message = new JFXDialogLayout();
        message.setMaxHeight(Region.USE_COMPUTED_SIZE);
        message.setMaxHeight(Region.USE_COMPUTED_SIZE);

        final Text hearder = new Text("Help Page");
        hearder.setStyle("-fx-font-weight: bold");
        hearder.setStyle("-fx-font-size: 60");
        hearder.setStyle("-fx-font-family: Roboto");
        hearder.setStyle("-fx-alignment: center");
        message.setHeading(hearder);

        final Text body = new Text("Patient room: This is the room number given to the patient by the hospital.\n" +
                "Current Room: This is where the patient is currently staying until transportation out of the hospital.\n" +
                "External Room: This is the location where the patient is going to be transported to.\n" +
                "Transportation Method: This is a dropdown menu that you select which type of transportation the patient will take.\n" +
                "Assign to Employee: Select an employee from the provided dropdown menu.");

        body.setStyle("-fx-font-size: 40");
        body.setStyle("-fx-font-family: Roboto");
        body.setStyle("-fx-alignment: center");

        message.setBody(body);


        JFXDialog dialog = new JFXDialog(dialogPane, message,JFXDialog.DialogTransition.CENTER);

        JFXButton cont = new JFXButton("Continue");
        cont.setStyle("-fx-font-size: 15");
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

    private void loadHelpDialogue() {
        dialogPane.toFront();
        loadDialog();
    }

    public void loadSubmitDialog(){
        //TODO Center the text of it.
        dialogPane.toFront();
        dialogPane.setDisable(false);
        JFXDialogLayout message = new JFXDialogLayout();
        message.setMaxHeight(Region.USE_PREF_SIZE);
        message.setMaxHeight(Region.USE_PREF_SIZE);

        final Text hearder = new Text("Submitted request");
        hearder.setStyle("-fx-font-weight: bold");
        hearder.setStyle("-fx-font-size: 30");
        hearder.setStyle("-fx-font-family: Roboto");
        hearder.setStyle("-fx-alignment: center");
        message.setHeading(hearder);

        final Text body = new Text("Your request has been submitted for the patient.");
        body.setStyle("-fx-font-size: 15");
        body.setStyle("-fx-font-family: Roboto");
        body.setStyle("-fx-alignment: center");
        message.setHeading(hearder);

        message.setBody(body);
        JFXDialog dialog = new JFXDialog(dialogPane, message,JFXDialog.DialogTransition.CENTER);
        JFXButton ok = new JFXButton("Done");
        ok.setStyle("-fx-font-size: 15");
        ok.setOnAction(event -> {
            changeSceneTo("landingPage");
        });

        dialog.setOnDialogClosed(event -> {
            dialogPane.toBack();
            dialog.close();
        });

        message.setActions(ok);
        dialog.show();
    }



    public void changeManagerTable(ActionEvent actionEvent) {
        requestPage.setVisible(false);
        managerPage.setVisible(true);
        managerPage.toFront();
    }


}
