package com.teleconsultation_backend.dtos;

import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

public class PatientRegistrationRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String birthCity;
    private String gender;
    private String note;
    private String source;
    private String insurance;
    private String password;
    private MultipartFile nationalCard;

    // Getters and setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public java.time.LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(java.time.LocalDate birthDate) { this.birthDate = birthDate; }

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

    public org.springframework.web.multipart.MultipartFile getNationalCard() { return nationalCard; }
    public void setNationalCard(org.springframework.web.multipart.MultipartFile nationalCard) { this.nationalCard = nationalCard; }
} 