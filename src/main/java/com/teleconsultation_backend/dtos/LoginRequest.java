package com.teleconsultation_backend.dtos;

public class LoginRequest {
    private String email;
    private String password;
    private String profile; // patient, practitioner, admin

    // Getters and setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getProfile() { return profile; }
    public void setProfile(String profile) { this.profile = profile; }
} 