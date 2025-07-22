package com.teleconsultation_backend.entities;

import jakarta.persistence.*;

@Entity
public class Practitioner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String specialty;
    private String phone;
    private String gender;
    private String source; // region
    private String password;

    @Lob
    private byte[] professionalCard;
    private String professionalCardFileName;
    private String professionalCardFileType;

    private boolean isVerified = false;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public byte[] getProfessionalCard() { return professionalCard; }
    public void setProfessionalCard(byte[] professionalCard) { this.professionalCard = professionalCard; }

    public String getProfessionalCardFileName() { return professionalCardFileName; }
    public void setProfessionalCardFileName(String professionalCardFileName) { this.professionalCardFileName = professionalCardFileName; }

    public String getProfessionalCardFileType() { return professionalCardFileType; }
    public void setProfessionalCardFileType(String professionalCardFileType) { this.professionalCardFileType = professionalCardFileType; }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }
} 