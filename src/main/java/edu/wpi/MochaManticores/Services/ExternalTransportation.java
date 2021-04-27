package edu.wpi.MochaManticores.Services;

public class ExternalTransportation extends ServiceRequest{
    private String patientRoom;
    private String currentRoom;
    private String externalRoom;
    private String transportationMethod;

    public ExternalTransportation(String RequestID, String employee, boolean completed, String patientRoom, String currentRoom,
                                  String externalRoom, String transportationMethod) {
        super(employee, completed, RequestID);
        if(RequestID.equals("")){
            this.RequestID = generateRequestID(ServiceRequestType.ExternalTransportation);
        }
        this.patientRoom = patientRoom;
        this.currentRoom = currentRoom;
        this.externalRoom = externalRoom;
        this.transportationMethod = transportationMethod;
    }

    public String getPatientRoom() {
        return patientRoom;
    }

    public String getCurrentRoom() {
        return currentRoom;
    }

    public String getExternalRoom() {
        return externalRoom;
    }

    public String getTransportationMethod() {
        return transportationMethod;
    }
}