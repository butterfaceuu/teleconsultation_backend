package com.teleconsultation_backend.repositories;

import com.teleconsultation_backend.entities.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DomainRepository extends JpaRepository<Domain, Long> {
    
    // Méthodes pour le chatbot et les services
    @NonNull
    List<Domain> findByActiveTrue();
    
    Optional<Domain> findByName(String name);
    
    List<Domain> findByNameContainingIgnoreCaseAndActiveTrue(String name);
    
    // Méthodes pour l'administration
    @NonNull
    List<Domain> findAll();
    
    @NonNull
    Optional<Domain> findById(@NonNull Long id);
}
