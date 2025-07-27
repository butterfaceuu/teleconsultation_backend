package com.teleconsultation_backend.repositories;

import com.teleconsultation_backend.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByAppointmentId(Long appointmentId);
    Optional<Payment> findByPaymentIntentId(String paymentIntentId);
    List<Payment> findByAppointmentPatientId(Long patientId);
    List<Payment> findByAppointmentPractitionerId(Long practitionerId);
    List<Payment> findByStatus(String status);
    List<Payment> findByCreatedAtBetweenAndStatus(LocalDateTime start, LocalDateTime end, String status);
    List<Payment> findByAppointmentPatientIdAndStatus(Long patientId, String status);
    List<Payment> findByAppointmentPractitionerIdAndStatus(Long practitionerId, String status);
}
