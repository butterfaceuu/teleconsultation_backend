package com.teleconsultation_backend.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class VerificationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String code;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private boolean used = false;
    private String type; // 'registration' or 'login'

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public java.time.LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; }

    public java.time.LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(java.time.LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public boolean isUsed() { return used; }
    public void setUsed(boolean used) { this.used = used; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
} 