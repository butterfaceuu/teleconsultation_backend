package com.teleconsultation_backend.dtos;

public class PractitionerValidationRequest {
    private Long practitionerId;
    private String action; // "ACCEPT" ou "REJECT"
    private String reason; // Raison en cas de rejet
    private String adminEmail;
    
    public Long getPractitionerId() {
        return this.practitionerId;
    }
    
    public void setPractitionerId(Long practitionerId) {
        this.practitionerId = practitionerId;
    }
    
    public String getAction() {
        return this.action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public String getReason() {
        return this.reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public String getAdminEmail() {
        return this.adminEmail;
    }
    
    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }
}
