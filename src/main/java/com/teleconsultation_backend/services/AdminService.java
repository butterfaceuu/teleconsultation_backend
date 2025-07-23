package com.teleconsultation_backend.services;

import com.teleconsultation_backend.entities.Admin;
import com.teleconsultation_backend.entities.Patient;
import com.teleconsultation_backend.entities.Practitioner;
import com.teleconsultation_backend.repositories.AdminRepository;
import com.teleconsultation_backend.repositories.PatientRepository;
import com.teleconsultation_backend.repositories.PractitionerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private PractitionerRepository practitionerRepository;

    public Optional<Admin> login(String email, String password) {
        Optional<Admin> adminOpt = adminRepository.findByEmail(email);
        if (adminOpt.isPresent() && adminOpt.get().getPassword().equals(password)) {
            return adminOpt;
        }
        return Optional.empty();
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public List<Practitioner> getAllPractitioners() {
        return practitionerRepository.findAll();
    }
} 