package com.teleconsultation_backend.repositories;

import com.teleconsultation_backend.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByPatientId(Long patientId);
    List<Document> findByPractitionerId(Long practitionerId);
    List<Document> findByAppointmentId(Long appointmentId);
    List<Document> findByDocumentType(String documentType);
}
