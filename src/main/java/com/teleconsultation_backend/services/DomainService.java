package com.teleconsultation_backend.services;

import com.teleconsultation_backend.entities.Domain;
import com.teleconsultation_backend.entities.Specialty;
import com.teleconsultation_backend.entities.Practitioner;
import com.teleconsultation_backend.repositories.DomainRepository;
import com.teleconsultation_backend.repositories.SpecialtyRepository;
import com.teleconsultation_backend.repositories.PractitionerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.time.LocalDateTime;

@Service
public class DomainService {
    
    @Autowired
    private DomainRepository domainRepository;
    
    @Autowired
    private SpecialtyRepository specialtyRepository;
    
    @Autowired
    private PractitionerRepository practitionerRepository;
    
    @PostConstruct
    public void initializeDefaultDomains() {
    
        initializeHealthDomain();
        initializeLawDomain();
        initializeEstheticDomain();
        initializeBienEtreDomain();
        initializeEducationDomain();
        initializeDecorDomain();
    }
    
    private void initializeHealthDomain() {
        Domain healthDomain = createDomainIfNotExists(
            "Santé", 
            "Professionnels de la santé et du médical", 
            "/assets/icons/health.svg"
        );
        
        if (healthDomain != null) {
           
            createSpecialtyIfNotExists("Cardiologie", "Spécialistes du cœur et des vaisseaux sanguins.", healthDomain);
            createSpecialtyIfNotExists("Dermatologie", "Soins pour la peau, les cheveux et les ongles.", healthDomain);
            createSpecialtyIfNotExists("Pédiatrie", "Médecine pour les nourrissons et les enfants.", healthDomain);
            createSpecialtyIfNotExists("Psychiatrie", "Santé mentale et troubles du comportement.", healthDomain);
        }
    }
    
    private void initializeLawDomain() {
        Domain lawDomain = createDomainIfNotExists(
            "Droit", 
            "Professionnels du droit et de la justice", 
            "/assets/icons/law.svg"
        );
        
        if (lawDomain != null) {
            // Exactement comme dans law-domain.component.ts
            createSpecialtyIfNotExists("Droit Civil", "Litiges familiaux, contrats, successions, etc.", lawDomain);
            createSpecialtyIfNotExists("Droit Pénal", "Défense en cas d'infractions et délits.", lawDomain);
            createSpecialtyIfNotExists("Droit du Travail", "Relations employeur-employé, contrats de travail.", lawDomain);
            createSpecialtyIfNotExists("Droit des Affaires", "Conseil aux entreprises, contrats commerciaux.", lawDomain);
        }
    }
    
    private void initializeEstheticDomain() {
        Domain estheticDomain = createDomainIfNotExists(
            "Esthétique", 
            "Professionnels de la beauté et de l'esthétique", 
            "/assets/icons/esthetic.svg"
        );
        
        if (estheticDomain != null) {
            // Exactement comme dans esthetic-domain.component.ts
            createSpecialtyIfNotExists("Soins Visage", "Nettoyage, peeling, hydrafacial et soins anti-âge.", estheticDomain);
            createSpecialtyIfNotExists("Épilation", "Épilation laser, à la cire et soins complémentaires.", estheticDomain);
            createSpecialtyIfNotExists("Massage", "Massages relaxants, drainants et thérapeutiques.", estheticDomain);
            createSpecialtyIfNotExists("Beauté Mains/Pieds", "Manucure, pédicure et pose de vernis semi-permanent.", estheticDomain);
        }
    }
    
    private void initializeBienEtreDomain() {
        Domain bienEtreDomain = createDomainIfNotExists(
            "Bien-être", 
            "Professionnels du bien-être et de la relaxation", 
            "/assets/icons/bienetre.svg"
        );
        
        if (bienEtreDomain != null) {
            // Exactement comme dans bienetre-domain.component.ts
            createSpecialtyIfNotExists("Coaching Personnel", "Développement personnel et confiance en soi", bienEtreDomain);
            createSpecialtyIfNotExists("Coaching Professionnel", "Accompagnement carrière et leadership", bienEtreDomain);
            createSpecialtyIfNotExists("Nutrition & Bien-être", "Programmes alimentaires équilibrés", bienEtreDomain);
            createSpecialtyIfNotExists("Méditation & Pleine Conscience", "Gestion du stress et relaxation", bienEtreDomain);
        }
    }
    
    private void initializeEducationDomain() {
        Domain educationDomain = createDomainIfNotExists(
            "Éducation", 
            "Professionnels de l'enseignement et de la formation", 
            "/assets/icons/education.svg"
        );
        
        if (educationDomain != null) {
            // Exactement comme dans education-domain.component.ts
            createSpecialtyIfNotExists("Cours Particuliers", "Soutien scolaire personnalisé toutes matières", educationDomain);
            createSpecialtyIfNotExists("Formation Professionnelle", "Formations certifiantes pour adultes", educationDomain);
            createSpecialtyIfNotExists("Langues Étrangères", "Cours de langues avec professeurs natifs", educationDomain);
            createSpecialtyIfNotExists("Préparation Examens", "Bac, concours et tests standardisés", educationDomain);
        }
    }
    
