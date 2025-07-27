package com.teleconsultation_backend.controllers;

import com.teleconsultation_backend.services.DomainService;
import com.teleconsultation_backend.entities.Domain;
import com.teleconsultation_backend.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/home")
public class HomeController {
    
    @Autowired
    private DomainService domainService;
    
    @Autowired
    private AdminService adminService;
    
    // Obtenir tous les domaines pour la page d'accueil
    @GetMapping("/domains")
    public ResponseEntity<List<Map<String, Object>>> getHomeDomains() {
        try {
            List<Domain> domains = domainService.getAllActiveDomains();
            List<Map<String, Object>> domainData = new ArrayList<>();
            
            for (Domain domain : domains) {
                Map<String, Object> domainInfo = new HashMap<>();
                domainInfo.put("id", domain.getId());
                domainInfo.put("name", domain.getName());
                domainInfo.put("description", domain.getDescription());
                domainInfo.put("icon", getIconForDomain(domain.getName()));
                domainInfo.put("routeId", getRouteIdForDomain(domain.getName()));
                domainInfo.put("practitionersCount", getPractitionersCountForDomain(domain.getId()));
                domainData.add(domainInfo);
            }
            
            return ResponseEntity.ok(domainData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ArrayList<>());
        }
    }
    
    // Obtenir les statistiques pour la page d'accueil
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getHomeStats() {
        try {
            Map<String, Object> stats = adminService.getGlobalStats();
            
            // Formater les statistiques pour la page d'accueil
            Map<String, Object> homeStats = new HashMap<>();
            homeStats.put("practitioners", stats.getOrDefault("totalPractitioners", 1500));
            homeStats.put("consultations", stats.getOrDefault("totalAppointments", 30000));
            homeStats.put("domains", 6); // Nos 6 domaines
            homeStats.put("satisfaction", "4.9"); // Note de satisfaction
            
            return ResponseEntity.ok(homeStats);
        } catch (Exception e) {
            // Statistiques par défaut si erreur
            Map<String, Object> defaultStats = Map.of(
                "practitioners", 1500,
                "consultations", 30000,
                "domains", 6,
                "satisfaction", "4.9"
            );
            return ResponseEntity.ok(defaultStats);
        }
    }
    
    // Obtenir les témoignages
    @GetMapping("/testimonials")
    public ResponseEntity<List<Map<String, Object>>> getTestimonials() {
        try {
            List<Map<String, Object>> testimonials = Arrays.asList(
                createTestimonial(
                    "J'ai trouvé un excellent dermatologue en quelques minutes. La plateforme est très intuitive !",
                    "Sophie Martin",
                    "Patiente",
                    "/images/sopihie.jpeg"
                ),
                createTestimonial(
                    "Cette plateforme me permet de gérer facilement mon planning et mes consultations.",
                    "Dr. El Yamani Khalid",
                    "Cardiologue",
                    "/images/khalid.jpeg"
                ),
                createTestimonial(
                    "Messagerie sécurisée très utile pour échanger avec mes patients entre les consultations.",
                    "Dr. Nezha Elhattab",
                    "Pédiatre",
                    "/images/nezha.jpeg"
                ),
                createTestimonial(
                    "Interface moderne et paiement sécurisé. Je recommande vivement !",
                    "Amina Benali",
                    "Patiente",
                    "/images/amina.jpeg"
                ),
                createTestimonial(
                    "Excellent service client et professionnels qualifiés dans tous les domaines.",
                    "Youssef Alaoui",
                    "Utilisateur",
                    "/images/youssef.jpg"
                ),
                createTestimonial(
                    "La téléconsultation fonctionne parfaitement. Très pratique pour mes patients.",
                    "Dr. Fatima Zahra",
                    "Dermatologue",
                    "/images/fatima.jpeg"
                )
            );
            
            return ResponseEntity.ok(testimonials);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ArrayList<>());
        }
    }
    
    // Contact form submission
    @PostMapping("/contact")
    public ResponseEntity<Map<String, String>> submitContactForm(@RequestBody Map<String, String> contactData) {
        try {
            String name = contactData.get("name");
            String email = contactData.get("email");
            String message = contactData.get("message");
            
            // Ici vous pouvez ajouter la logique pour envoyer l'email
            // ou sauvegarder le message en base de données
            
            // Pour l'instant, on simule juste le succès avec les données reçues
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Votre message a été envoyé avec succès. Nous vous répondrons dans les plus brefs délais.",
                "senderName", name,
                "senderEmail", email,
                "messageContent", message
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "status", "error",
                "message", "Erreur lors de l'envoi du message. Veuillez réessayer."
            ));
        }
    }
    
    // Recherche globale depuis la page d'accueil
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> globalSearch(@RequestParam String query) {
        try {
            Map<String, Object> results = domainService.searchDomains(query);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // ===== MÉTHODES UTILITAIRES =====
    
    private String getIconForDomain(String domainName) {
        switch (domainName.toLowerCase()) {
            case "santé":
                return "fa fa-heartbeat";
            case "droit":
                return "fa fa-balance-scale";
            case "esthétique":
                return "fa fa-scissors";
            case "bien-être":
                return "fa fa-leaf";
            case "éducation":
                return "fa fa-graduation-cap";
            case "décoration":
                return "fa fa-home";
            default:
                return "fa fa-circle";
        }
    }
    
    private String getRouteIdForDomain(String domainName) {
        switch (domainName.toLowerCase()) {
            case "santé":
                return "health-domain";
            case "droit":
                return "law-domain";
            case "esthétique":
                return "esthetic-domain";
            case "bien-être":
                return "bienetre-domain";
            case "éducation":
                return "education-domain";
            case "décoration":
                return "decor-domain";
            default:
                return "unknown-domain";
        }
    }
    
    private long getPractitionersCountForDomain(Long domainId) {
        // Cette méthode devrait compter les praticiens par domaine
        // Pour l'instant, on retourne des valeurs simulées
        return 50 + (domainId * 25); // Simulation
    }
    
    private Map<String, Object> createTestimonial(String text, String name, String role, String avatar) {
        Map<String, Object> testimonial = new HashMap<>();
        testimonial.put("text", text);
        testimonial.put("name", name);
        testimonial.put("role", role);
        testimonial.put("avatar", avatar);
        return testimonial;
    }
}
