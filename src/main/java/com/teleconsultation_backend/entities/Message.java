package com.teleconsultation_backend.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "sender_patient_id")
    private Patient senderPatient;
    
    @ManyToOne
    @JoinColumn(name = "sender_practitioner_id")
    private Practitioner senderPractitioner;
    
    @ManyToOne
    @JoinColumn(name = "receiver_patient_id")
    private Patient receiverPatient;
    
    @ManyToOne
    @JoinColumn(name = "receiver_practitioner_id")
    private Practitioner receiverPractitioner;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    
    private String messageType; // "TEXT", "IMAGE", "FILE"
    
    private String attachmentUrl;
    
    @Column(nullable = false)
    private LocalDateTime sentAt;
    
    private LocalDateTime readAt;
    
    private boolean isRead = false;
    
    // Constructors
    public Message() {
        this.sentAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Patient getSenderPatient() { return senderPatient; }
    public void setSenderPatient(Patient senderPatient) { this.senderPatient = senderPatient; }
    
    public Practitioner getSenderPractitioner() { return senderPractitioner; }
    public void setSenderPractitioner(Practitioner senderPractitioner) { this.senderPractitioner = senderPractitioner; }
    
    public Patient getReceiverPatient() { return receiverPatient; }
    public void setReceiverPatient(Patient receiverPatient) { this.receiverPatient = receiverPatient; }
    
    public Practitioner getReceiverPractitioner() { return receiverPractitioner; }
    public void setReceiverPractitioner(Practitioner receiverPractitioner) { this.receiverPractitioner = receiverPractitioner; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getMessageType() { return messageType; }
    public void setMessageType(String messageType) { this.messageType = messageType; }
    
    public String getAttachmentUrl() { return attachmentUrl; }
    public void setAttachmentUrl(String attachmentUrl) { this.attachmentUrl = attachmentUrl; }
    
    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
    
    public LocalDateTime getReadAt() { return readAt; }
    public void setReadAt(LocalDateTime readAt) { this.readAt = readAt; }
    
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}
