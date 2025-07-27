package com.teleconsultation_backend.dtos;

import java.util.List;
import java.util.Map;

public class DashboardResponse {
    private Map<String, Object> user;
    private List<Map<String, Object>> appointments;
    private List<Map<String, Object>> contacts;
    private List<Map<String, Object>> messages;
    private Map<String, Object> stats;
    
    // Constructors
    public DashboardResponse() {}
    
    // Getters and Setters
    public Map<String, Object> getUser() { return user; }
    public void setUser(Map<String, Object> user) { this.user = user; }
    
    public List<Map<String, Object>> getAppointments() { return appointments; }
    public void setAppointments(List<Map<String, Object>> appointments) { this.appointments = appointments; }
    
    public List<Map<String, Object>> getContacts() { return contacts; }
    public void setContacts(List<Map<String, Object>> contacts) { this.contacts = contacts; }
    
    public List<Map<String, Object>> getMessages() { return messages; }
    public void setMessages(List<Map<String, Object>> messages) { this.messages = messages; }
    
    public Map<String, Object> getStats() { return stats; }
    public void setStats(Map<String, Object> stats) { this.stats = stats; }
}
