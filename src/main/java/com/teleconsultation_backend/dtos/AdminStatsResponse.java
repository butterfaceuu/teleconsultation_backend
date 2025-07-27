package com.teleconsultation_backend.dtos;

public class AdminStatsResponse {
    private long totalPatients;
    private long totalPractitioners;
    private long pendingPractitioners;
    private long verifiedPractitioners;
    private long totalAppointments;
    private long todayAppointments;
    private double totalRevenue;
    private long totalMessages;
    
    public long getTotalPatients() {
        return this.totalPatients;
    }
    
    public void setTotalPatients(long totalPatients) {
        this.totalPatients = totalPatients;
    }
    
    public long getTotalPractitioners() {
        return this.totalPractitioners;
    }
    
    public void setTotalPractitioners(long totalPractitioners) {
        this.totalPractitioners = totalPractitioners;
    }
    
    public long getPendingPractitioners() {
        return this.pendingPractitioners;
    }
    
    public void setPendingPractitioners(long pendingPractitioners) {
        this.pendingPractitioners = pendingPractitioners;
    }
    
    public long getVerifiedPractitioners() {
        return this.verifiedPractitioners;
    }
    
    public void setVerifiedPractitioners(long verifiedPractitioners) {
        this.verifiedPractitioners = verifiedPractitioners;
    }
    
    public long getTotalAppointments() {
        return this.totalAppointments;
    }
    
    public void setTotalAppointments(long totalAppointments) {
        this.totalAppointments = totalAppointments;
    }
    
    public long getTodayAppointments() {
        return this.todayAppointments;
    }
    
    public void setTodayAppointments(long todayAppointments) {
        this.todayAppointments = todayAppointments;
    }
    
    public double getTotalRevenue() {
        return this.totalRevenue;
    }
    
    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
    
    public long getTotalMessages() {
        return this.totalMessages;
    }
    
    public void setTotalMessages(long totalMessages) {
        this.totalMessages = totalMessages;
    }
}
