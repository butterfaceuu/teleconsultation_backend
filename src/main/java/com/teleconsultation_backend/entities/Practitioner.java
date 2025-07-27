package com.teleconsultation_backend.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "practitioners")
public class Practitioner {
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
    private String specialty; // Domaine et spécialité
    
    @Column(nullable = false)
    private String phone; // Format: 9 digits (sans +212)
    
    @Column(nullable = false)
    private String gender; // "F" ou "H"
    
    @Column(nullable = false)
    private String source; // Région (address)
    
    // Relation avec Specialty (optionnel pour une structure plus avancée)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialty_id")
    private Specialty specialtyEntity;

    // Champs additionnels pour le profil
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private String location; // Localisation précise
    private Double consultationPrice;
    private boolean messagingEnabled = true;

    // Champs pour la carte professionnelle
    private String professionalCardUrl; // URL du fichier uploadé
    private String professionalCardFileName; // Nom original du fichier
    
    @Lob
    private byte[] professionalCard; // Données binaires de la carte

    // Champs de gestion
    private boolean isVerified = false; // Validation par admin
    private boolean isActive = true;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;

    // Relations
    @OneToMany(mappedBy = "practitioner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "practitioner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PractitionerSchedule> schedules;

    @OneToMany(mappedBy = "senderPractitioner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> sentMessages;

    @OneToMany(mappedBy = "receiverPractitioner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> receivedMessages;

    // Constructors
    public Practitioner() {
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

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public Specialty getSpecialtyEntity() { return specialtyEntity; }
    public void setSpecialtyEntity(Specialty specialtyEntity) { this.specialtyEntity = specialtyEntity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Double getConsultationPrice() { return consultationPrice; }
    public void setConsultationPrice(Double consultationPrice) { this.consultationPrice = consultationPrice; }

    public boolean isMessagingEnabled() { return messagingEnabled; }
    public void setMessagingEnabled(boolean messagingEnabled) { this.messagingEnabled = messagingEnabled; }

    public String getProfessionalCardUrl() { return professionalCardUrl; }
    public void setProfessionalCardUrl(String professionalCardUrl) { this.professionalCardUrl = professionalCardUrl; }

    public String getProfessionalCardFileName() { return professionalCardFileName; }
    public void setProfessionalCardFileName(String professionalCardFileName) { this.professionalCardFileName = professionalCardFileName; }

    public byte[] getProfessionalCard() { return professionalCard; }
    public void setProfessionalCard(byte[] professionalCard) { this.professionalCard = professionalCard; }

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

    public List<PractitionerSchedule> getSchedules() { return schedules; }
    public void setSchedules(List<PractitionerSchedule> schedules) { this.schedules = schedules; }

    public List<Message> getSentMessages() { return sentMessages; }
    public void setSentMessages(List<Message> sentMessages) { this.sentMessages = sentMessages; }

    public List<Message> getReceivedMessages() { return receivedMessages; }
    public void setReceivedMessages(List<Message> receivedMessages) { this.receivedMessages = receivedMessages; }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
