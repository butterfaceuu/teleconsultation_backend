package com.teleconsultation_backend.repositories;

import com.teleconsultation_backend.entities.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import java.time.LocalDateTime;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findTopByEmailAndTypeAndUsedFalseAndExpiresAtAfterOrderByCreatedAtDesc(String email, String type, LocalDateTime now);
    Optional<VerificationCode> findTopByEmailAndTypeOrderByCreatedAtDesc(String email, String type);
} 