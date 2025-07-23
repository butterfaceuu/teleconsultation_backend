package com.teleconsultation_backend.services;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    public void sendVerificationCode(String to, String code) {
        // TODO: Implement actual email sending logic
        System.out.println("Sending verification code " + code + " to " + to);
    }
} 