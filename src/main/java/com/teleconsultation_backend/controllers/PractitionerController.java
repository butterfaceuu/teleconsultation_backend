package com.teleconsultation_backend.controllers;

import com.teleconsultation_backend.dtos.PractitionerRegistrationRequest;
import com.teleconsultation_backend.entities.Practitioner;
import com.teleconsultation_backend.services.PractitionerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/practitioners")
public class PractitionerController {
    @Autowired
    private PractitionerService practitionerService;

    @PostMapping("/register")
    public ResponseEntity<?> registerPractitioner(@ModelAttribute PractitionerRegistrationRequest request) {
        try {
            Practitioner practitioner = practitionerService.registerPractitioner(request);
            return ResponseEntity.ok(practitioner);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPractitioner(@RequestParam String email, @RequestParam String code) {
        boolean verified = practitionerService.verifyPractitioner(email, code);
        if (verified) {
            return ResponseEntity.ok("Practitioner verified successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired code");
        }
    }

    @GetMapping
    public List<Practitioner> getAllPractitioners() {
        return practitionerService.getAllPractitioners();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPractitionerById(@PathVariable Long id) {
        Optional<Practitioner> practitioner = practitionerService.getPractitionerById(id);
        return practitioner.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<?> activatePractitioner(@PathVariable Long id) {
        boolean activated = practitionerService.activatePractitioner(id);
        if (activated) {
            return ResponseEntity.ok("Practitioner activated");
        } else {
            return ResponseEntity.badRequest().body("Practitioner not found");
        }
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivatePractitioner(@PathVariable Long id) {
        boolean deactivated = practitionerService.deactivatePractitioner(id);
        if (deactivated) {
            return ResponseEntity.ok("Practitioner deactivated");
        } else {
            return ResponseEntity.badRequest().body("Practitioner not found");
        }
    }

    @GetMapping("/{id}/card")
    public ResponseEntity<?> downloadProfessionalCard(@PathVariable Long id) {
        Optional<byte[]> fileOpt = practitionerService.getProfessionalCardFile(id);
        if (fileOpt.isPresent()) {
            byte[] file = fileOpt.get();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=professional_card_" + id)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(file);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
} 