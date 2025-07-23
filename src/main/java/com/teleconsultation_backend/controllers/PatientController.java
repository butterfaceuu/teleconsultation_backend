package com.teleconsultation_backend.controllers;

import com.teleconsultation_backend.dtos.PatientRegistrationRequest;
import com.teleconsultation_backend.services.PatientService;
import com.teleconsultation_backend.entities.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    @Autowired
    private PatientService patientService;

    @PostMapping("/register")
    public ResponseEntity<?> registerPatient(@ModelAttribute PatientRegistrationRequest request) {
        try {
            Patient patient = patientService.registerPatient(request);
            return ResponseEntity.ok(patient);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPatient(@RequestParam String email, @RequestParam String code) {
        boolean verified = patientService.verifyPatient(email, code);
        if (verified) {
            return ResponseEntity.ok("Patient verified successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired code");
        }
    }

    @GetMapping
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable Long id) {
        Optional<Patient> patient = patientService.getPatientById(id);
        return patient.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
} 