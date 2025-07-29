package com.teleconsultation_backend.services;

import com.teleconsultation_backend.entities.*;
import com.teleconsultation_backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DashboardService {
    
    private final PatientRepository patientRepository;
    private final PractitionerRepository practitionerRepository;
    private final AppointmentRepository appointmentRepository;
    private final PaymentRepository paymentRepository;
    
    @Autowired
    public DashboardService(PatientRepository patientRepository,
                          PractitionerRepository practitionerRepository,
                          AppointmentRepository appointmentRepository,
                          PaymentRepository paymentRepository) {
        this.patientRepository = patientRepository;
        this.practitionerRepository = practitionerRepository;
        this.appointmentRepository = appointmentRepository;
        this.paymentRepository = paymentRepository;
    }
    
    // ===== PATIENT DASHBOARD =====
    public Map<String, Object> getPatientDashboard(Long patientId) {
        Map<String, Object> dashboard = new HashMap<>();
        
        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        if (patientOpt.isEmpty()) {
            throw new RuntimeException("Patient non trouvé");
        }
        
        Patient patient = patientOpt.get();
        dashboard.put("patient", createPatientResponse(patient));
        
        // Rendez-vous à venir (appointments)
        List<Appointment> upcomingAppointments = appointmentRepository
            .findByPatientIdAndAppointmentDateTimeAfterOrderByAppointmentDateTimeAsc(
                patientId, LocalDateTime.now());
        
        List<Map<String, Object>> appointmentsList = new ArrayList<>();
        for (Appointment appt : upcomingAppointments) {
            Map<String, Object> appointmentData = new HashMap<>();
            appointmentData.put("id", appt.getId());
            appointmentData.put("title", "Consultation avec " + appt.getPractitioner().getFirstName() + " " + appt.getPractitioner().getLastName());
            appointmentData.put("date", appt.getAppointmentDateTime().toString());
            appointmentData.put("location", appt.getLocation() != null ? appt.getLocation() : "En ligne");
            appointmentData.put("status", mapAppointmentStatus(appt.getStatus()));
            appointmentsList.add(appointmentData);
        }
        dashboard.put("appointments", appointmentsList);
        
        // Contacts (praticiens avec qui le patient a échangé)
        List<Map<String, Object>> contacts = getPatientContacts(patientId);
        dashboard.put("contacts", contacts);
        
        // Messages pour la messagerie
        List<Map<String, Object>> messages = getPatientMessages(patientId);
        dashboard.put("messages", messages);
        
        // Documents (simulé pour l'instant)
        dashboard.put("documents", new ArrayList<>());
        
        return dashboard;
    }
    
    // ===== PRACTITIONER DASHBOARD =====
    public Map<String, Object> getPractitionerDashboard(Long practitionerId) {
        Map<String, Object> dashboard = new HashMap<>();
        
        Optional<Practitioner> practitionerOpt = practitionerRepository.findById(practitionerId);
        if (practitionerOpt.isEmpty()) {
            throw new RuntimeException("Praticien non trouvé");
        }
        
        Practitioner practitioner = practitionerOpt.get();
        dashboard.put("practitioner", createPractitionerResponse(practitioner));
        
        // Rendez-vous d'aujourd'hui et à venir
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);
        
        List<Appointment> todayAppointments = appointmentRepository
            .findByPractitionerIdAndAppointmentDateTimeBetween(practitionerId, startOfDay, endOfDay);
        dashboard.put("todayAppointments", todayAppointments);
        
        List<Appointment> upcomingAppointments = appointmentRepository
            .findByPractitionerIdAndAppointmentDateTimeAfterOrderByAppointmentDateTimeAsc(
                practitionerId, LocalDateTime.now());
        dashboard.put("upcomingAppointments", upcomingAppointments.stream().limit(10).toList());
        
        // Contacts (autres praticiens pour collaboration)
        List<Map<String, Object>> contacts = getPractitionerContacts(practitionerId);
        dashboard.put("contacts", contacts);
        
        // Messages pour collaboration
        List<Map<String, Object>> messages = getPractitionerMessages(practitionerId);
        dashboard.put("messages", messages);
        
        // Forum topics
        List<Map<String, Object>> forumTopics = getForumTopics();
        dashboard.put("forumTopics", forumTopics);
        
        // Remplacements disponibles
        List<Map<String, Object>> replacements = getReplacements(practitionerId);
        dashboard.put("replacements", replacements);
        
        return dashboard;
    }
    
    // ===== ADMIN DASHBOARD =====
    public Map<String, Object> getAdminDashboard() {
        Map<String, Object> dashboard = new HashMap<>();
        
        // Statistiques générales
        Map<String, Object> stats = getAdminStats();
        dashboard.put("stats", stats);
        
        // Liste des utilisateurs
        List<Map<String, Object>> users = getAllUsers();
        dashboard.put("users", users);
        
        // Praticiens en attente de validation
        List<Map<String, Object>> pendingPractitioners = getPendingPractitioners();
        dashboard.put("pendingPractitioners", pendingPractitioners);
        
        // Messages à modérer
        List<Map<String, Object>> messagesForModeration = getMessagesForModeration();
        dashboard.put("messagesForModeration", messagesForModeration);
        
        // Rapports
        List<Map<String, Object>> reports = getReports();
        dashboard.put("reports", reports);
        
        return dashboard;
    }
    
    // ===== MÉTHODES UTILITAIRES =====
    
    private List<Map<String, Object>> getPatientContacts(Long patientId) {
        List<Map<String, Object>> contacts = new ArrayList<>();
        
        // Récupérer les praticiens avec qui le patient a échangé
        List<Practitioner> practitioners = practitionerRepository.findByIsVerifiedTrue();
        
        for (Practitioner practitioner : practitioners.stream().limit(3).toList()) {
            Map<String, Object> contact = new HashMap<>();
            contact.put("id", practitioner.getId());
            contact.put("name", "Dr. " + practitioner.getFirstName() + " " + practitioner.getLastName());
            contact.put("avatar", "/assets/doctor" + (practitioner.getId() % 3 + 1) + ".jpg");
            contact.put("specialty", practitioner.getSpecialty());
            contacts.add(contact);
        }
        
        return contacts;
    }
    
    private List<Map<String, Object>> getPatientMessages(Long patientId) {
        List<Map<String, Object>> messages = new ArrayList<>();
        
        // Messages simulés pour la démo - utilisant patientId pour personnaliser
        Map<String, Object> msg1 = new HashMap<>();
        msg1.put("contactId", 1);
        msg1.put("text", "Bonjour Docteur, j'ai une douleur.");
        msg1.put("sent", true);
        msg1.put("time", new Date());
        msg1.put("patientId", patientId); // Utilisation du paramètre
        messages.add(msg1);
        
        Map<String, Object> msg2 = new HashMap<>();
        msg2.put("contactId", 1);
        msg2.put("text", "Depuis combien de temps ?");
        msg2.put("sent", false);
        msg2.put("time", new Date());
        msg2.put("patientId", patientId); // Utilisation du paramètre
        messages.add(msg2);
        
        return messages;
    }
    
    private List<Map<String, Object>> getPractitionerContacts(Long practitionerId) {
        List<Map<String, Object>> contacts = new ArrayList<>();
        
        // Autres praticiens pour collaboration
        List<Practitioner> otherPractitioners = practitionerRepository.findByIsVerifiedTrue()
            .stream().filter(p -> !p.getId().equals(practitionerId)).limit(3).toList();
        
        for (Practitioner practitioner : otherPractitioners) {
            Map<String, Object> contact = new HashMap<>();
            contact.put("id", practitioner.getId());
            contact.put("name", "Dr. " + practitioner.getFirstName() + " " + practitioner.getLastName());
            contact.put("avatar", "/assets/doctor" + (practitioner.getId() % 3 + 1) + ".jpg");
            contact.put("specialty", practitioner.getSpecialty());
            contact.put("unread", (int)(Math.random() * 5)); // Messages non lus simulés
            contacts.add(contact);
        }
        
        return contacts;
    }
    
    private List<Map<String, Object>> getPractitionerMessages(Long practitionerId) {
        List<Map<String, Object>> messages = new ArrayList<>();
        
        // Messages simulés pour collaboration - utilisant practitionerId pour personnaliser
        Map<String, Object> msg1 = new HashMap<>();
        msg1.put("text", "Bonjour, avez-vous reçu les résultats du patient X?");
        msg1.put("sent", false);
        msg1.put("time", new Date(System.currentTimeMillis() - 3600000));
        msg1.put("practitionerId", practitionerId); // Utilisation du paramètre
        messages.add(msg1);
        
        Map<String, Object> msg2 = new HashMap<>();
        msg2.put("text", "Oui, je les ai reçus ce matin");
        msg2.put("sent", true);
        msg2.put("time", new Date(System.currentTimeMillis() - 1800000));
        msg2.put("practitionerId", practitionerId); // Utilisation du paramètre
        messages.add(msg2);
        
        return messages;
    }
    
    private List<Map<String, Object>> getForumTopics() {
        List<Map<String, Object>> topics = new ArrayList<>();
        
        Map<String, Object> topic1 = new HashMap<>();
        topic1.put("title", "Cas complexe de diabète");
        topic1.put("lastMessage", "Quelqu'un a une expérience avec...");
        topic1.put("replies", 12);
        topic1.put("lastDate", new Date(System.currentTimeMillis() - 86400000));
        topics.add(topic1);
        
        Map<String, Object> topic2 = new HashMap<>();
        topic2.put("title", "Nouveaux protocoles COVID");
        topic2.put("lastMessage", "Les nouvelles recommandations...");
        topic2.put("replies", 8);
        topic2.put("lastDate", new Date(System.currentTimeMillis() - 172800000));
        topics.add(topic2);
        
        return topics;
    }
    
    private List<Map<String, Object>> getReplacements(Long practitionerId) {
        List<Map<String, Object>> replacements = new ArrayList<>();
        
        Map<String, Object> replacement1 = new HashMap<>();
        replacement1.put("name", "Dr. Nadia");
        replacement1.put("specialty", "Dermatologie");
        replacement1.put("availableFrom", "2025-07-19");
        replacements.add(replacement1);
        
        Map<String, Object> replacement2 = new HashMap<>();
        replacement2.put("name", "Dr. Mehdi");
        replacement2.put("specialty", "ORL");
        replacement2.put("availableFrom", "2025-07-20");
        replacements.add(replacement2);
        
        return replacements;
    }
    
    private List<Map<String, Object>> getAllUsers() {
        List<Map<String, Object>> users = new ArrayList<>();
        
        // Ajouter les patients
        List<Patient> patients = patientRepository.findAll();
        for (Patient patient : patients) {
            Map<String, Object> user = new HashMap<>();
            user.put("id", patient.getId());
            user.put("firstName", patient.getFirstName());
            user.put("lastName", patient.getLastName());
            user.put("fullName", patient.getFirstName() + " " + patient.getLastName());
            user.put("email", patient.getEmail());
            user.put("type", "patient");
            user.put("status", patient.isActive() ? "active" : "inactive");
            users.add(user);
        }
        
        // Ajouter les praticiens
        List<Practitioner> practitioners = practitionerRepository.findAll();
        for (Practitioner practitioner : practitioners) {
            Map<String, Object> user = new HashMap<>();
            user.put("id", practitioner.getId());
            user.put("firstName", practitioner.getFirstName());
            user.put("lastName", practitioner.getLastName());
            user.put("fullName", practitioner.getFirstName() + " " + practitioner.getLastName());
            user.put("email", practitioner.getEmail());
            user.put("type", "practitioner");
            user.put("status", practitioner.isVerified() ? "verified" : "pending");
            user.put("specialty", practitioner.getSpecialty());
            users.add(user);
        }
        
        return users;
    }
    
    private List<Map<String, Object>> getPendingPractitioners() {
        List<Map<String, Object>> pending = new ArrayList<>();
        
        List<Practitioner> practitioners = practitionerRepository.findByIsVerifiedFalse();
        for (Practitioner practitioner : practitioners) {
            Map<String, Object> pendingPractitioner = new HashMap<>();
            pendingPractitioner.put("id", practitioner.getId());
            pendingPractitioner.put("name", practitioner.getFirstName() + " " + practitioner.getLastName());
            pendingPractitioner.put("email", practitioner.getEmail());
            pendingPractitioner.put("specialty", practitioner.getSpecialty());
            pendingPractitioner.put("phone", practitioner.getPhone());
            pendingPractitioner.put("createdAt", practitioner.getCreatedAt());
            pending.add(pendingPractitioner);
        }
        
        return pending;
    }
    
    private List<Map<String, Object>> getMessagesForModeration() {
        List<Map<String, Object>> messages = new ArrayList<>();
        
        // Messages simulés pour la modération
        Map<String, Object> msg1 = new HashMap<>();
        msg1.put("id", 1);
        msg1.put("sender", "Patient123");
        msg1.put("receiver", "Dr. Ahmed");
        msg1.put("content", "Message suspect...");
        msg1.put("reported", true);
        msg1.put("reportedAt", new Date());
        messages.add(msg1);
        
        return messages;
    }
    
    private Map<String, Object> getAdminStats() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalPatients = patientRepository.count();
        long totalPractitioners = practitionerRepository.count();
        long verifiedPractitioners = practitionerRepository.countByIsVerifiedTrue();
        long pendingPractitioners = practitionerRepository.countByIsVerifiedFalse();
        long todayAppointments = getTodayAppointmentsCount();
        long totalPayments = paymentRepository.count(); // Utilisation du paymentRepository
        
        stats.put("totalPatients", totalPatients);
        stats.put("totalPractitioners", totalPractitioners);
        stats.put("verifiedPractitioners", verifiedPractitioners);
        stats.put("pendingPractitioners", pendingPractitioners);
        stats.put("todayAppointments", todayAppointments);
        stats.put("totalPayments", totalPayments);
        
        return stats;
    }
    
    private List<Map<String, Object>> getReports() {
        List<Map<String, Object>> reports = new ArrayList<>();
        
        Map<String, Object> report1 = new HashMap<>();
        report1.put("type", "user_growth");
        report1.put("title", "Croissance des utilisateurs");
        report1.put("value", "+15%");
        report1.put("period", "Ce mois");
        reports.add(report1);
        
        Map<String, Object> report2 = new HashMap<>();
        report2.put("type", "appointment_volume");
        report2.put("title", "Volume de rendez-vous");
        report2.put("value", "1,234");
        report2.put("period", "Cette semaine");
        reports.add(report2);
        
        return reports;
    }
    
    private String mapAppointmentStatus(String status) {
        return switch (status) {
            case "SCHEDULED" -> "Programmé";
            case "CONFIRMED" -> "Confirmé";
            case "CANCELLED" -> "Annulé";
            case "COMPLETED" -> "Terminé";
            case "NO_SHOW" -> "Absent";
            default -> status;
        };
    }
    
    private long getTodayAppointmentsCount() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);
        return appointmentRepository.countByAppointmentDateTimeBetween(startOfDay, endOfDay);
    }
    
    private Map<String, Object> createPatientResponse(Patient patient) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", patient.getId());
        response.put("firstName", patient.getFirstName());
        response.put("lastName", patient.getLastName());
        response.put("fullName", patient.getFirstName() + " " + patient.getLastName());
        response.put("email", patient.getEmail());
        response.put("phone", patient.getPhone());
        response.put("gender", patient.getGender());
        response.put("birthDate", patient.getBirthDate());
        response.put("isActive", patient.isActive());
        return response;
    }
    
    private Map<String, Object> createPractitionerResponse(Practitioner practitioner) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", practitioner.getId());
        response.put("firstName", practitioner.getFirstName());
        response.put("lastName", practitioner.getLastName());
        response.put("fullName", practitioner.getFirstName() + " " + practitioner.getLastName());
        response.put("email", practitioner.getEmail());
        response.put("phone", practitioner.getPhone());
        response.put("specialty", practitioner.getSpecialty());
        response.put("gender", practitioner.getGender());
        response.put("location", practitioner.getLocation());
        response.put("consultationPrice", practitioner.getConsultationPrice());
        response.put("isVerified", practitioner.isVerified());
        response.put("isActive", practitioner.isActive());
        return response;
    }
}
