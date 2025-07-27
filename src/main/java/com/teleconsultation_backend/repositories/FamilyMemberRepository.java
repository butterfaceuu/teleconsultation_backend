package com.teleconsultation_backend.repositories;

import com.teleconsultation_backend.entities.FamilyMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Long> {
    List<FamilyMember> findByPatientIdAndActiveTrue(Long patientId);
}
