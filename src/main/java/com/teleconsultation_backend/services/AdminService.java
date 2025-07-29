package com.teleconsultation_backend.services;

import com.teleconsultation_backend.entities.*;
import com.teleconsultation_backend.dtos.*;
import com.teleconsultation_backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.*;

@Service
public class AdminService {
    
    private final AdminRepository adminRepository;
    private final PractitionerRepository practitionerRepository;
    private final PatientRepository patientRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final AppointmentRepository appointmentRepository;
    private final PaymentRepository paymentRepository;
    private final MessageRepository messageRepository;
    
    @Autowired
    public AdminService(AdminRepository adminRepository,
                       PractitionerRepository practitionerRepository,
                       PatientRepository patientRepository,
                       VerificationCodeRepository verificationCodeRepository,
                       AppointmentRepository appointmentRepository,
                       PaymentRepository paymentRepository,
                       MessageRepository messageRepository,
                       EmailService emailService) {
        this.adminRepository = adminRepository;
        this.practitionerRepository = practitionerRepository;
        this.patientRepository = patientRepository;
        this.verificationCodeRepository = verificationCodeRepository;
        this.appointmentRepository = appointmentRepository;
        this.paymentRepository = paymentRepository;
        this.messageRepository = messageRepository;
        this.emailService = emailService;
    }
    
    private final EmailService emailService;
    
