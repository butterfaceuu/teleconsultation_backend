package com.teleconsultation_backend.dtos;

import java.time.LocalDateTime;

public class AppointmentRequest {
    private Long patientId;
    private Long practitionerId;
    private LocalDateTime appointmentDateTime;
    private String type; // ONLINE, IN_PERSON
    private String consultationType; // VIDEO, AUDIO, CHAT
    private String location;
    private String notes;
    private Double price;
    
    // Constructors
    public AppointmentRequest() {}
    
    // Getters and Setters
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    
    public Long getPractitionerId() { return practitionerId; }
    public void setPractitionerId(Long practitionerId) { this.practitionerId = practitionerId; }
    
    public LocalDateTime getAppointmentDateTime() { return appointmentDateTime; }
    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) { this.appointmentDateTime = appointmentDateTime; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getConsultationType() { return consultationType; }
    public void setConsultationType(String consultationType) { this.consultationType = consultationType; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}
