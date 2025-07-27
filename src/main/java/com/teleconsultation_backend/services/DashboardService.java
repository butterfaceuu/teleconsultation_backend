package com.teleconsultation_backend.services;

import com.teleconsultation_backend.entities.*;
import com.teleconsultation_backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.*;

@Service
public class DashboardService {
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private PractitionerRepository practitionerRepository;
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    // ===== PATIENT DASHBOARD =====
    public Map<String, Object> getPatientDashboard(Long patientId) {
        Map<String, Object> dashboard = new HashMap<>();
        
        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        if (!patientOpt.isPresent()) {
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
        if (!practitionerOpt.isPresent()) {
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
        
        // Forum topics (simulé)
        List<Map<String, Object>> forumTopics = getForumTopics();
        dashboard.put("forumTopics", forumTopics);
        
        // Remplaçants (simulé)
        List<Map<String, Object>> replacements = getReplacements(practitionerId);
        dashboard.put("replacements", replacements);
        
        return dashboard;
    }
    
    // ===== ADMIN DASHBOARD =====
    public Map<String, Object> getAdminDashboard() {
        Map<String, Object> dashboard = new HashMap<>();
        
        // Utilisateurs (patients et praticiens combinés)
        List<Map<String, Object>> users = getAllUsers();
        dashboard.put("users", users);
        
        // Praticiens en attente de validation
        List<Map<String, Object>> practitioners = getPendingPractitioners();
        dashboard.put("practitioners", practitioners);
        
        // Messages pour modération
        List<Map<String, Object>> messages = getMessagesForModeration();
        dashboard.put("messages", messages);
        
        // Statistiques
        Map<String, Object> stats = getAdminStats();
        dashboard.put("stats", stats);
        
        // Réclamations
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
        
        // Messages simulés pour la démo
        Map<String, Object> msg1 = new HashMap<>();
        msg1.put("contactId", 1);
        msg1.put("text", "Bonjour Docteur, j'ai une douleur.");
        msg1.put("sent", true);
        msg1.put("time", new Date());
        messages.add(msg1);
        
        Map<String, Object> msg2 = new HashMap<>();
        msg2.put("contactId", 1);
        msg2.put("text", "Depuis combien de temps ?");
        msg2.put("sent", false);
        msg2.put("time", new Date());
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
        
        // Messages simulés pour collaboration
        Map<String, Object> msg1 = new HashMap<>();
        msg1.put("text", "Bonjour, avez-vous reçu les résultats du patient X?");
        msg1.put("sent", false);
        msg1.put("time", new Date(System.currentTimeMillis() - 3600000));
        messages.add(msg1);
        
        Map<String, Object> msg2 = new HashMap<>();
        msg2.put("text", "Oui, je les ai reçus ce matin");
        msg2.put("sent", true);
        msg2.put("time", new Date(System.currentTimeMillis() - 1800000));
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
            user.put("profession", practitioner.getSpecialty());
            user.put("status", practitioner.isVerified() ? "active" : "pending");
            users.add(user);
        }
        
        return users;
    }
    
    private List<Map<String, Object>> getPendingPractitioners() {
        List<Map<String, Object>> practitioners = new ArrayList<>();
        
        List<Practitioner> pendingPractitioners = practitionerRepository.findByIsVerifiedFalse();
        for (Practitioner practitioner : pendingPractitioners) {
            Map<String, Object> practData = new HashMap<>();
            practData.put("id", practitioner.getId());
            practData.put("name", "Dr. " + practitioner.getFirstName() + " " + practitioner.getLastName());
            practData.put("specialty", practitioner.getSpecialty());
            practData.put("consultations", 0); // Nouveau praticien
            practData.put("status", "pending");
            practitioners.add(practData);
        }
        
        return practitioners;
    }
    
    private List<Map<String, Object>> getMessagesForModeration() {
        List<Map<String, Object>> messages = new ArrayList<>();
        
        // Messages simulés pour modération
        Map<String, Object> msg1 = new HashMap<>();
        msg1.put("id", 1);
        msg1.put("sender", "Ahmed Hachimi");
        msg1.put("receiver", "Nadia El Mansouri");
        msg1.put("date", "2023-05-15 14:30");
        msg1.put("content", "Bonjour, je voudrais prendre rendez-vous pour une consultation.");
        msg1.put("flagged", false);
        msg1.put("deleted", false);
        messages.add(msg1);
        
        Map<String, Object> msg2 = new HashMap<>();
        msg2.put("id", 2);
        msg2.put("sender", "Anonyme");
        msg2.put("receiver", "Karima Benbrahim");
        msg2.put("date", "2023-05-17 16:45");
        msg2.put("content", "Message inapproprié contenant des insultes.");
        msg2.put("flagged", true);
        msg2.put("deleted", true);
        messages.add(msg2);
        
        return messages;
    }
    
    private Map<String, Object> getAdminStats() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalUsers", patientRepository.count() + practitionerRepository.count());
        stats.put("activePractitioners", practitionerRepository.countByIsVerifiedTrue());
        stats.put("pendingPractitioners", practitionerRepository.countByIsVerifiedFalse());
        stats.put("consultationsToday", getTodayAppointmentsCount());
        stats.put("unresolvedReports", 3); // Simulé
        
        return stats;
    }
    
    private List<Map<String, Object>> getReports() {
        List<Map<String, Object>> reports = new ArrayList<>();
        
        Map<String, Object> report1 = new HashMap<>();
        report1.put("id", 1);
        report1.put("user", "Ahmed Hachimi");
        report1.put("type", "Problème de rendez-vous");
        report1.put("date", "2023-05-10");
        report1.put("content", "Je n'ai pas pu annuler mon rendez-vous via la plateforme.");
        report1.put("status", "pending");
        reports.add(report1);
        
        Map<String, Object> report2 = new HashMap<>();
        report2.put("id", 2);
        report2.put("user", "Laila Belhaj");
        report2.put("type", "Message inapproprié");
        report2.put("date", "2023-05-12");
        report2.put("content", "J'ai reçu un message déplacé d'un autre utilisateur.");
        report2.put("status", "resolved");
        reports.add(report2);
        
        return reports;
    }
    
    private String mapAppointmentStatus(String status) {
        switch (status.toLowerCase()) {
            case "scheduled": return "confirmé";
            case "confirmed": return "confirmé";
            case "cancelled": return "annulé";
            case "completed": return "terminé";
            default: return "en attente";
        }
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
        response.put("email", patient.getEmail());
        response.put("phone", patient.getPhone());
        response.put("gender", patient.getGender());
        response.put("isVerified", patient.isVerified());
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
        return response;
    }
}
