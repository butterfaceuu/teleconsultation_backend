package com.teleconsultation_backend.services;

import com.teleconsultation_backend.entities.Message;
import com.teleconsultation_backend.entities.Patient;
import com.teleconsultation_backend.entities.Practitioner;
import com.teleconsultation_backend.dtos.MessageRequest;
import com.teleconsultation_backend.repositories.MessageRepository;
import com.teleconsultation_backend.repositories.PatientRepository;
import com.teleconsultation_backend.repositories.PractitionerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    
    private final MessageRepository messageRepository;
    private final PatientRepository patientRepository;
    private final PractitionerRepository practitionerRepository;
    
    @Autowired
    public MessageService(MessageRepository messageRepository,
                         PatientRepository patientRepository,
                         PractitionerRepository practitionerRepository) {
        this.messageRepository = messageRepository;
        this.patientRepository = patientRepository;
        this.practitionerRepository = practitionerRepository;
    }
    
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
            patientRepository.findById(request.getSenderId())
                .ifPresent(message::setSenderPatient);
            
            practitionerRepository.findById(request.getSenderId())
                .ifPresent(message::setSenderPractitioner);
        }
        
        // Set receiver
        if (request.getReceiverId() != null) {
            patientRepository.findById(request.getReceiverId())
                .ifPresent(message::setReceiverPatient);
            
            practitionerRepository.findById(request.getReceiverId())
                .ifPresent(message::setReceiverPractitioner);
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
        return messageRepository.findById(messageId)
            .map(message -> {
                message.setRead(true);
                message.setReadAt(LocalDateTime.now());
                return messageRepository.save(message);
            })
            .orElse(null);
    }
    
    public int getUnreadMessagesCountForPatient(Long patientId) {
        return messageRepository.countUnreadMessagesForPatient(patientId);
    }
    
    public int getUnreadMessagesCountForPractitioner(Long practitionerId) {
        return messageRepository.countUnreadMessagesForPractitioner(practitionerId);
    }
}
