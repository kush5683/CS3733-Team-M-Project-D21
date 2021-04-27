package edu.wpi.MochaManticores.database;

import edu.wpi.MochaManticores.Exceptions.InvalidElementException;
import edu.wpi.MochaManticores.Exceptions.InvalidLoginException;
import edu.wpi.MochaManticores.Exceptions.InvalidPermissionsException;
import edu.wpi.MochaManticores.Nodes.NodeSuper;
import edu.wpi.MochaManticores.Nodes.EdgeSuper;
import edu.wpi.MochaManticores.Services.SanitationServices;
import edu.wpi.MochaManticores.Services.ServiceMap;
import edu.wpi.MochaManticores.Services.ServiceRequest;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
public class DatabaseManager{
    private static Connection connection = null;
    private static EmployeeManager empManager = null;
    private static NodeManager nodeManager = null;
    private static EdgeManager edgeManager = null;

    private static EmergencyManager emergencyManager = null;
    private static ExtTransportManager extTransportManager = null;
    private static IntTransportManager intTransportManager = null;
    private static FoodDeliveryManager foodDeliveryManager = null;
    private static FloralDeliveryManager floralDeliveryManager = null;
    private static SanitationServiceManager sanitationServiceManager = null;
    private static ReligiousManager religiousManager = null;
    private static MedicineRequestManager medicineRequestManager = null;
    private static LaundryManager laundryManager = null;
    private static LanguageInterpreterManager languageInterpreterManager = null;

    private static ServiceMap serviceMap = null;

    // ==== Mdb methods ==== //

    /*  function: startup()
     *  starts the database, should only be run on app startup
     *  @return void
     */
    public static void startup(){
        try{
            Mdb.databaseStartup();
        }catch(InterruptedException | SQLException e){
            e.printStackTrace();
        }
    }

    /*  function: shutdown()
     *  closes the database connection and saves CSV's
     *  @return void
     */
    public static void shutdown(){
        Mdb.databaseShutdown();
    }

    // ==== Manager Methods === //

    /*  function: resetTable(sel, string)
     *  resets the selected table to the CSV at the path given.
     *  @return void
     */
    public static void resetTable(sel s, String newPath){
        try {
            Manager man = getManager(s);
            System.out.println("resetting Table: " + s);
            man.saveElements();
            man.cleanTable();
            man.setCSV_path(newPath);
            man.loadFromCSV();
        }catch(SQLException | FileNotFoundException e){
            e.printStackTrace();
        }
    }

    /* function: addRequest()
     * adds request to specified manager table
     * @ return void
     */
    public static void addRequest(sel s, ServiceRequest request){
        getManager(s).addElement(request);
    }

    /*  function: addNode()
     *  adds a node to the database and MapSuper()
     *  @return void
     */
    public static void addNode(NodeSuper node){
        getNodeManager().addElement(node);
    };

    /*  function: addEdge()
     *  adds an Edge to the database and EdgeMapSuper()
     *  @return void
     */
    public static void addEdge(EdgeSuper edge){
        getEdgeManager().addElement(edge);
    };

    /*  function: addEmployee()
     *  adds an employee to the database
     *  @return void
     */
    public static void addEmployee(Employee employee){
        getEmpManager().addElement(employee);
    };

