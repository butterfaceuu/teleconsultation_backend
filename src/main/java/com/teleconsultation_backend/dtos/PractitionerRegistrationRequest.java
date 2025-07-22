package com.teleconsultation_backend.dtos;

import org.springframework.web.multipart.MultipartFile;

public class PractitionerRegistrationRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String specialty;
    private String phone;
    private String gender;
    private String source;
    private String password;
    private MultipartFile professionalCard;

    // Getters and setters
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

    public org.springframework.web.multipart.MultipartFile getProfessionalCard() { return professionalCard; }
    public void setProfessionalCard(org.springframework.web.multipart.MultipartFile professionalCard) { this.professionalCard = professionalCard; }
} 