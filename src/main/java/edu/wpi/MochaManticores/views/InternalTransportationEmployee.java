package edu.wpi.MochaManticores.views;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.MochaManticores.App;
import edu.wpi.MochaManticores.Services.ServiceRequest;
import edu.wpi.MochaManticores.Services.ServiceRequestType;
import edu.wpi.MochaManticores.database.DatabaseManager;
import edu.wpi.MochaManticores.database.sel;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.Arrays;
import java.util.LinkedList;

public class InternalTransportationEmployee {

    public class it extends RecursiveTreeObject<it> {

        edu.wpi.MochaManticores.Services.InternalTransportation ref;
        StringProperty patientIDTable;
        IntegerProperty numStaffNeededTable;
        StringProperty destinationTable;
        StringProperty transportationMethodsTable;
        @FXML
        JFXComboBox employeeAssigned;
        LinkedList<String> fields;


        public it(edu.wpi.MochaManticores.Services.ServiceRequest ref){
            this.ref = (edu.wpi.MochaManticores.Services.InternalTransportation) ref;
            patientIDTable = new SimpleStringProperty(this.ref.getPatientID());
            numStaffNeededTable = new SimpleIntegerProperty(this.ref.getNumStaffNeeded());
            destinationTable = new SimpleStringProperty(this.ref.getDestination());
            transportationMethodsTable = new SimpleStringProperty(this.ref.getTransportationMethod());
            fields = new LinkedList<>(Arrays.asList(patientIDTable.get(), Integer.toString(numStaffNeededTable.get()),
                    destinationTable.get(),transportationMethodsTable.get()));
        }

        public LinkedList<String> getFields() {
            return fields;
        }

        public String getTransportationMethod(){
            return transportationMethodsTable.get();
        }

        public edu.wpi.MochaManticores.Services.InternalTransportation getRef() {
            return ref;
        }

        public String getPatientID(){
            return patientIDTable.get();
        }

        public int getNumStaffNeeded(){
            return numStaffNeededTable.get();
        }

        public String getDestination(){
            return destinationTable.get();
        }
    }

    ObservableList<String> typeOfTransportList = FXCollections
            .observableArrayList("Wheelchair","Walker","Medical Bed");

    @FXML
    private ImageView backgroundIMG;

    @FXML
    private GridPane contentGrid;

    @FXML
    private StackPane dialogPane;

    @FXML
    private JFXTextField patientID, numberOfStaff, destination, empBox;

    @FXML
    private ComboBox<String> transportComboBox;

    @FXML
    private TableView<it> internalTransportationTable;

    @FXML
    private TableView<it> externalTable;

    @FXML
    private JFXComboBox employeeAssigned;

    @FXML
    private GridPane requestPage;

    @FXML
    private GridPane managerPage;

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
        double height = App.getPrimaryStage().getScene().getHeight();
        double width = App.getPrimaryStage().getScene().getWidth();
        backgroundIMG.setFitHeight(height);
        backgroundIMG.setFitWidth(width);
        contentGrid.setPrefSize(width, height);

        backgroundIMG.fitWidthProperty().bind(App.getPrimaryStage().widthProperty());
        backgroundIMG.fitHeightProperty().bind(App.getPrimaryStage().heightProperty());

        //externalTable.setEditable(true);

        this.employeeAssigned.setEditable(true);
        //fromLocation.setOnKeyTyped(new AutoCompleteComboBoxListener<>(fromLocation));
        ObservableList<String> items = FXCollections.observableArrayList();
        DatabaseManager.getEmployeeNames().forEach(s -> {
            items.add(s.substring(s.indexOf(" ")));
        });
        employeeAssigned.setItems(items);
        createFilterListener(employeeAssigned);

        transportComboBox.setItems(typeOfTransportList);

        dialogPane.toBack();

        if(App.getClearenceLevel()<=0){
            empBox.setVisible(false);
        }

        // Initializing the tables for the manager
//        patientIdColumn = new TableColumn<it, String>("Patient ID");
//        patientIdColumn.setPrefWidth(100);
//        patientIdColumn.setCellValueFactory(new PropertyValueFactory<it, String>("patientID"));
//
//        numStaffNeededColumn = new TableColumn<it, Integer>("Number of Staff Needed");
//        numStaffNeededColumn.setPrefWidth(100);
//        numStaffNeededColumn.setCellValueFactory(new PropertyValueFactory<it, Integer>("numStaffNeeded"));
//
//        destinationColumn = new TableColumn<it, String>("Destination Place");
//        destinationColumn.setPrefWidth(100);
//        destinationColumn.setCellValueFactory(new PropertyValueFactory<it, String>("transportationMethod"));
//
       // managerPage.setVisible(false);
       // requestPage.setVisible(true);
      //  requestPage.toFront();
//
//        buildTable("");
    }

    public void submitEvent(ActionEvent actionEvent) {
        if(!patientID.getText().isEmpty() || !numberOfStaff.getText().isEmpty() ||
                !destination.getText().isEmpty() || !transportComboBox.getSelectionModel().getSelectedItem().isEmpty()){
            sel s = sel.InternalTransportation;
            DatabaseManager.addRequest(s, new edu.wpi.MochaManticores.Services.InternalTransportation(
                    "",
                    employeeAssigned.getEditor().getText(),
                    false,
                    patientID.getText(),
                    Integer.parseInt(numberOfStaff.getText()),
                    destination.getText(),
                    transportComboBox.getValue()
            ));
            System.out.println("Adds to database");
        }
    }

    public void openHelp(MouseEvent mouseEvent) {
    }

    private ObservableList<it> buildTable(String searchTerm) {
        ObservableList<it> tableRow = FXCollections.observableArrayList();

        LinkedList<ServiceRequest> requests = DatabaseManager.getServiceMap().getServiceRequestsForType(ServiceRequestType.InternalTransportation);

        for (ServiceRequest s : requests) {
            it itToAdd = new it(s);
            for (int i = 0; i < itToAdd.getFields().size(); i++){
                if(itToAdd.getFields().get(i).toLowerCase().equals(searchTerm) || searchTerm.equals("")){
                    //System.out.println(i + " " + itToAdd.getDietaryPreference());
                    tableRow.add(itToAdd);
                    break;
                }
            }
        }
        internalTransportationTable.setItems(tableRow);
        //internalTransportationTable.getColumns().setAll(patientIdColumn,numStaffNeededColumn,
          //      destinationColumn,transportationMethodColumn);

        return tableRow;
    }

    public void changeToRequest(ActionEvent actionEvent) {
        requestPage.setVisible(true);
        managerPage.setVisible(false);
        requestPage.toFront();
    }

    public void changeManagerTable(ActionEvent actionEvent) {
        requestPage.setVisible(false);
        managerPage.setVisible(true);
        managerPage.toFront();
    }

}
