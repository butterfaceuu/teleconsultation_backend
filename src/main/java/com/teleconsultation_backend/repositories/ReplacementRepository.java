package com.teleconsultation_backend.repositories;

import com.teleconsultation_backend.entities.Replacement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReplacementRepository extends JpaRepository<Replacement, Long> {
    List<Replacement> findByOriginalPractitionerId(Long practitionerId);
    List<Replacement> findByReplacementPractitionerId(Long practitionerId);
    List<Replacement> findByOriginalPractitionerIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
        Long practitionerId, LocalDateTime endDate, LocalDateTime startDate);
}
