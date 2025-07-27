package com.teleconsultation_backend.repositories;

import com.teleconsultation_backend.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientIdOrderByAppointmentDateTimeDesc(Long patientId);
    List<Appointment> findByPractitionerIdOrderByAppointmentDateTimeDesc(Long practitionerId);
    List<Appointment> findByPractitionerIdAndAppointmentDateTimeBetween(Long practitionerId, LocalDateTime start, LocalDateTime end);
    List<Appointment> findByPatientIdAndAppointmentDateTimeBetween(Long patientId, LocalDateTime start, LocalDateTime end);
    long countByAppointmentDateTimeBetween(LocalDateTime start, LocalDateTime end);
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // Add these methods to the existing interface
    List<Appointment> findByPatientIdAndAppointmentDateTimeAfterOrderByAppointmentDateTimeAsc(Long patientId, LocalDateTime dateTime);
    List<Appointment> findByPatientIdAndAppointmentDateTimeBeforeOrderByAppointmentDateTimeDesc(Long patientId, LocalDateTime dateTime);
    List<Appointment> findByPractitionerIdAndAppointmentDateTimeAfterOrderByAppointmentDateTimeAsc(Long practitionerId, LocalDateTime dateTime);
    long countByPatientId(Long patientId);
    long countByPractitionerId(Long practitionerId);
    long countByPatientIdAndStatus(Long patientId, String status);
    long countByPractitionerIdAndStatus(Long practitionerId, String status);
    long countByPractitionerIdAndAppointmentDateTimeBetween(Long practitionerId, LocalDateTime start, LocalDateTime end);
    List<Appointment> findTop10ByOrderByCreatedAtDesc();
}
