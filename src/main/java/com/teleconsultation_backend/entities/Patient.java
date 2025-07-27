package com.teleconsultation_backend.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String phone; // Format: 9 digits (sans +212)
    
    private String birthDate; // Date de naissance
    private String birthCity; // Ville de naissance
    
    @Column(nullable = false)
    private String gender; // "F" ou "H"
    
    @Column(columnDefinition = "TEXT")
    private String note; // Remarque
    
    @Column(nullable = false)
    private String source; // Région
    
    private String insurance; // Assurance
    
    // Champs pour la carte nationale
    private String nationalCardUrl; // URL du fichier uploadé
    private String nationalCardFileName; // Nom original du fichier
    
    @Lob
    private byte[] nationalCard; // Données binaires de la carte (optionnel)
    
    // Champs de gestion
    private boolean isVerified = false;
    private boolean isActive = true;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    // Relations
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FamilyMember> familyMembers;

    @OneToMany(mappedBy = "senderPatient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> sentMessages;

    @OneToMany(mappedBy = "receiverPatient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> receivedMessages;

    // Constructors
    public Patient() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public String getBirthCity() { return birthCity; }
    public void setBirthCity(String birthCity) { this.birthCity = birthCity; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getInsurance() { return insurance; }
    public void setInsurance(String insurance) { this.insurance = insurance; }

    public String getNationalCardUrl() { return nationalCardUrl; }
    public void setNationalCardUrl(String nationalCardUrl) { this.nationalCardUrl = nationalCardUrl; }

    public String getNationalCardFileName() { return nationalCardFileName; }
    public void setNationalCardFileName(String nationalCardFileName) { this.nationalCardFileName = nationalCardFileName; }

    public byte[] getNationalCard() { return nationalCard; }
    public void setNationalCard(byte[] nationalCard) { this.nationalCard = nationalCard; }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<Appointment> getAppointments() { return appointments; }
    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }

    public List<FamilyMember> getFamilyMembers() { return familyMembers; }
    public void setFamilyMembers(List<FamilyMember> familyMembers) { this.familyMembers = familyMembers; }

    public List<Message> getSentMessages() { return sentMessages; }
    public void setSentMessages(List<Message> sentMessages) { this.sentMessages = sentMessages; }

    public List<Message> getReceivedMessages() { return receivedMessages; }
    public void setReceivedMessages(List<Message> receivedMessages) { this.receivedMessages = receivedMessages; }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