    /*  function: addElement()
     *  deletes a selected element with a given ID
     *  @return void
     */
    public static void delElement(sel s, String ID) {
        try {
            getManager(s).delElement(ID);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    /*  function:  modRequest()
     *  swaps a specifed node with a new node
     *  @return void
     */
    public static void modRequest(sel s, String ID, ServiceRequest request){
        try{
            getManager(s).modElement(ID,request);
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /*  function:  modNode()
     *  swaps a specifed node with a new node
     *  @return void
     */
    public static void modNode(String ID, NodeSuper newNode){
        try{
            getNodeManager().modElement(ID,newNode);
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /*  function:  modEdge()
     *  swaps a specifed edge with a new edge
     *  @return void
     */
    public static void modEdge(String ID, EdgeSuper newEdge){
        try{
            getEdgeManager().modElement(ID,newEdge);
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /*  function:  modEmployee()
     *  swaps a specifed employee with a new employee
     *  @return void
     */
    public static void modEmployee(String ID, Employee newEmployee){
        try{
            getEmpManager().modElement(ID,newEmployee);
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /* function: getRequest()
     * returns a request object, you will need to cast it
     * return ServiceRequest
     */
    public static ServiceRequest getRequest(sel s, String ID) throws InvalidElementException {
        return (ServiceRequest) getManager(s).getElement(ID);  // TODO GENERALIZE ALL MANAGERS
    }

    /*  function:  getNode()
     *  returns a node specifed by ID
     *  @return NodeSuper
     */
    public static NodeSuper getNode(String ID) throws InvalidElementException {
        return getNodeManager().getElement(ID);
    }

    /*  function:  getEdge()
     *  returns a Edge specifed by ID
     *  @return EdgeSuper
     */
    public static EdgeSuper getEdge(String ID) throws InvalidElementException {
        return getEdgeManager().getElement(ID);
    }
    /*  function:  getEmployee()
     *  returns a Employee specifed by ID
     *  @return Employee
     */
    public static Employee getEmployee(String ID) throws InvalidElementException {
        return getEmpManager().getElement(ID);
    }

    /*  function:  getCSV_path()
     *  gets the operating CSV path of selection
     *  @return string
     */
    public static String getCSV_path(sel s){
        return getManager(s).getCSV_path();
    }

    // ==== Employee specials ==== //

    /*  function:  checkEmployeeLogin()
     *  checks user password and clearance, throws invalid login if pass is wrong, throws invalid element if user doesnt exist
     *  @return Employee
     */
    public static Employee checkEmployeeLogin(String usr, String pass) throws InvalidLoginException, InvalidElementException {
        return getEmpManager().checkEmployeeLogin(usr,pass);
    }

    /*  function:  checkAdminLogin()
     *  checks user password and clearance, throws invalid login if pass is wrong, throws invalid element if user doesnt exist,
     *  Throws invalid permissions if user is not an admin
     *  @return Employee
     */
    public static Employee checkAdminLogin(String usr, String pass) throws InvalidLoginException, InvalidPermissionsException, InvalidElementException{
        return getEmpManager().checkAdminLogin(usr,pass);
    }

    /*  function:  getManager()
     *  gets a manager based on a selection, we are using this to abstract the manager classes in the API,
     *  @return Manager
     */
    public static Manager getManager(sel s){
        switch(s){
            case NODE:
                return getNodeManager();
            case EDGE:
                return getEdgeManager();
            case EMPLOYEE:
                return getEmpManager();
            case InternalTransportation:
                return getIntTransportManager();
            case ExternalTransportation:
                return getExtTransportManager();
            case FloralDelivery:
                return getFloralDeliveryManager();
            case FoodDelivery:
                return getFoodDeliveryManager();
            case SanitationServices:
                return getSanitationServices();
            case Emergency:
                return getEmergencyManager();
            case ReligiousRequest:
                return getReligiousManager();
            case LanguageInterperter:
                return getLanguageInterpreterManager();
            case Medicine:
                return getMedicineRequestManager();
            case Laundry:
                return getLaundryManager();
            default:
                System.out.println("No Manager Found");
                return null;
        }
    }

    // ==== Private DB methods ==== //

    private static void loadFromCSV(sel s){
        try{
            getManager(s).loadFromCSV();
        }catch(NullPointerException e){
            e.printStackTrace();
        }
    }

    /*  function:  saveElements()
     *  Saves database of sel table to selected managers current CSV path
     *  @return void
     */
    private static void saveElements(sel s){
        try {
            getManager(s).saveElements();
        }catch(FileNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    /*  function:  cleanTable()
     *  deletes all elements from selected manager's table
     *  @return Employee
     */
    private static void cleanTable(sel s){
        try {
            getManager(s).cleanTable();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    /*  function:  setCSV_path()
     *  updates selected manager's csv path
     *  @return Employee
     */
    private static void setCSV_path(sel s, String path){
        getManager(s).setCSV_path(path);
    }

    // ==== getters and setters ===== //
    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        DatabaseManager.connection = connection;
    }

    public static EmployeeManager getEmpManager() {
        if(empManager == null){
            empManager = new EmployeeManager(connection,null);
        }
        return empManager;
    }

    public static NodeManager getNodeManager() {
        if(nodeManager == null){
            nodeManager = new NodeManager(connection, null);
        }
        return nodeManager;
    }

    public static EdgeManager getEdgeManager() {
        if(edgeManager == null){
            edgeManager = new EdgeManager(connection, null);
        }
        return edgeManager;
    }

    public static ServiceMap getServiceMap() {
        if(serviceMap == null){
            serviceMap = new ServiceMap();
        }
        return serviceMap;
    }

    public static EmergencyManager getEmergencyManager() {
        if(emergencyManager == null){
            emergencyManager = new EmergencyManager(connection, null);
        }
        return emergencyManager;
    }

    public static ExtTransportManager getExtTransportManager() {
        if(extTransportManager == null){
            extTransportManager = new ExtTransportManager(connection, null);
        }
        return extTransportManager;
    }

    public static IntTransportManager getIntTransportManager() {
        if(intTransportManager == null){
            intTransportManager = new IntTransportManager(connection, null);
        }
        return intTransportManager;
    }

    public static FloralDeliveryManager getFloralDeliveryManager() {
        if(floralDeliveryManager == null){
            floralDeliveryManager = new FloralDeliveryManager(connection, null);
        }
        return floralDeliveryManager;
    }

    public static FoodDeliveryManager getFoodDeliveryManager() {
        if(foodDeliveryManager == null){
            foodDeliveryManager = new FoodDeliveryManager(connection, null);
        }
        return foodDeliveryManager;
    }

    public static SanitationServiceManager getSanitationServices() {
        if(sanitationServiceManager == null){
            sanitationServiceManager = new SanitationServiceManager (connection, null);
        }
        return sanitationServiceManager;
    }

    public static ReligiousManager getReligiousManager() {
        if(religiousManager == null){
            religiousManager = new ReligiousManager(connection, null);
        }
        return religiousManager;
    }

    public static MedicineRequestManager getMedicineRequestManager() {
        if(medicineRequestManager == null){
            medicineRequestManager = new MedicineRequestManager(connection, null);
        }
        return medicineRequestManager;
    }

    public static LaundryManager getLaundryManager() {
        if(laundryManager == null){
            laundryManager = new LaundryManager(connection, null);
        }
        return laundryManager;
    }

    public static LanguageInterpreterManager getLanguageInterpreterManager() {
        if(languageInterpreterManager == null){
            languageInterpreterManager = new LanguageInterpreterManager(connection, null);
        }
        return languageInterpreterManager;
    }


}