    public Map<String, Object> login(LoginRequest loginRequest) {
        Optional<Admin> adminOpt = adminRepository.findByEmail(loginRequest.getEmail());
        
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            
            // Vérification du mot de passe (vous devriez utiliser BCrypt en production)
            if (admin.getPassword().equals(loginRequest.getPassword())) {
                
                // Générer et envoyer l'OTP
                String otpCode = generateOTP();
                
                VerificationCode verificationCode = new VerificationCode();
                verificationCode.setEmail(admin.getEmail());
                verificationCode.setCode(otpCode);
                verificationCode.setType("ADMIN_LOGIN");
                verificationCode.setCreatedAt(LocalDateTime.now());
                verificationCode.setExpiresAt(LocalDateTime.now().plusMinutes(5));
                verificationCode.setUsed(false);
                
                verificationCodeRepository.save(verificationCode);
                
                // Envoyer l'OTP par email
                emailService.sendOTPEmail(admin.getEmail(), otpCode, "Connexion Admin");
                
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Code OTP envoyé à votre email");
                response.put("email", admin.getEmail());
                response.put("requiresOTP", true);
                
                return response;
            } else {
                throw new RuntimeException("Mot de passe incorrect");
            }
        } else {
            throw new RuntimeException("Admin non trouvé");
        }
    }
    
    public Map<String, Object> verifyOTP(VerificationRequest verificationRequest) {
        Optional<VerificationCode> codeOpt = verificationCodeRepository
            .findTopByEmailAndTypeAndUsedFalseAndExpiresAtAfterOrderByCreatedAtDesc(
                verificationRequest.getEmail(), 
                verificationRequest.getType(), 
                LocalDateTime.now()
            );
        
        if (codeOpt.isPresent()) {
            VerificationCode code = codeOpt.get();
            
            if (code.getCode().equals(verificationRequest.getCode())) {
                code.setUsed(true);
                verificationCodeRepository.save(code);
                
                Optional<Admin> adminOpt = adminRepository.findByEmail(verificationRequest.getEmail());
                
                if (!adminOpt.isPresent()) {
                    throw new RuntimeException("Admin non trouvé");
                }
                
                Admin admin = adminOpt.get();
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Connexion réussie");
                response.put("admin", admin);
                response.put("token", generateToken(admin)); // Vous pouvez implémenter JWT ici
                
                return response;
            } else {
                throw new RuntimeException("Code OTP incorrect");
            }
        } else {
            throw new RuntimeException("Code OTP expiré ou invalide");
        }
    }
    
    public AdminStatsResponse getDashboardStats() {
        AdminStatsResponse stats = new AdminStatsResponse();
        
        stats.setTotalPatients(patientRepository.count());
        stats.setTotalPractitioners(practitionerRepository.count());
        stats.setPendingPractitioners(practitionerRepository.countByIsVerifiedFalse());
        stats.setVerifiedPractitioners(practitionerRepository.countByIsVerifiedTrue());
        stats.setTotalAppointments(appointmentRepository.count());
        
        // Rendez-vous d'aujourd'hui
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);
        stats.setTodayAppointments(appointmentRepository.countByAppointmentDateTimeBetween(startOfDay, endOfDay));
        
        // Revenus totaux
        List<Payment> completedPayments = paymentRepository.findByStatus("COMPLETED");
        double totalRevenue = completedPayments.stream()
            .mapToDouble(Payment::getAmount)
            .sum();
        stats.setTotalRevenue(totalRevenue);
        
        stats.setTotalMessages(messageRepository.count());
        
        return stats;
    }
    
    public List<Practitioner> getPendingPractitioners() {
        return practitionerRepository.findByIsVerifiedFalse();
    }
    
    public Practitioner getPractitionerById(Long id) {
        Optional<Practitioner> practitioner = practitionerRepository.findById(id);
        return practitioner.orElse(null);
    }
    
    public String validatePractitioner(PractitionerValidationRequest request) {
        Optional<Practitioner> practitionerOpt = practitionerRepository.findById(request.getPractitionerId());
        
        if (practitionerOpt.isPresent()) {
            Practitioner practitioner = practitionerOpt.get();
            
            if ("ACCEPT".equals(request.getAction())) {
                practitioner.setVerified(true);
                practitionerRepository.save(practitioner);
                
                // Envoyer email de confirmation
                emailService.sendPractitionerApprovalEmail(
                    practitioner.getEmail(), 
                    practitioner.getFirstName() + " " + practitioner.getLastName()
                );
                
                return "Praticien accepté avec succès";
                
            } else if ("REJECT".equals(request.getAction())) {
                // Vous pouvez soit supprimer le praticien soit marquer comme rejeté
                // Ici on va juste envoyer un email de rejet et garder le compte non vérifié
                
                emailService.sendPractitionerRejectionEmail(
                    practitioner.getEmail(), 
                    practitioner.getFirstName() + " " + practitioner.getLastName(),
                    request.getReason()
                );
                
                return "Praticien rejeté";
            } else {
                throw new RuntimeException("Action non valide");
            }
        } else {
            throw new RuntimeException("Praticien non trouvé");
        }
    }
    
    public List<Practitioner> getAllPractitioners() {
        return practitionerRepository.findAll();
    }
    
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
    
    public String updatePractitionerStatus(Long id, String status) {
        Optional<Practitioner> practitionerOpt = practitionerRepository.findById(id);
        
        if (practitionerOpt.isPresent()) {
            Practitioner practitioner = practitionerOpt.get();
            
            if ("VERIFIED".equals(status)) {
                practitioner.setVerified(true);
            } else if ("UNVERIFIED".equals(status)) {
                practitioner.setVerified(false);
            }
            
            practitionerRepository.save(practitioner);
            return "Statut du praticien mis à jour";
        } else {
            throw new RuntimeException("Praticien non trouvé");
        }
    }
    
    public String updatePatientStatus(Long id, String status) {
        Optional<Patient> patientOpt = patientRepository.findById(id);
        
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            
            if ("VERIFIED".equals(status)) {
                patient.setVerified(true);
            } else if ("UNVERIFIED".equals(status)) {
                patient.setVerified(false);
            }
            
            patientRepository.save(patient);
            return "Statut du patient mis à jour";
        } else {
            throw new RuntimeException("Patient non trouvé");
        }
    }
    
    public byte[] getPractitionerProfessionalCard(Long id) {
        Optional<Practitioner> practitioner = practitionerRepository.findById(id);
        if (practitioner.isPresent()) {
            return practitioner.get().getProfessionalCard();
        }
        return null;
    }
    
    public byte[] getPatientNationalCard(Long id) {
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isPresent()) {
            return patient.get().getNationalCard();
        }
        return null;
    }
    
    public Map<String, Object> getMonthlyReport() {
        Map<String, Object> report = new HashMap<>();
        
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atTime(23, 59, 59);
        
        // Nouveaux patients ce mois
        long newPatients = patientRepository.countByCreatedAtBetween(startOfMonth, endOfMonth);
        report.put("newPatients", newPatients);
        
        // Nouveaux praticiens ce mois
        long newPractitioners = practitionerRepository.countByCreatedAtBetween(startOfMonth, endOfMonth);
        report.put("newPractitioners", newPractitioners);
        
        // Rendez-vous ce mois
        long monthlyAppointments = appointmentRepository.countByCreatedAtBetween(startOfMonth, endOfMonth);
        report.put("monthlyAppointments", monthlyAppointments);
        
        // Revenus ce mois
        List<Payment> monthlyPayments = paymentRepository.findByCreatedAtBetweenAndStatus(startOfMonth, endOfMonth, "COMPLETED");
        double monthlyRevenue = monthlyPayments.stream()
            .mapToDouble(Payment::getAmount)
            .sum();
        report.put("monthlyRevenue", monthlyRevenue);
        
        return report;
    }
    
    public String sendNotification(Map<String, Object> request) {
        String type = (String) request.get("type");
        String message = (String) request.get("message");
        String targetEmail = (String) request.get("targetEmail");
        
        if ("EMAIL".equals(type)) {
            emailService.sendNotificationEmail(targetEmail, "Notification Admin", message);
            return "Notification envoyée par email";
        }
        
        return "Type de notification non supporté";
    }
    
    public Map<String, Object> getGlobalStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPractitioners", practitionerRepository.count());
        stats.put("totalAppointments", appointmentRepository.count());
        // Add more stats as needed
        return stats;
    }

    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
    
    private String generateToken(Admin admin) {
        // Implémentation simple - vous devriez utiliser JWT en production
        return "admin_token_" + admin.getId() + "_" + System.currentTimeMillis();
    }
}
