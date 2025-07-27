package com.teleconsultation_backend.controllers;

import com.teleconsultation_backend.services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    
    @Autowired
    private DashboardService dashboardService;
    
    // Pour patient-dashboard.component
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Map<String, Object>> getPatientDashboard(@PathVariable Long patientId) {
        try {
            Map<String, Object> dashboardData = dashboardService.getPatientDashboard(patientId);
            return ResponseEntity.ok(dashboardData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // Pour praticien-dashboard.component
    @GetMapping("/practitioner/{practitionerId}")
    public ResponseEntity<Map<String, Object>> getPractitionerDashboard(@PathVariable Long practitionerId) {
        try {
            Map<String, Object> dashboardData = dashboardService.getPractitionerDashboard(practitionerId);
            return ResponseEntity.ok(dashboardData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // Pour admin-dashboard.component
    @GetMapping("/admin")
    public ResponseEntity<Map<String, Object>> getAdminDashboard() {
        try {
            Map<String, Object> dashboardData = dashboardService.getAdminDashboard();
            return ResponseEntity.ok(dashboardData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // Endpoints spécifiques pour les actions des dashboards
    
    // ===== PATIENT ACTIONS =====
    
    @PostMapping("/patient/{patientId}/cancel-appointment/{appointmentId}")
    public ResponseEntity<Map<String, String>> cancelAppointment(
            @PathVariable Long patientId, 
            @PathVariable Long appointmentId) {
        try {
            // Logique pour annuler un rendez-vous
            return ResponseEntity.ok(Map.of("message", "Rendez-vous annulé avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/patient/{patientId}/send-message")
    public ResponseEntity<Map<String, String>> sendPatientMessage(
            @PathVariable Long patientId,
            @RequestBody Map<String, Object> messageData) {
        try {
            // Logique pour envoyer un message
            return ResponseEntity.ok(Map.of("message", "Message envoyé"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/patient/{patientId}/payment")
    public ResponseEntity<Map<String, Object>> processPayment(
            @PathVariable Long patientId,
            @RequestBody Map<String, Object> paymentData) {
        try {
            // Simulation du paiement
            Double amount = Double.valueOf(paymentData.get("amount").toString());
            String cardNumber = paymentData.get("cardNumber").toString();
            
            if (amount > 0 && cardNumber.length() == 16) {
                return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Paiement de " + amount + " MAD effectué avec succès !",
                    "transactionId", "TXN_" + System.currentTimeMillis()
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Erreur dans les informations de paiement."
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "status", "error",
                "message", e.getMessage()
            ));
        }
    }
    
    // ===== PRACTITIONER ACTIONS =====
    
    @PostMapping("/practitioner/{practitionerId}/send-message")
    public ResponseEntity<Map<String, String>> sendPractitionerMessage(
            @PathVariable Long practitionerId,
            @RequestBody Map<String, Object> messageData) {
        try {
            // Logique pour envoyer un message entre praticiens
            return ResponseEntity.ok(Map.of("message", "Message envoyé"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/practitioner/{practitionerId}/start-call")
    public ResponseEntity<Map<String, Object>> startCall(
            @PathVariable Long practitionerId,
            @RequestBody Map<String, Object> callData) {
        try {
            String callType = callData.get("type").toString(); // "video" ou "audio"
            Long contactId = Long.valueOf(callData.get("contactId").toString());
            
            // Logique pour démarrer un appel
            return ResponseEntity.ok(Map.of(
                "callId", "CALL_" + System.currentTimeMillis(),
                "type", callType,
                "contactId", contactId,
                "status", "initiated",
                "message", "Appel " + callType + " démarré"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/practitioner/{practitionerId}/add-appointment")
    public ResponseEntity<Map<String, String>> addAppointment(
            @PathVariable Long practitionerId,
            @RequestBody Map<String, Object> appointmentData) {
        try {
            // Logique pour ajouter un rendez-vous
            return ResponseEntity.ok(Map.of("message", "Rendez-vous ajouté avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // ===== ADMIN ACTIONS =====
    
    @PostMapping("/admin/validate-practitioner/{practitionerId}")
    public ResponseEntity<Map<String, String>> validatePractitioner(
            @PathVariable Long practitionerId,
            @RequestBody Map<String, String> actionData) {
        try {
            String action = actionData.get("action"); // "validate" ou "reject"
            
            if ("validate".equals(action)) {
                // Logique pour valider le praticien
                return ResponseEntity.ok(Map.of("message", "Praticien validé avec succès"));
            } else {
                // Logique pour rejeter le praticien
                return ResponseEntity.ok(Map.of("message", "Praticien rejeté"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/admin/moderate-message/{messageId}")
    public ResponseEntity<Map<String, String>> moderateMessage(
            @PathVariable Long messageId,
            @RequestBody Map<String, String> actionData) {
        try {
            String action = actionData.get("action"); // "warn", "delete", "restore"
            
            switch (action) {
                case "warn":
                    return ResponseEntity.ok(Map.of("message", "Avertissement envoyé"));
                case "delete":
                    return ResponseEntity.ok(Map.of("message", "Message supprimé"));
                case "restore":
                    return ResponseEntity.ok(Map.of("message", "Message restauré"));
                default:
                    return ResponseEntity.badRequest().body(Map.of("error", "Action non reconnue"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/admin/resolve-report/{reportId}")
    public ResponseEntity<Map<String, String>> resolveReport(@PathVariable Long reportId) {
        try {
            // Logique pour résoudre une réclamation
            return ResponseEntity.ok(Map.of("message", "Réclamation marquée comme résolue"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/admin/contact-user/{reportId}")
    public ResponseEntity<Map<String, String>> contactUser(@PathVariable Long reportId) {
        try {
            // Logique pour contacter un utilisateur
            return ResponseEntity.ok(Map.of("message", "Contact initié avec l'utilisateur"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
