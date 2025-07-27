package com.teleconsultation_backend.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "replacements")
public class Replacement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "original_practitioner_id", nullable = false)
    private Practitioner originalPractitioner;
    
    @ManyToOne
    @JoinColumn(name = "replacement_practitioner_id", nullable = false)
    private Practitioner replacementPractitioner;
    
    @Column(nullable = false)
    private LocalDateTime startDate;
    
    @Column(nullable = false)
    private LocalDateTime endDate;
    
    private String reason;
    
    @Enumerated(EnumType.STRING)
    private ReplacementStatus status; // PENDING, ACCEPTED, REJECTED, ACTIVE, COMPLETED
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime acceptedAt;
    
    // Constructors
    public Replacement() {
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Practitioner getOriginalPractitioner() { return originalPractitioner; }
    public void setOriginalPractitioner(Practitioner originalPractitioner) { this.originalPractitioner = originalPractitioner; }
    
    public Practitioner getReplacementPractitioner() { return replacementPractitioner; }
    public void setReplacementPractitioner(Practitioner replacementPractitioner) { this.replacementPractitioner = replacementPractitioner; }
    
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    
    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    
    public ReplacementStatus getStatus() { return status; }
    public void setStatus(ReplacementStatus status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getAcceptedAt() { return acceptedAt; }
    public void setAcceptedAt(LocalDateTime acceptedAt) { this.acceptedAt = acceptedAt; }
}
