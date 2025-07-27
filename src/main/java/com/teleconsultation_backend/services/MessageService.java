package com.teleconsultation_backend.services;

import com.teleconsultation_backend.entities.*;
import com.teleconsultation_backend.dtos.MessageRequest;
import com.teleconsultation_backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private PractitionerRepository practitionerRepository;
    
    public Message sendMessage(MessageRequest request) {
        Message message = new Message();
        message.setContent(request.getContent());
        message.setMessageType(request.getMessageType());
        
        // Set sender and receiver using getSenderId() and getReceiverId()
        // Example: message.setSenderPatient(patientRepository.findById(request.getSenderId()).orElse(null));
        //          message.setReceiverPractitioner(practitionerRepository.findById(request.getReceiverId()).orElse(null));
        // Adjust as needed based on your business logic.
        
        // Set sender
        if (request.getSenderId() != null) {
            Optional<Patient> patient = patientRepository.findById(request.getSenderId());
            if (patient.isPresent()) {
                message.setSenderPatient(patient.get());
            }
        }
        
        if (request.getSenderId() != null) {
            Optional<Practitioner> practitioner = practitionerRepository.findById(request.getSenderId());
            if (practitioner.isPresent()) {
                message.setSenderPractitioner(practitioner.get());
            }
        }
        
        // Set receiver
        if (request.getReceiverId() != null) {
            Optional<Patient> patient = patientRepository.findById(request.getReceiverId());
            if (patient.isPresent()) {
                message.setReceiverPatient(patient.get());
            }
        }
        
        if (request.getReceiverId() != null) {
            Optional<Practitioner> practitioner = practitionerRepository.findById(request.getReceiverId());
            if (practitioner.isPresent()) {
                message.setReceiverPractitioner(practitioner.get());
            }
        }
        
        return messageRepository.save(message);
    }
    
    public List<Message> getConversation(Long patientId, Long practitionerId) {
        return messageRepository.findConversationBetweenPatientAndPractitioner(patientId, practitionerId);
    }
    
    public List<Object> getPatientConversations(Long patientId) {
        return messageRepository.findPatientConversations(patientId);
    }
    
    public List<Object> getPractitionerConversations(Long practitionerId) {
        return messageRepository.findPractitionerConversations(practitionerId);
    }
    
    public Message markAsRead(Long messageId) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if (optionalMessage.isPresent()) {
            Message message = optionalMessage.get();
            message.setRead(true);
            message.setReadAt(LocalDateTime.now());
            return messageRepository.save(message);
        }
        return null;
    }
    
    public int getUnreadMessagesCountForPatient(Long patientId) {
        return messageRepository.countUnreadMessagesForPatient(patientId);
    }
    
    public int getUnreadMessagesCountForPractitioner(Long practitionerId) {
        return messageRepository.countUnreadMessagesForPractitioner(practitionerId);
    }
}
