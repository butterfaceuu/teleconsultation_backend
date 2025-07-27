package com.teleconsultation_backend.controllers;

import com.teleconsultation_backend.dtos.LoginRequest;
import com.teleconsultation_backend.dtos.PatientRegistrationRequest;
import com.teleconsultation_backend.dtos.PractitionerRegistrationRequest;
import com.teleconsultation_backend.dtos.VerificationRequest;
import com.teleconsultation_backend.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    // Endpoint pour login.component
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        try {
            Map<String, Object> response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // Endpoint pour register-patient.component
    @PostMapping("/register/patient")
    public ResponseEntity<Map<String, Object>> registerPatient(@ModelAttribute PatientRegistrationRequest request) {
        try {
            Map<String, Object> response = authService.registerPatient(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // Endpoint pour practitioner-register.component
    @PostMapping("/register/practitioner")
    public ResponseEntity<Map<String, Object>> registerPractitioner(@ModelAttribute PractitionerRegistrationRequest request) {
        try {
            Map<String, Object> response = authService.registerPractitioner(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // Vérification OTP pour tous les types d'utilisateurs
    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, Object>> verifyOTP(@RequestBody VerificationRequest verificationRequest) {
        try {
            Map<String, Object> response = authService.verifyOTP(verificationRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // Renvoyer un code OTP
    @PostMapping("/resend-otp")
    public ResponseEntity<Map<String, String>> resendOTP(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String type = request.get("type");
            authService.resendOTP(email, type);
            return ResponseEntity.ok(Map.of("message", "Code OTP renvoyé"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // Logout
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");
            authService.logout(token);
            return ResponseEntity.ok(Map.of("message", "Déconnexion réussie"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // Vérifier si un email existe déjà
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmailExists(@RequestParam String email) {
        boolean exists = authService.emailExists(email);
        return ResponseEntity.ok(Map.of("exists", exists));
    }
}
