package com.teleconsultation_backend.repositories;

import com.teleconsultation_backend.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    long countByIsVerifiedTrue();
    long countByIsVerifiedFalse();
}
