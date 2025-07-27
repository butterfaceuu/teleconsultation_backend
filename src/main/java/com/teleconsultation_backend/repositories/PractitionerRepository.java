package com.teleconsultation_backend.repositories;

import com.teleconsultation_backend.entities.Practitioner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PractitionerRepository extends JpaRepository<Practitioner, Long> {
    
    // Méthodes pour l'authentification
    Optional<Practitioner> findByEmail(String email);
    
    Optional<Practitioner> findByEmailAndIsVerifiedTrue(String email);
    
    // Méthodes pour la recherche
    List<Practitioner> findBySpecialtyContainingIgnoreCase(String specialty);
    
    List<Practitioner> findBySourceContainingIgnoreCase(String source);
    
    List<Practitioner> findBySpecialtyAndIsVerifiedTrue(String specialty);
    
    List<Practitioner> findBySourceAndIsVerifiedTrue(String source);
    
    // Méthodes pour l'administration
    List<Practitioner> findByIsVerifiedFalse();
    
    List<Practitioner> findByIsActiveTrue();
    
    List<Practitioner> findByIsActiveFalse();
    
    // Méthodes pour les statistiques
    long countByIsVerifiedTrue();
    
    long countByIsVerifiedFalse();
    
    long countByIsActiveTrue();
    
    long countByIsActiveFalse();
    
    // Méthodes pour la recherche avancée
    List<Practitioner> findBySpecialtyContainingIgnoreCaseAndIsVerifiedTrue(String specialty);
    
    List<Practitioner> findBySourceContainingIgnoreCaseAndIsVerifiedTrue(String source);
    
    // Méthodes héritées avec annotations @NonNull
    @NonNull
    List<Practitioner> findAll();
    
    @NonNull
    Optional<Practitioner> findById(@NonNull Long id);
}
