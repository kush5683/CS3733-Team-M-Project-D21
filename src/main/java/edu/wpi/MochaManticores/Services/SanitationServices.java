package edu.wpi.MochaManticores.Services;

public class SanitationServices extends ServiceRequest {
    private final String location;
    private final String safetyHazards;
    private final String sanitationType;
    private final String equipmentNeeded;
    private final String description;

    public SanitationServices(String RequestID, String employee, boolean completed, String location, String safetyHazards,
                              String sanitationType, String equipmentNeeded, String description) {
        super(employee, completed, RequestID);
        if(RequestID.equals("")){
            this.RequestID = generateRequestID(ServiceRequestType.SanitationServices);
        }
        this.location = location;
        this.safetyHazards = safetyHazards;
        this.sanitationType = sanitationType;
        this.equipmentNeeded = equipmentNeeded;
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public String getSafetyHazards() {
        return safetyHazards;
    }

    public String getSanitationType() {
        return sanitationType;
    }

    public String getEquipmentNeeded() {
        return equipmentNeeded;
    }

    public String getDescription() {
        return description;
    }

}
