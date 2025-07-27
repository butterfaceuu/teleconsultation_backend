package com.teleconsultation_backend.services;

import com.teleconsultation_backend.entities.*;
import com.teleconsultation_backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class DashboardActionService {
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private PractitionerRepository practitionerRepository;
    
    @Autowired
    private EmailService emailService;
    
    // ===== PATIENT ACTIONS =====
    
    public boolean cancelAppointment(Long patientId, Long appointmentId) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
        
        if (appointmentOpt.isPresent()) {
            Appointment appointment = appointmentOpt.get();
            
            // Vérifier que l'appointment appartient au patient
            if (appointment.getPatient().getId().equals(patientId)) {
                appointment.setStatus("CANCELLED");
                appointment.setUpdatedAt(LocalDateTime.now());
                appointmentRepository.save(appointment);
                
                // Notifier le praticien
                emailService.sendAppointmentCancellationEmail(
                    appointment.getPractitioner().getEmail(),
                    appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName(),
                    appointment.getAppointmentDateTime()
                );
                
                return true;
            }
        }
        
        return false;
    }
    
    public boolean sendPatientMessage(Long patientId, Long practitionerId, String messageContent) {
        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        Optional<Practitioner> practitionerOpt = practitionerRepository.findById(practitionerId);
        
        if (patientOpt.isPresent() && practitionerOpt.isPresent()) {
            Message message = new Message();
            message.setSenderPatient(patientOpt.get());
            message.setReceiverPractitioner(practitionerOpt.get());
            message.setContent(messageContent);
            message.setMessageType("TEXT");
            
            messageRepository.save(message);
            return true;
        }
        
        return false;
    }
    
    // ===== PRACTITIONER ACTIONS =====
    
    public boolean sendPractitionerMessage(Long practitionerId, Long targetPractitionerId, String messageContent) {
        Optional<Practitioner> senderOpt = practitionerRepository.findById(practitionerId);
        Optional<Practitioner> receiverOpt = practitionerRepository.findById(targetPractitionerId);
        
        if (senderOpt.isPresent() && receiverOpt.isPresent()) {
            Message message = new Message();
            message.setSenderPractitioner(senderOpt.get());
            message.setReceiverPractitioner(receiverOpt.get());
            message.setContent(messageContent);
            message.setMessageType("TEXT");
            
            messageRepository.save(message);
            return true;
        }
        
        return false;
    }
    
    public String initiateCall(Long practitionerId, Long contactId, String callType) {
        // Générer un ID d'appel unique
        String callId = "CALL_" + System.currentTimeMillis();
        
        // Ici vous pourriez intégrer avec un service de visioconférence
        // comme Jitsi Meet, Zoom API, ou WebRTC
        
        System.out.println("Appel " + callType + " initié entre praticien " + practitionerId + " et contact " + contactId);
        System.out.println("Call ID: " + callId);
        
        return callId;
    }
    
    // ===== ADMIN ACTIONS =====
    
    public boolean validatePractitioner(Long practitionerId) {
        Optional<Practitioner> practitionerOpt = practitionerRepository.findById(practitionerId);
        
        if (practitionerOpt.isPresent()) {
            Practitioner practitioner = practitionerOpt.get();
            practitioner.setVerified(true);
            practitionerRepository.save(practitioner);
            
            // Envoyer email de confirmation
            emailService.sendPractitionerApprovalEmail(
                practitioner.getEmail(),
                practitioner.getFirstName() + " " + practitioner.getLastName()
            );
            
            return true;
        }
        
        return false;
    }
    
    public boolean rejectPractitioner(Long practitionerId, String reason) {
        Optional<Practitioner> practitionerOpt = practitionerRepository.findById(practitionerId);
        
        if (practitionerOpt.isPresent()) {
            Practitioner practitioner = practitionerOpt.get();
            
            // Envoyer email de rejet
            emailService.sendPractitionerRejectionEmail(
                practitioner.getEmail(),
                practitioner.getFirstName() + " " + practitioner.getLastName(),
                reason
            );
            
            // Optionnel: supprimer le praticien ou le marquer comme rejeté
            practitioner.setActive(false);
            practitionerRepository.save(practitioner);
            
            return true;
        }
        
        return false;
    }
    
    public boolean moderateMessage(Long messageId, String action) {
        Optional<Message> messageOpt = messageRepository.findById(messageId);
        
        if (messageOpt.isPresent()) {
            Message message = messageOpt.get();
            
            switch (action) {
                case "warn":
                    // Envoyer un avertissement à l'expéditeur
                    if (message.getSenderPatient() != null) {
                        emailService.sendWarningEmail(message.getSenderPatient().getEmail(), "Message inapproprié");
                    } else if (message.getSenderPractitioner() != null) {
                        emailService.sendWarningEmail(message.getSenderPractitioner().getEmail(), "Message inapproprié");
                    }
                    break;
                    
                case "delete":
                    // Marquer le message comme supprimé (soft delete)
                    message.setContent("[Message supprimé par l'administrateur]");
                    messageRepository.save(message);
                    break;
                    
                case "restore":
                    // Restaurer le message (si vous gardez une copie)
                    // Implémentation dépendante de votre stratégie de sauvegarde
                    break;
            }
            
            return true;
        }
        
        return false;
    }
}
