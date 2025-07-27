package com.teleconsultation_backend.controllers;

import com.teleconsultation_backend.entities.Message;
import com.teleconsultation_backend.dtos.MessageRequest;
import com.teleconsultation_backend.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    
    @Autowired
    private MessageService messageService;
    
    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestBody MessageRequest request) {
        try {
            Message message = messageService.sendMessage(request);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/conversation")
    public ResponseEntity<List<Message>> getConversation(
            @RequestParam Long patientId,
            @RequestParam Long practitionerId) {
        List<Message> messages = messageService.getConversation(patientId, practitionerId);
        return ResponseEntity.ok(messages);
    }
    
    @GetMapping("/patient/{patientId}/conversations")
    public ResponseEntity<List<Object>> getPatientConversations(@PathVariable Long patientId) {
        List<Object> conversations = messageService.getPatientConversations(patientId);
        return ResponseEntity.ok(conversations);
    }
    
    @GetMapping("/practitioner/{practitionerId}/conversations")
    public ResponseEntity<List<Object>> getPractitionerConversations(@PathVariable Long practitionerId) {
        List<Object> conversations = messageService.getPractitionerConversations(practitionerId);
        return ResponseEntity.ok(conversations);
    }
    
    @PutMapping("/{messageId}/read")
    public ResponseEntity<Message> markAsRead(@PathVariable Long messageId) {
        Message message = messageService.markAsRead(messageId);
        if (message != null) {
            return ResponseEntity.ok(message);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/unread/patient/{patientId}")
    public ResponseEntity<Integer> getUnreadMessagesCount(@PathVariable Long patientId) {
        int count = messageService.getUnreadMessagesCountForPatient(patientId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/unread/practitioner/{practitionerId}")
    public ResponseEntity<Integer> getUnreadMessagesCountForPractitioner(@PathVariable Long practitionerId) {
        int count = messageService.getUnreadMessagesCountForPractitioner(practitionerId);
        return ResponseEntity.ok(count);
    }
}
