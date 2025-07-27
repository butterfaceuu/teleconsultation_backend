package com.teleconsultation_backend.dtos;

public class LoginRequest {
    private String profile; // "admin", "patient", "praticien"
    private String email;
    private String password;
    
    // Constructors
    public LoginRequest() {}
    
    public LoginRequest(String profile, String email, String password) {
        this.profile = profile;
        this.email = email;
        this.password = password;
    }
    
    // Getters and Setters
    public String getProfile() { return profile; }
    public void setProfile(String profile) { this.profile = profile; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
