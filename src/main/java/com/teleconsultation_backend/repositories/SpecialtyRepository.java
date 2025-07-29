package com.teleconsultation_backend.repositories;

import com.teleconsultation_backend.entities.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {
    
    // Méthodes pour le chatbot et les services
    List<Specialty> findByDomainName(String domainName);
    
    List<Specialty> findByDomainNameAndActiveTrue(String domainName);
    
    // Méthodes utilisées par DomainService
    List<Specialty> findByDomainIdAndActiveTrue(Long domainId);
    
    Optional<Specialty> findByNameAndDomainId(String name, Long domainId);
    
    List<Specialty> findByNameContainingIgnoreCase(String name);
    
    List<Specialty> findByNameContainingIgnoreCaseAndActiveTrue(String name);
    
    List<Specialty> findByActiveTrue();
    
    // Méthodes pour l'administration
    List<Specialty> findAll();
    
    @NonNull
    Optional<Specialty> findById(@NonNull Long id);
}
