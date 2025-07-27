package com.teleconsultation_backend.dtos;

public class VerificationRequest {
    private String email;
    private String code;
    private String type; // "PATIENT_REGISTRATION", "PRACTITIONER_REGISTRATION", "ADMIN_LOGIN"
    
    // Constructors
    public VerificationRequest() {}
    
    public VerificationRequest(String email, String code, String type) {
        this.email = email;
        this.code = code;
        this.type = type;
    }
    
    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
