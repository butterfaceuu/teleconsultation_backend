package com.teleconsultation_backend.services;

import com.teleconsultation_backend.dtos.*;
import com.teleconsultation_backend.entities.*;
import com.teleconsultation_backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AuthService {
    
    private final PatientRepository patientRepository;
    private final PractitionerRepository practitionerRepository;
    private final AdminRepository adminRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    
    @Autowired
    public AuthService(PatientRepository patientRepository,
                      PractitionerRepository practitionerRepository,
                      AdminRepository adminRepository,
                      VerificationCodeRepository verificationCodeRepository,
                      EmailService emailService) {
        this.patientRepository = patientRepository;
        this.practitionerRepository = practitionerRepository;
        this.adminRepository = adminRepository;
        this.verificationCodeRepository = verificationCodeRepository;
        this.emailService = emailService;
    }
    
    private final String uploadDir = "uploads/";
    
    public Map<String, Object> login(LoginRequest loginRequest) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            switch (loginRequest.getProfile().toLowerCase()) {
                case "admin":
                    return loginAdmin(loginRequest);
                case "patient":
                    return loginPatient(loginRequest);
                case "praticien":
                    return loginPractitioner(loginRequest);
                default:
                    throw new RuntimeException("Profil non reconnu");
            }
        } catch (Exception e) {
            response.put("error", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
    
    private Map<String, Object> loginAdmin(LoginRequest loginRequest) {
        Optional<Admin> adminOpt = adminRepository.findByEmail(loginRequest.getEmail());
        
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            
            // Vérification du mot de passe (vous devriez utiliser BCrypt en production)
            if (admin.getPassword().equals(loginRequest.getPassword())) {
                
                // Générer et envoyer l'OTP pour admin
                String otpCode = generateOTP();
                
                VerificationCode verificationCode = new VerificationCode(
                    admin.getEmail(), otpCode, "ADMIN_LOGIN", 5);
                verificationCodeRepository.save(verificationCode);
                
                // Envoyer l'OTP par email
                emailService.sendOTPEmail(admin.getEmail(), otpCode, "Connexion Admin");
                
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Code OTP envoyé à votre email");
                response.put("email", admin.getEmail());
                response.put("requiresOTP", true);
                response.put("redirectTo", "/admin-dashboard");
                
                return response;
            } else {
                throw new RuntimeException("Mot de passe incorrect");
            }
        } else {
            throw new RuntimeException("Admin non trouvé");
        }
    }
    
    private Map<String, Object> loginPatient(LoginRequest loginRequest) {
        Optional<Patient> patientOpt = patientRepository.findByEmail(loginRequest.getEmail());
        
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            
            if (!patient.isActive()) {
                throw new RuntimeException("Compte désactivé");
            }
            
            // Vérification du mot de passe
            if (patient.getPassword().equals(loginRequest.getPassword())) {
                
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Connexion réussie");
                response.put("user", createPatientResponse(patient));
                response.put("token", generateToken("patient", patient.getId()));
                response.put("redirectTo", "/patient-dashboard");
                
                return response;
            } else {
                throw new RuntimeException("Mot de passe incorrect");
            }
        } else {
            throw new RuntimeException("Patient non trouvé");
        }
    }
    
    private Map<String, Object> loginPractitioner(LoginRequest loginRequest) {
        Optional<Practitioner> practitionerOpt = practitionerRepository.findByEmail(loginRequest.getEmail());
        
        if (practitionerOpt.isPresent()) {
            Practitioner practitioner = practitionerOpt.get();
            
            if (!practitioner.isActive()) {
                throw new RuntimeException("Compte désactivé");
            }
            
            if (!practitioner.isVerified()) {
                throw new RuntimeException("Compte en attente de validation par l'administrateur");
            }
            
            // Vérification du mot de passe
            if (practitioner.getPassword().equals(loginRequest.getPassword())) {
                
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Connexion réussie");
                response.put("user", createPractitionerResponse(practitioner));
                response.put("token", generateToken("practitioner", practitioner.getId()));
                response.put("redirectTo", "/praticien-dashboard");
                
                return response;
            } else {
                throw new RuntimeException("Mot de passe incorrect");
            }
        } else {
            throw new RuntimeException("Praticien non trouvé");
        }
    }
    
    public Map<String, Object> registerPatient(PatientRegistrationRequest request) {
        // Vérifications
        if (patientRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Un compte avec cet email existe déjà");
        }
        
        // if (patientRepository.existsByPhone(request.getPhone())) { // Commented out if not present
        //     throw new RuntimeException("Un compte avec ce numéro de téléphone existe déjà");
        // }
        
        try {
            // Créer le patient
            Patient patient = new Patient();
            patient.setFirstName(request.getFirstName());
            patient.setLastName(request.getLastName());
            patient.setEmail(request.getEmail());
            patient.setPassword(request.getPassword()); // À hasher en production
            patient.setPhone(request.getPhone());
            patient.setBirthDate(request.getBirthDate());
            patient.setBirthCity(request.getBirthCity());
            patient.setGender(request.getGender());
            patient.setNote(request.getNote());
            patient.setSource(request.getSource());
            patient.setInsurance(request.getInsurance());
            
            // Gérer l'upload de fichier
            if (request.getFile() != null && !request.getFile().isEmpty()) {
                String fileUrl = saveFile(request.getFile(), "patients");
                patient.setNationalCardUrl(fileUrl);
                patient.setNationalCardFileName(request.getFile().getOriginalFilename());
            }
            
            // Sauvegarder le patient
            patient = patientRepository.save(patient);
            
            // Générer et envoyer l'OTP
            String otpCode = generateOTP();
            VerificationCode verificationCode = new VerificationCode(
                patient.getEmail(), otpCode, "PATIENT_REGISTRATION", 10);
            verificationCodeRepository.save(verificationCode);
            
            emailService.sendOTPEmail(patient.getEmail(), otpCode, "Vérification de votre compte patient");
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Inscription réussie! Un code de vérification a été envoyé à votre email");
            response.put("email", patient.getEmail());
            response.put("requiresOTP", true);
            response.put("userId", patient.getId());
            response.put("redirectTo", "/patient-dashboard");
            
            return response;
            
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'inscription: " + e.getMessage());
        }
    }
    
    public Map<String, Object> registerPractitioner(PractitionerRegistrationRequest request) {
        // Vérifications
        if (practitionerRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Un compte avec cet email existe déjà");
        }
        
        // if (practitionerRepository.existsByPhone(request.getPhone())) {
        //     throw new RuntimeException("Un compte avec ce numéro de téléphone existe déjà");
        // }
        
        try {
            // Créer le praticien
            Practitioner practitioner = new Practitioner();
            practitioner.setFirstName(request.getFirstName());
            practitioner.setLastName(request.getLastName());
            practitioner.setEmail(request.getEmail());
            practitioner.setPassword(request.getPassword()); // À hasher en production
            practitioner.setSpecialty(request.getSpecialty());
            practitioner.setPhone(request.getPhone());
            practitioner.setGender(request.getGender());
            practitioner.setSource(request.getSource());
            practitioner.setVerified(false); // En attente de validation admin
            
            // Gérer l'upload de fichier
            if (request.getFile() != null && !request.getFile().isEmpty()) {
                String fileUrl = saveFile(request.getFile(), "practitioners");
                practitioner.setProfessionalCardUrl(fileUrl);
                practitioner.setProfessionalCardFileName(request.getFile().getOriginalFilename());
            }
            
            // Sauvegarder le praticien
            practitioner = practitionerRepository.save(practitioner);
            
            // Générer et envoyer l'OTP
            String otpCode = generateOTP();
            VerificationCode verificationCode = new VerificationCode(
                practitioner.getEmail(), otpCode, "PRACTITIONER_REGISTRATION", 10);
            verificationCodeRepository.save(verificationCode);
            
            emailService.sendOTPEmail(practitioner.getEmail(), otpCode, "Vérification de votre compte praticien");
            
            // Notifier les admins
            emailService.sendNewPractitionerNotificationToAdmins(practitioner);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Inscription réussie! Un code de vérification a été envoyé à votre email. Votre compte sera activé après validation par l'administrateur");
            response.put("email", practitioner.getEmail());
            response.put("requiresOTP", true);
            response.put("userId", practitioner.getId());
            response.put("redirectTo", "/praticien-dashboard");
            
            return response;
            
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'inscription: " + e.getMessage());
        }
    }
    
    public Map<String, Object> verifyOTP(VerificationRequest verificationRequest) {
        Optional<VerificationCode> codeOpt = verificationCodeRepository
            .findByEmailAndCodeAndTypeAndUsedFalse(
                verificationRequest.getEmail(), 
                verificationRequest.getCode(), 
                verificationRequest.getType()
            );
        
        if (codeOpt.isPresent()) {
            VerificationCode code = codeOpt.get();
            
            if (code.isExpired()) {
                throw new RuntimeException("Code OTP expiré");
            }
            
            // Marquer le code comme utilisé
            code.setUsed(true);
            code.setUsedAt(LocalDateTime.now());
            verificationCodeRepository.save(code);
            
            Map<String, Object> response = new HashMap<>();
            
            switch (verificationRequest.getType()) {
                case "ADMIN_LOGIN":
                    Optional<Admin> admin = adminRepository.findByEmail(verificationRequest.getEmail());
                    if (admin.isPresent()) {
                        Admin adminUser = admin.get();
                        // adminUser.setLastLoginAt(LocalDateTime.now()); // Deleted from entity
                        // adminRepository.save(adminUser); // Deleted from entity
                        
                        response.put("message", "Connexion admin réussie");
                        response.put("user", createAdminResponse(adminUser));
                        response.put("token", generateToken("admin", adminUser.getId()));
                        response.put("redirectTo", "/admin-dashboard");
                    }
                    break;
                    
                case "PATIENT_REGISTRATION":
                    Optional<Patient> patient = patientRepository.findByEmail(verificationRequest.getEmail());
                    if (patient.isPresent()) {
                        Patient patientUser = patient.get();
                        patientUser.setVerified(true);
                        patientRepository.save(patientUser);
                        
                        response.put("message", "Compte patient vérifié avec succès");
                        response.put("user", createPatientResponse(patientUser));
                        response.put("token", generateToken("patient", patientUser.getId()));
                        response.put("redirectTo", "/patient-dashboard");
                    }
                    break;
                    
                case "PRACTITIONER_REGISTRATION":
                    Optional<Practitioner> practitioner = practitionerRepository.findByEmail(verificationRequest.getEmail());
                    if (practitioner.isPresent()) {
                        response.put("message", "Email vérifié. Votre compte sera activé après validation par l'administrateur");
                        response.put("redirectTo", "/login");
                    }
                    break;
            }
            
            return response;
        } else {
            throw new RuntimeException("Code OTP incorrect ou expiré");
        }
    }
    
    public void resendOTP(String email, String type) {
        // Supprimer les anciens codes
        verificationCodeRepository.deleteByEmailAndType(email, type);
        
        // Générer un nouveau code
        String otpCode = generateOTP();
        VerificationCode verificationCode = new VerificationCode(email, otpCode, type, 10);
        verificationCodeRepository.save(verificationCode);
        
        // Envoyer le nouveau code
        String subject = getOTPSubject(type);
        emailService.sendOTPEmail(email, otpCode, subject);
    }
    
    public void logout(String token) {
        // Implémenter la logique de déconnexion (blacklist token, etc.)
        // Pour l'instant, on ne fait rien car on n'utilise pas de JWT
    }
    
    public boolean emailExists(String email) {
        return patientRepository.existsByEmail(email) || 
               practitionerRepository.existsByEmail(email) || 
               adminRepository.existsByEmail(email);
    }
    
    // Méthodes utilitaires
    private String saveFile(MultipartFile file, String folder) throws IOException {
        // Créer le dossier s'il n'existe pas
        Path uploadPath = Paths.get(uploadDir + folder);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Générer un nom de fichier unique
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            originalFilename = "unknown_file";
        }
        String fileExtension = originalFilename.contains(".") ? 
            originalFilename.substring(originalFilename.lastIndexOf(".")) : ".tmp";
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
        
        // Sauvegarder le fichier
        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath);
        
        return "/" + uploadDir + folder + "/" + uniqueFilename;
    }
    
    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
    
    private String generateToken(String userType, Long userId) {
        // Implémentation simple - vous devriez utiliser JWT en production
        return userType + "_token_" + userId + "_" + System.currentTimeMillis();
    }
    
    private String getOTPSubject(String type) {
        switch (type) {
            case "ADMIN_LOGIN":
                return "Code de connexion administrateur";
            case "PATIENT_REGISTRATION":
                return "Vérification de votre compte patient";
            case "PRACTITIONER_REGISTRATION":
                return "Vérification de votre compte praticien";
            default:
                return "Code de vérification";
        }
    }
    
    private Map<String, Object> createPatientResponse(Patient patient) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", patient.getId());
        response.put("firstName", patient.getFirstName());
        response.put("lastName", patient.getLastName());
        response.put("email", patient.getEmail());
        response.put("phone", patient.getPhone());
        response.put("gender", patient.getGender());
        response.put("isVerified", patient.isVerified());
        response.put("userType", "patient");
        return response;
    }
    
    private Map<String, Object> createPractitionerResponse(Practitioner practitioner) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", practitioner.getId());
        response.put("firstName", practitioner.getFirstName());
        response.put("lastName", practitioner.getLastName());
        response.put("email", practitioner.getEmail());
        response.put("phone", practitioner.getPhone());
        response.put("specialty", practitioner.getSpecialty());
        response.put("gender", practitioner.getGender());
        response.put("isVerified", practitioner.isVerified());
        response.put("userType", "practitioner");
        return response;
    }
    
    private Map<String, Object> createAdminResponse(Admin admin) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", admin.getId());
        // response.put("firstName", admin.getFirstName()); // Removed since firstName was deleted
        // response.put("lastName", admin.getLastName()); // Removed since lastName was deleted
        response.put("email", admin.getEmail());
        response.put("userType", "admin");
        return response;
    }
}
