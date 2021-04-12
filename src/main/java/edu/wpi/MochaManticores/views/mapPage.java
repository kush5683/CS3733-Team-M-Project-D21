package edu.wpi.MochaManticores.views;
//TODO:comment :(
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.controls.events.JFXDialogEvent;
import edu.wpi.MochaManticores.Nodes.NodeSuper;
import edu.wpi.MochaManticores.Nodes.MapSuper;
import edu.wpi.MochaManticores.Nodes.VertexList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.input.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Iterator;
import java.util.Set;

public class mapPage extends SceneController{
    public JFXTextField mapName;
    public JFXButton backButton;
    public TableView<Node> dispTable;
    public TableColumn<Node, String> xcoord;
    public TableColumn<Node, String> ycoord;
    public TableColumn<Node, String> floor;
    public TableColumn<Node, String> building;
    public TableColumn<Node, String> longName;
    public TableColumn<Node, String> shortName;
    public TableColumn<Node, String> nodeID;
    public ObservableList<Node> listOfNodes = FXCollections.observableArrayList();



    @FXML
    public VBox selectionPage;

    @FXML
    public VBox editPage;

    @FXML
    public StackPane dialogPane;
    public JFXTextField nodeIDField;
    public JFXTextField shortNameField;
    public JFXTextField logNameField;
    public JFXTextField buildingField;
    public JFXTextField floorField;
    public JFXTextField ycoordField;
    public JFXTextField xcoordField;
    public Node selectedNode;

    public class Node extends RecursiveTreeObject<Node>{
        public StringProperty xcoord;
        public StringProperty ycoord;
        public StringProperty floor;
        public StringProperty building;
        public StringProperty longName;
        public StringProperty shortName;
        public StringProperty nodeID;
        public Set<String> neighbors;

        public StringProperty[] fields;

        public Node(String xcoord, String ycoord, String floor, String building, String longName, String shortName, String nodeID, Set<String> neighbors) {
            this.xcoord = new SimpleStringProperty(xcoord);
            this.ycoord = new SimpleStringProperty(ycoord);
            this.floor = new SimpleStringProperty(floor);
            this.building = new SimpleStringProperty(building);
            this.longName = new SimpleStringProperty(longName);
            this.shortName = new SimpleStringProperty(shortName);
            this.nodeID = new SimpleStringProperty(nodeID);
            this.neighbors = neighbors;
            fields = new StringProperty[]{this.xcoord, this.ycoord,this.floor,this.building,this.longName,this.shortName,this.nodeID};
        }
        public Node(StringProperty[] fields, Set<String> neighbors){
            this.xcoord = fields[0];
            this.ycoord = fields[1];
            this.floor = fields[2];
            this.building = fields[3];
            this.longName = fields[4];
            this.shortName = fields[5];
            this.nodeID = fields[6];
            this.neighbors = neighbors;
        }

        public Set<String> getNeighbors() {
            return neighbors;
        }

        public void setNeighbors(Set<String> neighbors) {
            this.neighbors = neighbors;
        }

        public void setFields() {
            fields = new StringProperty[]{xcoord, ycoord,floor,building,longName,shortName,nodeID};
        }

        public void setXcoord(String xcoord) {
            this.xcoord.set(xcoord);
        }

        public void setYcoord(String ycoord) {
            this.ycoord.set(ycoord);
        }

        public void setFloor(String floor) {
            this.floor.set(floor);
        }

        public void setBuilding(String building) {
            this.building.set(building);
        }

        public void setLongName(String longName) {
            this.longName.set(longName);
        }

        public void setShortName(String shortName) {
            this.shortName.set(shortName);
        }

        public void setNodeID(String nodeID) {
            this.nodeID.set(nodeID);
        }

        @Override
        public String toString(){
            String s = "";
            s+= ("X-coord: "+getXcoord() + "\n");
            s+= ("Y-coord: "+getYcoord() + "\n");
            s+= ("Floor: "+getFloor() + "\n");
            s+= ("Building: "+getBuilding() + "\n");
            s+= ("Long Name: "+getLongName() + "\n");
            s+= ("Short Name: "+getShortName() + "\n");
            s+= ("NodeID: "+getNodeID() + "\n");
            return s;
        }

        public StringProperty[] getFields() {
            return fields;
        }

        public String getXcoord() {
            return xcoord.get();
        }

        public StringProperty xcoordProperty() {
            return xcoord;
        }

        public String getYcoord() {
            return ycoord.get();
        }

        public StringProperty ycoordProperty() {
            return ycoord;
        }

        public String getFloor() {
            return floor.get();
        }

        public StringProperty floorProperty() {
            return floor;
        }

        public String getBuilding() {
            return building.get();
        }

        public StringProperty buildingProperty() {
            return building;
        }

        public String getLongName() {
            return longName.get();
        }

        public StringProperty longNameProperty() {
            return longName;
        }

        public String getShortName() {
            return shortName.get();
        }

        public StringProperty shortNameProperty() {
            return shortName;
        }

        public String getNodeID() {
            return nodeID.get();
        }

        public StringProperty nodeIDProperty() {
            return nodeID;
        }
    }

