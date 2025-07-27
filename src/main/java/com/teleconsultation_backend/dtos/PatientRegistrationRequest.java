package com.teleconsultation_backend.dtos;

import org.springframework.web.multipart.MultipartFile;

public class PatientRegistrationRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone; // 9 digits
    private String birthDate; // Date de naissance
    private String birthCity; // Ville de naissance
    private String gender; // "F" ou "H"
    private String note; // Remarque (optionnel)
    private String source; // Région
    private String insurance; // Assurance (optionnel)
    private String password;
    private MultipartFile file; // Carte nationale
    
    // Constructors
    public PatientRegistrationRequest() {}
    
    // Getters and Setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
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
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public MultipartFile getFile() { return file; }
    public void setFile(MultipartFile file) { this.file = file; }
}
