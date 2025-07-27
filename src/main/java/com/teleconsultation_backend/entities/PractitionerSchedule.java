package com.teleconsultation_backend.entities;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
public class PractitionerSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "practitioner_id", nullable = false)
    private Practitioner practitioner;
    
    private String dayOfWeek; // "MONDAY", "TUESDAY", etc.
    
    private LocalTime startTime;
    private LocalTime endTime;
    
    private String scheduleType; // "IN_PERSON", "ONLINE", "BOTH"
    
    private String location; // Pour les consultations en présentiel
    
    private boolean active = true;
    
    // Constructors
    public PractitionerSchedule() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Practitioner getPractitioner() { return practitioner; }
    public void setPractitioner(Practitioner practitioner) { this.practitioner = practitioner; }
    
    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    
    public String getScheduleType() { return scheduleType; }
    public void setScheduleType(String scheduleType) { this.scheduleType = scheduleType; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
