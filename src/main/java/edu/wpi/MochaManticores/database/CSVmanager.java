package edu.wpi.MochaManticores.database;

import edu.wpi.MochaManticores.Nodes.MapSuper;
import edu.wpi.MochaManticores.Nodes.NodeSuper;
import edu.wpi.MochaManticores.Nodes.VertexList;

import java.io.*;
import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;

public class CSVmanager {

    //Attributes
    private String CSVpath;
    private String CSVdelim = ",";

    //Constructor
    public CSVmanager(String path) {
        this.CSVpath = path;
    }

    //load NODE CSV
    public void load_node_csv(Connection connect){
        System.out.println("code is getting here");
        String CSVpath = this.CSVpath;
        try{
            BufferedReader reader = new BufferedReader(new FileReader(CSVpath));
            String line = reader.readLine();

            String sql = "INSERT INTO NODES (nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connect.prepareStatement(sql);
            while (line != null){
                line = reader.readLine();
                String[] row = line.split(this.CSVdelim);

                //enter data
                pstmt.setString(1, row[0]);
                pstmt.setInt(2, Integer.parseInt(row[1]));
                pstmt.setInt(3, Integer.parseInt(row[2]));
                pstmt.setString(4, row[3]);
                pstmt.setString(5, row[4]);
                pstmt.setString(6, row[5]);
                pstmt.setString(7, row[6]);
                pstmt.setString(8, row[7]);
                pstmt.executeUpdate();
            }
        } catch (FileNotFoundException | SQLException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    //load EDGES CSV
    public void load_edges_csv(Connection connect){
        String CSVpath = this.CSVpath;
        try{
            BufferedReader reader = new BufferedReader(new FileReader(CSVpath));
            String line = reader.readLine();

            String sql = "INSERT INTO EDGES (edgeID, startNode, endNode) " +
                    "VALUES (?, ?, ?)";
            PreparedStatement pstmt = connect.prepareStatement(sql);

            while (line != null){
                line = reader.readLine();
                String[] row = line.split(this.CSVdelim);

                //enter data
                pstmt.setString(1, row[0]);
                pstmt.setString(2, row[1]);
                pstmt.setString(3, row[2]);
                pstmt.executeUpdate();
                MapSuper.getMap().get(row[1]).addNeighbor(row[2], 0);
            }
        } catch (FileNotFoundException | SQLException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    //save NODE CSV
    String makeNodes(Connection connect) throws SQLException, FileNotFoundException {
        PrintWriter pw = new PrintWriter(new File(this.CSVpath));
        StringBuilder sb = new StringBuilder();

        String sql = "SELECT * FROM NODES";
        Statement stmt = connect.createStatement();
        ResultSet results = stmt.executeQuery(sql);
        ResultSetMetaData rsmd = results.getMetaData();

        for(int i = 1; i <= rsmd.getColumnCount(); i++) {
            sb.append(rsmd.getColumnName(i));
            sb.append(",");
        }
        sb.append("\n");
        while (results.next()) {
            NodeSuper tempNode = new NodeSuper(Integer.parseInt(results.getString(2)), Integer.parseInt(results.getString(3)),
                    results.getString(4), results.getString(5), results.getString(7), results.getString(8),
                    results.getString(1), results.getString(6), new VertexList(new HashMap<>()));
            MapSuper.getMap().put(tempNode.getID(), tempNode);

            for(int i = 1; i <= rsmd.getColumnCount(); i++) {
                sb.append(results.getString(i));
                sb.append(",");
            }
            sb.append("\n");
        }
        results.close();
        pw.write(sb.toString());
        pw.close();
        return sb.toString();
    }
    //save EDGES CSV
     String printEdges(Connection connect) throws FileNotFoundException, SQLException {
        PrintWriter pw = new PrintWriter(new File(this.CSVpath));
        StringBuilder sb = new StringBuilder();

        String sql = "SELECT * FROM EDGES";
        Statement stmt = connect.createStatement();
        ResultSet results = stmt.executeQuery(sql);
        ResultSetMetaData rsmd = results.getMetaData();

        for(int i = 1; i <= rsmd.getColumnCount(); i++) {
            sb.append(rsmd.getColumnName(i));
            sb.append(",");
        }
        sb.append("\n");
        while (results.next()) {
            for(int i = 1; i <= rsmd.getColumnCount(); i++) {
                sb.append(results.getString(i));
                sb.append(",");
            }
            sb.append("\n");
        }
        results.close();
        pw.write(sb.toString());
        pw.close();
        return sb.toString();
    }
}
