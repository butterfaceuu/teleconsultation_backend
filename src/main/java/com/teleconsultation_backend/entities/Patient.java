package com.teleconsultation_backend.entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String birthCity;
    private String gender;
    private String note;
    private String source; // region
    private String insurance;
    private String password;

    @Lob
    private byte[] nationalCard;
    private String nationalCardFileName;
    private String nationalCardFileType;

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

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

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

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public byte[] getNationalCard() { return nationalCard; }
    public void setNationalCard(byte[] nationalCard) { this.nationalCard = nationalCard; }

    public String getNationalCardFileName() { return nationalCardFileName; }
    public void setNationalCardFileName(String nationalCardFileName) { this.nationalCardFileName = nationalCardFileName; }

    public String getNationalCardFileType() { return nationalCardFileType; }
    public void setNationalCardFileType(String nationalCardFileType) { this.nationalCardFileType = nationalCardFileType; }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }
} 