    public void initialize() {
        xcoord = new TableColumn<Node, String>("X-coordinates");
        xcoord.setMinWidth(100);
        xcoord.setCellValueFactory(
                new PropertyValueFactory<Node, String>("xcoord"));

        ycoord = new TableColumn<Node, String>("Y-coordinates");
        ycoord.setMinWidth(100);
        ycoord.setCellValueFactory(
                new PropertyValueFactory<Node, String>("ycoord"));
        floor = new TableColumn<>("floor");
        floor.setMinWidth(100);
        floor.setCellValueFactory(
                new PropertyValueFactory<Node, String>("floor"));

        building = new TableColumn<Node, String>("building");
        building.setMinWidth(100);
        building.setCellValueFactory(
                new PropertyValueFactory<Node, String>("building"));

        longName = new TableColumn<>("longName");
        longName.setMinWidth(100);
        longName.setCellValueFactory(
                new PropertyValueFactory<Node, String>("longName"));

        shortName = new TableColumn<Node, String>("shortName");
        shortName.setMinWidth(100);
        shortName.setCellValueFactory(
                new PropertyValueFactory<Node, String>("shortName"));
        nodeID = new TableColumn<>("nodeID");
        nodeID.setMinWidth(100);
        nodeID.setCellValueFactory(
                new PropertyValueFactory<Node, String>("nodeID"));

        listOfNodes = buildTable("");
        editPage.setVisible(false);
        selectionPage.setVisible(true);
    }

    public void displayTable() {

    }

    public void searchPressed(ActionEvent e){
        String searchTerm = mapName.getText();

        if(mapName.getText().equals("")){
            buildTable("");
        }else{
            buildTable(searchTerm);
        }
    }

    public void searchTyped(KeyEvent e){
        String searchTerm = mapName.getText();

        if(mapName.getText().equals("")){
            buildTable("");
        }else{
            buildTable(searchTerm);
        }
    }

    private ObservableList<Node> buildTable(String searchTerm){
        ObservableList<Node> nodes = FXCollections.observableArrayList();
        Iterator<NodeSuper> mapIter = MapSuper.getMap().values().iterator();
        for (int i = 0; i <MapSuper.getMap().size(); i++) {
            NodeSuper n = mapIter.next();
            Node nodeToAdd = new Node(Integer.toString(n.getXcoord()),
                    Integer.toString(n.getYcoord()),
                    n.getFloor(),
                    n.getBuilding(),
                    n.getLongName(),
                    n.getShortName(),
                    n.getID(),
                    n.getNeighbors());
            for (int j = 0; j < nodeToAdd.getFields().length; j++) {
                if(nodeToAdd.getFields()[j].get().toLowerCase().contains(searchTerm.toLowerCase()) || searchTerm.equals("")){
                    nodes.add(nodeToAdd);
                }
            }

        }

        dispTable.setItems(nodes);
        dispTable.getColumns().setAll(xcoord,ycoord,floor,building,longName,shortName,nodeID);
        return nodes;
    }

    public void cancel(ActionEvent e){
        returnToMain(e);
    }

    public void submit(ActionEvent e){
        Node n = dispTable.getSelectionModel().getSelectedItem();
        if (n == null){
            loadErrorDialog();
        }else{
            selectedNode = new Node(n.getFields(),n.getNeighbors()); //TODO: selectedNode should not be a reference to n
            System.out.println("Node Info:\n"+n);
            loadEditPage(n);
        }

    }

    public void loadErrorDialog(){
        dialogPane.toFront();
        dialogPane.setDisable(false);
        JFXDialogLayout message = new JFXDialogLayout();
        message.setHeading(new Text("Oops!"));
        message.setBody(new Text("Please select a table entry before editing."));
        JFXDialog dialog = new JFXDialog(dialogPane, message,JFXDialog.DialogTransition.CENTER);
        JFXButton exit = new JFXButton("DONE");
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
                dialogPane.setDisable(true);
                dialogPane.toBack();
            }
        });
        dialog.setOnDialogClosed(new EventHandler<JFXDialogEvent>() {
            @Override
            public void handle(JFXDialogEvent event) {
                dialogPane.setDisable(true);
                dialogPane.toBack();
            }
        });
        message.setActions(exit);
        dialog.show();
    }

    public void loadEditPage(Node node){
        selectionPage.setVisible(false);
        editPage.setVisible(true);
        xcoordField.setText(node.getXcoord());
        ycoordField.setText(node.getYcoord());
        floorField.setText(node.getFloor());
        buildingField.setText(node.getBuilding());
        logNameField.setText(node.getLongName());
        shortNameField.setText(node.getShortName());
        nodeIDField.setText(node.getNodeID());
    }

    public void submitEdit(ActionEvent e){
        for (Node node : listOfNodes) {
            if (node.getNodeID().equals(selectedNode.getNodeID())) {
                Node n = updateNode(node);
                break;
            }
            }
        //TODO:Talk to CSV Manager
        ;
        cancelEdit(e);
    }

    public Node updateNode(Node n){
        n.setXcoord(xcoordField.getText());
        n.setYcoord(ycoordField.getText());
        n.setFloor(floorField.getText());
        n.setBuilding(buildingField.getText());
        n.setLongName(logNameField.getText());
        n.setShortName(shortNameField.getText());
        n.setNodeID(nodeIDField.getText());
        n.setFields();
        System.out.println(n);
        System.out.println(selectedNode);

        System.out.println("printed");
        return n;
    }

    public void cancelEdit(ActionEvent e){
        editPage.setVisible(false);
        selectionPage.setVisible(true);

    }

    public void back(ActionEvent e){
        super.back(e);
    }
}