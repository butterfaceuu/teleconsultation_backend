package com.teleconsultation_backend.controllers;

import com.teleconsultation_backend.entities.Domain;
import com.teleconsultation_backend.entities.Specialty;
import com.teleconsultation_backend.entities.Practitioner;
import com.teleconsultation_backend.services.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/domains")
public class DomainController {
    
    @Autowired
    private DomainService domainService;
    
    // Correspond à votre navigation frontend par domaines
    @GetMapping
    public ResponseEntity<List<Domain>> getAllDomains() {
        List<Domain> domains = domainService.getAllActiveDomains();
        return ResponseEntity.ok(domains);
    }
    
    // Pour health-domain.component
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getHealthDomain() {
        Map<String, Object> healthData = domainService.getDomainWithSpecialties("Santé");
        return ResponseEntity.ok(healthData);
    }
    
    // Pour law-domain.component
    @GetMapping("/law")
    public ResponseEntity<Map<String, Object>> getLawDomain() {
        Map<String, Object> lawData = domainService.getDomainWithSpecialties("Droit");
        return ResponseEntity.ok(lawData);
    }
    
    // Pour esthetic-domain.component
    @GetMapping("/esthetic")
    public ResponseEntity<Map<String, Object>> getEstheticDomain() {
        Map<String, Object> estheticData = domainService.getDomainWithSpecialties("Esthétique");
        return ResponseEntity.ok(estheticData);
    }
    
    // Pour bienetre-domain.component
    @GetMapping("/bienetre")
    public ResponseEntity<Map<String, Object>> getBienEtreDomain() {
        Map<String, Object> bienEtreData = domainService.getDomainWithSpecialties("Bien-être");
        return ResponseEntity.ok(bienEtreData);
    }
    
    // Pour education-domain.component
    @GetMapping("/education")
    public ResponseEntity<Map<String, Object>> getEducationDomain() {
        Map<String, Object> educationData = domainService.getDomainWithSpecialties("Éducation");
        return ResponseEntity.ok(educationData);
    }
    
    // Pour decor-domain.component
    @GetMapping("/decor")
    public ResponseEntity<Map<String, Object>> getDecorDomain() {
        Map<String, Object> decorData = domainService.getDomainWithSpecialties("Décoration");
        return ResponseEntity.ok(decorData);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Domain> getDomainById(@PathVariable Long id) {
        Domain domain = domainService.getDomainById(id);
        if (domain != null) {
            return ResponseEntity.ok(domain);
        }
        return ResponseEntity.notFound().build();
    }
    
    // Pour obtenir les spécialités d'un domaine
    @GetMapping("/{domainName}/specialties")
    public ResponseEntity<List<Specialty>> getDomainSpecialties(@PathVariable String domainName) {
        List<Specialty> specialties = domainService.getSpecialtiesByDomainName(domainName);
        return ResponseEntity.ok(specialties);
    }
    
    // Pour obtenir les praticiens d'une spécialité
    @GetMapping("/{domainName}/specialties/{specialtyName}/practitioners")
    public ResponseEntity<List<Practitioner>> getSpecialtyPractitioners(
            @PathVariable String domainName, 
            @PathVariable String specialtyName) {
        List<Practitioner> practitioners = domainService.getPractitionersBySpecialty(domainName, specialtyName);
        return ResponseEntity.ok(practitioners);
    }
    
    // Pour la recherche (utilisé par le chatbot et la navigation)
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchDomains(@RequestParam String query) {
        Map<String, Object> searchResults = domainService.searchDomains(query);
        return ResponseEntity.ok(searchResults);
    }
    
    @PostMapping
    public ResponseEntity<Domain> createDomain(@RequestBody Domain domain) {
        Domain createdDomain = domainService.createDomain(domain);
        return ResponseEntity.ok(createdDomain);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Domain> updateDomain(@PathVariable Long id, @RequestBody Domain domain) {
        Domain updatedDomain = domainService.updateDomain(id, domain);
        if (updatedDomain != null) {
            return ResponseEntity.ok(updatedDomain);
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDomain(@PathVariable Long id) {
        boolean deleted = domainService.deleteDomain(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
