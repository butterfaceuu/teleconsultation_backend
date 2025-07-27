package com.teleconsultation_backend.dtos;

public class MessageRequest {
    private Long senderId;
    private Long receiverId;
    private String content;
    private String messageType = "TEXT";
    
    // Constructors
    public MessageRequest() {}
    
    // Getters and Setters
    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }
    
    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getMessageType() { return messageType; }
    public void setMessageType(String messageType) { this.messageType = messageType; }
}
