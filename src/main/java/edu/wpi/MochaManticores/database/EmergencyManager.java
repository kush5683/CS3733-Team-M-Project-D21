package edu.wpi.MochaManticores.database;

import edu.wpi.MochaManticores.Exceptions.InvalidElementException;
import edu.wpi.MochaManticores.Nodes.EdgeMapSuper;
import edu.wpi.MochaManticores.Services.EmergencyRequest;
import edu.wpi.MochaManticores.Services.InternalTransportation;
import edu.wpi.MochaManticores.Services.ServiceRequestType;

import java.io.*;
import java.sql.*;

public class EmergencyManager  extends Manager<EmergencyRequest> {
    private static String csv_path = "data/services/EmergencyRequest.csv";
    private static Connection connection = null;
    private static final String CSVdelim = ",";
    private static final ServiceRequestType type = ServiceRequestType.Emergency;

    EmergencyManager(Connection connection, String csv_path){
        EmergencyManager.connection = connection;
        if(csv_path != null){
            EmergencyManager.csv_path = csv_path;
        }
    }

    @Override
    void loadFromCSV() {
        //loads database and sets hashmap
        try{
            BufferedReader reader = new BufferedReader(new FileReader(csv_path));
            String line = reader.readLine();

            while (line != null){
                line = reader.readLine();
                if(line == null) break;
                String[] row = line.split(CSVdelim);

                EmergencyRequest temp = new EmergencyRequest("",row[1],Boolean.parseBoolean(row[2]),
                        Integer.parseInt(row[3]),row[4],Boolean.parseBoolean(row[5]));
                addElement(temp);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    void addElement(EmergencyRequest temp) {
        temp.setRequestID(temp.generateRequestID(type));
        addElement_db(temp);
        addElement_map(temp);
    }
    void addElement(EmergencyRequest temp, String ID){
        temp.setRequestID(ID);
        addElement_db(temp);
        addElement_map(temp);
    }

    void addElement_db(EmergencyRequest temp) {

        try {
            String sql = "INSERT INTO EMGREQ (RequestID, EmpID, Completed, numPeopleNeeded, location, gurney) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, temp.getRequestID());
            pstmt.setString(2, temp.getEmployee());
            pstmt.setBoolean(3, temp.getCompleted());
            pstmt.setInt(4,temp.getNumPeopleNeeded());
            pstmt.setString(5, temp.getLocation());
            pstmt.setBoolean(6, temp.isGurney());
            pstmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    void addElement_map(EmergencyRequest temp) {
        if(!DatabaseManager.getServiceMap().containsRequest(type, temp.RequestID)) {
            DatabaseManager.getServiceMap().addRequest(type,temp);
        }
        else {
            System.out.printf("This node %s already exists\n", temp.RequestID);
        }
    }

    @Override
    void delElement(String ID) throws SQLException {
        //remove node from database
        PreparedStatement pstmt = connection.prepareStatement("DELETE FROM EMGREQ WHERE RequestID=?");
        pstmt.setString(1, ID);
        pstmt.executeUpdate();

        // remove node from map
        DatabaseManager.getServiceMap().delRequest(type,ID);
    }

    @Override
    void modElement(String ID, EmergencyRequest temp) throws SQLException {
        delElement(ID);
        addElement(temp);
    }

    @Override
    void saveElements() throws FileNotFoundException, SQLException {
        PrintWriter pw = new PrintWriter(new File(csv_path));
        StringBuilder sb = new StringBuilder();

        String sql = "SELECT * FROM EMGREQ";
        Statement stmt = connection.createStatement();
        ResultSet results = stmt.executeQuery(sql);
        ResultSetMetaData rsmd = results.getMetaData();

        for(int i = 1; i <= rsmd.getColumnCount(); i++) {
            sb.append(rsmd.getColumnName(i));
            sb.append(",");
        }
        sb.append("\n");
        while (results.next()) {
            //writing to csv file
            for(int i = 1; i <= rsmd.getColumnCount(); i++) {
                sb.append(results.getString(i));
                sb.append(",");
            }
            sb.append("\n");
        }
        results.close();
        pw.write(sb.toString());
        pw.close();
    }

    @Override
    EmergencyRequest getElement(String ID) throws InvalidElementException {
        // unlike employeeManager, we get nodes from the map so that they include neighbors
        if(DatabaseManager.getServiceMap().containsRequest(type,ID)){
            return (EmergencyRequest) DatabaseManager.getServiceMap().getRequest(type,ID); //TODO DOES THIS CAST BREAK THINGS
        }else{
            throw new InvalidElementException();
        }
    }

    @Override
    String getCSV_path() {
        return csv_path;
    }

    @Override
    void setCSV_path(String s) {
        csv_path = s;
    }

    @Override
    void cleanTable() throws SQLException {
        String sql = "DELETE FROM EMGREQ";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        int result = pstmt.executeUpdate();

        //clean hashmap
        DatabaseManager.getServiceMap().clearMap(ServiceRequestType.Emergency);
    }

    @Override
    void cleanMap(){
        DatabaseManager.getServiceMap().clearMap(ServiceRequestType.Emergency);
    }

    @Override
    public void updateElementMap() throws SQLException {
        String sql = "SELECT * FROM EMGREQ";
        Statement stmt = connection.createStatement();
        ResultSet result = stmt.executeQuery(sql);
        while (result.next()) {
            EmergencyRequest temp = new EmergencyRequest(result.getString(1),result.getString(2),
                    Boolean.parseBoolean(result.getString(3)),
                    Integer.parseInt(result.getString(4)),
                    result.getString(5),
                    Boolean.parseBoolean(result.getString(6)));
            addElement_map(temp);
        }
    }

}