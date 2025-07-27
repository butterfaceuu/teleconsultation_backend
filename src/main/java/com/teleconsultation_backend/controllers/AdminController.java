package com.teleconsultation_backend.controllers;

import com.teleconsultation_backend.dtos.PractitionerValidationRequest;
import com.teleconsultation_backend.dtos.AdminStatsResponse;
import com.teleconsultation_backend.services.AdminService;
import com.teleconsultation_backend.dtos.LoginRequest;
import com.teleconsultation_backend.dtos.VerificationRequest;
import com.teleconsultation_backend.entities.Patient;
import com.teleconsultation_backend.entities.Practitioner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        try {
            Map<String, Object> response = adminService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, Object>> verifyOTP(@RequestBody VerificationRequest verificationRequest) {
        try {
            Map<String, Object> response = adminService.verifyOTP(verificationRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/dashboard/stats")
    public ResponseEntity<AdminStatsResponse> getDashboardStats() {
        try {
            AdminStatsResponse stats = adminService.getDashboardStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/practitioners/pending")
    public ResponseEntity<List<Practitioner>> getPendingPractitioners() {
        try {
            List<Practitioner> practitioners = adminService.getPendingPractitioners();
            return ResponseEntity.ok(practitioners);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/practitioners/{id}")
    public ResponseEntity<Practitioner> getPractitionerById(@PathVariable Long id) {
        try {
            Practitioner practitioner = adminService.getPractitionerById(id);
            if (practitioner != null) {
                return ResponseEntity.ok(practitioner);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/practitioners/validate")
    public ResponseEntity<Map<String, String>> validatePractitioner(@RequestBody PractitionerValidationRequest request) {
        try {
            String result = adminService.validatePractitioner(request);
            return ResponseEntity.ok(Map.of("message", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/practitioners")
    public ResponseEntity<List<Practitioner>> getAllPractitioners() {
        try {
            List<Practitioner> practitioners = adminService.getAllPractitioners();
            return ResponseEntity.ok(practitioners);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> getAllPatients() {
        try {
            List<Patient> patients = adminService.getAllPatients();
            return ResponseEntity.ok(patients);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/practitioners/{id}/status")
    public ResponseEntity<Map<String, String>> updatePractitionerStatus(
            @PathVariable Long id, 
            @RequestParam String status) {
        try {
            String result = adminService.updatePractitionerStatus(id, status);
            return ResponseEntity.ok(Map.of("message", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/patients/{id}/status")
    public ResponseEntity<Map<String, String>> updatePatientStatus(
            @PathVariable Long id, 
            @RequestParam String status) {
        try {
            String result = adminService.updatePatientStatus(id, status);
            return ResponseEntity.ok(Map.of("message", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/practitioners/{id}/professional-card")
    public ResponseEntity<byte[]> getPractitionerProfessionalCard(@PathVariable Long id) {
        try {
            byte[] cardData = adminService.getPractitionerProfessionalCard(id);
            if (cardData != null) {
                return ResponseEntity.ok()
                    .header("Content-Type", "application/octet-stream")
                    .body(cardData);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/patients/{id}/national-card")
    public ResponseEntity<byte[]> getPatientNationalCard(@PathVariable Long id) {
        try {
            byte[] cardData = adminService.getPatientNationalCard(id);
            if (cardData != null) {
                return ResponseEntity.ok()
                    .header("Content-Type", "application/octet-stream")
                    .body(cardData);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/reports/monthly")
    public ResponseEntity<Map<String, Object>> getMonthlyReport() {
        try {
            Map<String, Object> report = adminService.getMonthlyReport();
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/send-notification")
    public ResponseEntity<Map<String, String>> sendNotification(@RequestBody Map<String, Object> request) {
        try {
            String result = adminService.sendNotification(request);
            return ResponseEntity.ok(Map.of("message", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
