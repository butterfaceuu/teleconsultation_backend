package com.teleconsultation_backend.services;

import com.teleconsultation_backend.entities.Practitioner;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailService {
    
    public void sendOTPEmail(String email, String otpCode, String subject) {
        // Implémentation de l'envoi d'email
        // Vous pouvez utiliser JavaMailSender ou un service externe comme SendGrid
        
        System.out.println("=== EMAIL OTP ===");
        System.out.println("To: " + email);
        System.out.println("Subject: " + subject);
        System.out.println("OTP Code: " + otpCode);
        System.out.println("================");
        
        // TODO: Implémenter l'envoi réel d'email
    }
    
    public void sendPractitionerApprovalEmail(String email, String practitionerName) {
        System.out.println("=== EMAIL APPROVAL ===");
        System.out.println("To: " + email);
        System.out.println("Subject: Compte praticien approuvé");
        System.out.println("Message: Félicitations " + practitionerName + ", votre compte a été approuvé!");
        System.out.println("=====================");
    }
    
    public void sendPractitionerRejectionEmail(String email, String practitionerName, String reason) {
        System.out.println("=== EMAIL REJECTION ===");
        System.out.println("To: " + email);
        System.out.println("Subject: Compte praticien rejeté");
        System.out.println("Message: Désolé " + practitionerName + ", votre compte a été rejeté. Raison: " + reason);
        System.out.println("======================");
    }
    
    public void sendNewPractitionerNotificationToAdmins(Practitioner practitioner) {
        System.out.println("=== ADMIN NOTIFICATION ===");
        System.out.println("Subject: Nouveau praticien en attente de validation");
        System.out.println("Message: " + practitioner.getFirstName() + " " + practitioner.getLastName() + 
                          " (" + practitioner.getSpecialty() + ") a créé un compte et attend validation");
        System.out.println("=========================");
    }
    
    public void sendNotificationEmail(String email, String subject, String message) {
        System.out.println("=== NOTIFICATION EMAIL ===");
        System.out.println("To: " + email);
        System.out.println("Subject: " + subject);
        System.out.println("Message: " + message);
        System.out.println("=========================");
    }

    public void sendAppointmentCancellationEmail(String practitionerEmail, String patientName, LocalDateTime appointmentDateTime) {
        System.out.println("=== EMAIL CANCELLATION ===");
        System.out.println("To: " + practitionerEmail);
        System.out.println("Subject: Rendez-vous annulé");
        System.out.println("Message: Le patient " + patientName + " a annulé son rendez-vous du " + appointmentDateTime);
        System.out.println("==========================");
    }

    public void sendWarningEmail(String userEmail, String reason) {
        System.out.println("=== EMAIL WARNING ===");
        System.out.println("To: " + userEmail);
        System.out.println("Subject: Avertissement");
        System.out.println("Message: Vous avez reçu un avertissement pour: " + reason);
        System.out.println("=====================");
    }
}