    private void initializeDecorDomain() {
        Domain decorDomain = createDomainIfNotExists(
            "Décoration", 
            "Professionnels de la décoration et de l'aménagement", 
            "/assets/icons/decor.svg"
        );
        
        if (decorDomain != null) {
            // Exactement comme dans deco-domain.component.ts
            createSpecialtyIfNotExists("Décoration Résidentielle", "Aménagement intérieur pour maisons et appartements", decorDomain);
            createSpecialtyIfNotExists("Décoration Événementielle", "Mariages, fêtes et cérémonies", decorDomain);
            createSpecialtyIfNotExists("Design Commercial", "Boutiques, restaurants et espaces professionnels", decorDomain);
            createSpecialtyIfNotExists("Staging Immobilier", "Mise en valeur de biens immobiliers", decorDomain);
        }
    }
    
    // Méthodes existantes...
    public List<Domain> getAllActiveDomains() {
        return domainRepository.findByActiveTrue();
    }
    
    public Map<String, Object> getDomainWithSpecialties(String domainName) {
        Optional<Domain> domainOpt = domainRepository.findByName(domainName);
        
        if (domainOpt.isPresent()) {
            Domain domain = domainOpt.get();
            List<Specialty> specialties = specialtyRepository.findByDomainIdAndActiveTrue(domain.getId());
            
            Map<String, Object> result = new HashMap<>();
            result.put("domain", domain);
            result.put("specialties", specialties);
            result.put("practitionersCount", getPractitionersCountByDomain(domain.getId()));
            
            return result;
        }
        
        return new HashMap<>();
    }
    
    public List<Specialty> getSpecialtiesByDomainName(String domainName) {
        Optional<Domain> domain = domainRepository.findByName(domainName);
        if (domain.isPresent()) {
            return specialtyRepository.findByDomainIdAndActiveTrue(domain.get().getId());
        }
        return new ArrayList<>();
    }
    
    public List<Practitioner> getPractitionersBySpecialty(String domainName, String specialtyName) {
        Optional<Domain> domain = domainRepository.findByName(domainName);
        if (domain.isPresent()) {
            Optional<Specialty> specialty = specialtyRepository.findByNameAndDomainId(specialtyName, domain.get().getId());
            if (specialty.isPresent()) {
                return practitionerRepository.findBySpecialtyEntityIdAndIsVerifiedTrue(specialty.get().getId());
            }
        }
        return new ArrayList<>();
    }
    
    public Map<String, Object> searchDomains(String query) {
        Map<String, Object> results = new HashMap<>();
        
        // Recherche dans les domaines
        List<Domain> domains = domainRepository.findByNameContainingIgnoreCaseAndActiveTrue(query);
        
        // Recherche dans les spécialités
        List<Specialty> specialties = specialtyRepository.findByNameContainingIgnoreCaseAndActiveTrue(query);
        
        // Recherche dans les praticiens
        List<Practitioner> practitioners = practitionerRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseAndIsVerifiedTrue(query, query);
        
        results.put("domains", domains);
        results.put("specialties", specialties);
        results.put("practitioners", practitioners);
        results.put("totalResults", domains.size() + specialties.size() + practitioners.size());
        
        return results;
    }
    
    private long getPractitionersCountByDomain(Long domainId) {
        List<Specialty> specialties = specialtyRepository.findByDomainIdAndActiveTrue(domainId);
        return specialties.stream()
                .mapToLong(specialty -> practitionerRepository.countBySpecialtyEntityIdAndIsVerifiedTrue(specialty.getId()))
                .sum();
    }
    
    // Méthodes utilitaires
    private Domain createDomainIfNotExists(String name, String description, String iconUrl) {
        Optional<Domain> existing = domainRepository.findByName(name);
        if (!existing.isPresent()) {
            Domain domain = new Domain(name, description, iconUrl);
            return domainRepository.save(domain);
        }
        return existing.get();
    }
    
    private void createSpecialtyIfNotExists(String name, String description, Domain domain) {
        Optional<Specialty> existing = specialtyRepository.findByNameAndDomainId(name, domain.getId());
        if (!existing.isPresent()) {
            Specialty specialty = new Specialty(name, description, domain);
            specialtyRepository.save(specialty);
        }
    }
    
    // Méthodes manquantes pour DomainController
    public Domain getDomainById(Long id) {
        Optional<Domain> domain = domainRepository.findById(id);
        return domain.orElse(null);
    }
    
    public Domain createDomain(Domain domain) {
        domain.setActive(true);
        domain.setCreatedAt(LocalDateTime.now());
        return domainRepository.save(domain);
    }
    
    public Domain updateDomain(Long id, Domain domainDetails) {
        Optional<Domain> domainOpt = domainRepository.findById(id);
        if (domainOpt.isPresent()) {
            Domain domain = domainOpt.get();
            domain.setName(domainDetails.getName());
            domain.setDescription(domainDetails.getDescription());
            domain.setIconUrl(domainDetails.getIconUrl());
            domain.setUpdatedAt(LocalDateTime.now());
            return domainRepository.save(domain);
        }
        return null;
    }
    
    public boolean deleteDomain(Long id) {
        Optional<Domain> domainOpt = domainRepository.findById(id);
        if (domainOpt.isPresent()) {
            Domain domain = domainOpt.get();
            domain.setActive(false);
            domainRepository.save(domain);
            return true;
        }
        return false;
    }
}
