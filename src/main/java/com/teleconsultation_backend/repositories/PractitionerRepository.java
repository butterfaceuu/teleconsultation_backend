package com.teleconsultation_backend.repositories;

import com.teleconsultation_backend.entities.Practitioner;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PractitionerRepository extends JpaRepository<Practitioner, Long> {
    Optional<Practitioner> findByEmail(String email);
} 