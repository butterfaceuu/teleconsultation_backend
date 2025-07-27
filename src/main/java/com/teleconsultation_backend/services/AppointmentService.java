package com.teleconsultation_backend.services;

import com.teleconsultation_backend.entities.*;
import com.teleconsultation_backend.dtos.AppointmentRequest;
import com.teleconsultation_backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private PractitionerRepository practitionerRepository;
    
    public Appointment createAppointment(AppointmentRequest request) {
        Optional<Patient> patient = patientRepository.findById(request.getPatientId());
        Optional<Practitioner> practitioner = practitionerRepository.findById(request.getPractitionerId());
        
        if (patient.isPresent() && practitioner.isPresent()) {
            Appointment appointment = new Appointment();
            appointment.setPatient(patient.get());
            appointment.setPractitioner(practitioner.get());
            appointment.setAppointmentDateTime(request.getAppointmentDateTime());
            appointment.setType(request.getType());
            appointment.setConsultationType(request.getConsultationType());
            appointment.setLocation(request.getLocation());
            appointment.setNotes(request.getNotes());
            appointment.setPrice(request.getPrice());
            appointment.setStatus("SCHEDULED");
            
            return appointmentRepository.save(appointment);
        }
        throw new RuntimeException("Patient or Practitioner not found");
    }
    
    public List<Appointment> getPatientAppointments(Long patientId) {
        return appointmentRepository.findByPatientIdOrderByAppointmentDateTimeDesc(patientId);
    }
    
    public List<Appointment> getPractitionerAppointments(Long practitionerId) {
        return appointmentRepository.findByPractitionerIdOrderByAppointmentDateTimeDesc(practitionerId);
    }
    
    public Appointment getAppointmentById(Long id) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        return appointment.orElse(null);
    }
    
    public Appointment updateAppointmentStatus(Long id, String status) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);
        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();
            appointment.setStatus(status);
            appointment.setUpdatedAt(LocalDateTime.now());
            return appointmentRepository.save(appointment);
        }
        throw new RuntimeException("Appointment not found");
    }
    
    public Appointment updateAppointment(Long id, AppointmentRequest request) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);
        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();
            appointment.setAppointmentDateTime(request.getAppointmentDateTime());
            appointment.setType(request.getType());
            appointment.setConsultationType(request.getConsultationType());
            appointment.setLocation(request.getLocation());
            appointment.setNotes(request.getNotes());
            appointment.setPrice(request.getPrice());
            appointment.setUpdatedAt(LocalDateTime.now());
            return appointmentRepository.save(appointment);
        }
        throw new RuntimeException("Appointment not found");
    }
    
    public boolean cancelAppointment(Long id) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        if (appointment.isPresent()) {
            Appointment app = appointment.get();
            app.setStatus("CANCELLED");
            app.setUpdatedAt(LocalDateTime.now());
            appointmentRepository.save(app);
            return true;
        }
        return false;
    }
}
