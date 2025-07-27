package com.teleconsultation_backend.repositories;

import com.teleconsultation_backend.entities.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findTopByEmailAndTypeAndUsedFalseAndExpiresAtAfterOrderByCreatedAtDesc(
        String email, String type, LocalDateTime now);
    
    Optional<VerificationCode> findByEmailAndCodeAndTypeAndUsedFalse(
        String email, String code, String type);
    
    void deleteByEmailAndType(String email, String type);
}
