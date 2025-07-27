package com.teleconsultation_backend.services;

import com.teleconsultation_backend.entities.Domain;
import com.teleconsultation_backend.entities.Specialty;
import com.teleconsultation_backend.entities.Practitioner;
import com.teleconsultation_backend.repositories.DomainRepository;
import com.teleconsultation_backend.repositories.SpecialtyRepository;
import com.teleconsultation_backend.repositories.PractitionerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatbotService {
    
    @Autowired
    private DomainRepository domainRepository;
    
    @Autowired
    private SpecialtyRepository specialtyRepository;
    
    @Autowired
    private PractitionerRepository practitionerRepository;
    
    @Autowired
    private ChatbotQuestionsService questionsService;
    
    // Sessions de chat en mémoire
    private final Map<String, ChatSession> chatSessions = new ConcurrentHashMap<>();
    
    public String startNewSession() {
        String sessionId = "session_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
        ChatSession session = new ChatSession(sessionId);
        chatSessions.put(sessionId, session);
        return sessionId;
    }
    
    public Map<String, Object> processMessage(String message, String sessionId) {
        ChatSession session = chatSessions.get(sessionId);
        if (session == null) {
            session = new ChatSession(sessionId);
            chatSessions.put(sessionId, session);
        }
        
        // Ajouter le message de l'utilisateur à l'historique
        session.addMessage("user", message);
        
        // Analyser le message et générer la réponse
        Map<String, Object> response = analyzeMessage(message, session);
        
        // Ajouter la réponse du chatbot à l'historique
        session.addMessage("bot", response.get("message").toString());
        
        // Préparer la réponse pour le frontend
        Map<String, Object> frontendResponse = new HashMap<>();
        frontendResponse.put("message", response.get("message"));
        frontendResponse.put("type", response.get("type"));
        frontendResponse.put("data", response);
        
        return frontendResponse;
    }
    
    private Map<String, Object> analyzeMessage(String message, ChatSession session) {
        Map<String, Object> response = new HashMap<>();
        String lowerMessage = message.toLowerCase();
        
        // Analyse des mots-clés pour chaque domaine selon votre frontend
        if (containsHealthKeywords(lowerMessage)) {
            return handleHealthDomain(lowerMessage, session);
        } else if (containsLawKeywords(lowerMessage)) {
            return handleLawDomain(lowerMessage, session);
        } else if (containsEstheticKeywords(lowerMessage)) {
            return handleEstheticDomain(lowerMessage, session);
        } else if (containsBienEtreKeywords(lowerMessage)) {
            return handleBienEtreDomain(lowerMessage, session);
        } else if (containsEducationKeywords(lowerMessage)) {
            return handleEducationDomain(lowerMessage, session);
        } else if (containsDecorKeywords(lowerMessage)) {
            return handleDecorDomain(lowerMessage, session);
        } else if (containsAppointmentKeywords(lowerMessage)) {
            return handleAppointmentRequest(lowerMessage, session);
        } else {
            return handleGeneralQuery(lowerMessage, session);
        }
    }
    
    // ===== GESTION DU DOMAINE SANTÉ =====
    private boolean containsHealthKeywords(String message) {
        String[] keywords = {
            "santé", "médecin", "docteur", "consultation", "maladie", "douleur", 
            "symptôme", "traitement", "médicament", "hôpital", "clinique",
            "cardiologie", "cardiologue", "cœur", "coeur", "vaisseaux",
            "dermatologie", "dermatologue", "peau", "cheveux", "ongles", "acné",
            "pédiatrie", "pédiatre", "enfant", "nourrisson", "bébé", "vaccination",
            "psychiatrie", "psychiatre", "santé mentale", "dépression", "anxiété", "stress"
        };
        return containsAnyKeyword(message, keywords);
    }
    
    private Map<String, Object> handleHealthDomain(String message, ChatSession session) {
        Map<String, Object> response = new HashMap<>();
        Domain healthDomain = domainRepository.findByName("Santé").orElse(null);
        
        if (healthDomain != null) {
            List<Specialty> specialties = specialtyRepository.findByDomainIdAndActiveTrue(healthDomain.getId());
            Specialty matchedSpecialty = findMatchingHealthSpecialty(message, specialties);
            
            if (matchedSpecialty != null) {
                List<Practitioner> practitioners = practitionerRepository
                    .findBySpecialtyEntityIdAndIsVerifiedTrue(matchedSpecialty.getId());
                
                response.put("type", "specialty_match");
                response.put("domain", healthDomain);
                response.put("specialty", matchedSpecialty);
                response.put("practitioners", practitioners);
                response.put("message", "J'ai trouvé " + practitioners.size() + " spécialiste(s) en " + 
                    matchedSpecialty.getName() + ". Voulez-vous voir leurs profils ?");
                
                // Ajouter des questions suggérées
                response.put("suggestedQuestions", questionsService.getRandomQuestionsForDomain("Santé", 3));
            } else {
                response.put("type", "domain_suggestion");
                response.put("domain", healthDomain);
                response.put("specialties", specialties);
                response.put("message", "Je vois que vous cherchez un professionnel de santé. " +
                    "Voici nos spécialités médicales disponibles : " +
                    "Cardiologie, Dermatologie, Pédiatrie, Psychiatrie. " +
                    "Quelle spécialité vous intéresse ?");
            }
        }
        
        return response;
    }
    
    private Specialty findMatchingHealthSpecialty(String message, List<Specialty> specialties) {
        if (message.contains("cardiologie") || message.contains("cardiologue") || 
            message.contains("cœur") || message.contains("coeur") || message.contains("cardiaque")) {
            return specialties.stream().filter(s -> s.getName().equals("Cardiologie")).findFirst().orElse(null);
        }
        if (message.contains("dermatologie") || message.contains("dermatologue") || 
            message.contains("peau") || message.contains("acné") || message.contains("eczéma")) {
            return specialties.stream().filter(s -> s.getName().equals("Dermatologie")).findFirst().orElse(null);
        }
        if (message.contains("pédiatrie") || message.contains("pédiatre") || 
            message.contains("enfant") || message.contains("bébé") || message.contains("vaccination")) {
            return specialties.stream().filter(s -> s.getName().equals("Pédiatrie")).findFirst().orElse(null);
        }
        if (message.contains("psychiatrie") || message.contains("psychiatre") || 
            message.contains("santé mentale") || message.contains("dépression") || message.contains("anxiété")) {
            return specialties.stream().filter(s -> s.getName().equals("Psychiatrie")).findFirst().orElse(null);
        }
        return null;
    }
    
    // ===== GESTION DU DOMAINE DROIT =====
    private boolean containsLawKeywords(String message) {
        String[] keywords = {
            "droit", "avocat", "juridique", "justice", "tribunal", "procès", "contrat",
            "divorce", "succession", "civil", "pénal", "travail", "affaires",
            "licenciement", "harcèlement", "garde à vue", "diffamation"
        };
        return containsAnyKeyword(message, keywords);
    }
    
    private Map<String, Object> handleLawDomain(String message, ChatSession session) {
        Map<String, Object> response = new HashMap<>();
        Domain lawDomain = domainRepository.findByName("Droit").orElse(null);
        
        if (lawDomain != null) {
            List<Specialty> specialties = specialtyRepository.findByDomainIdAndActiveTrue(lawDomain.getId());
            Specialty matchedSpecialty = findMatchingLawSpecialty(message, specialties);
            
            if (matchedSpecialty != null) {
                List<Practitioner> practitioners = practitionerRepository
                    .findBySpecialtyEntityIdAndIsVerifiedTrue(matchedSpecialty.getId());
                
                response.put("type", "specialty_match");
                response.put("domain", lawDomain);
                response.put("specialty", matchedSpecialty);
                response.put("practitioners", practitioners);
                response.put("message", "Voici " + practitioners.size() + " avocat(s) spécialisé(s) en " + 
                    matchedSpecialty.getName() + " disponibles pour vous conseiller.");
                
                response.put("suggestedQuestions", questionsService.getRandomQuestionsForDomain("Droit", 3));
            } else {
                response.put("type", "domain_suggestion");
                response.put("domain", lawDomain);
                response.put("specialties", specialties);
                response.put("message", "Vous avez besoin d'aide juridique. " +
                    "Nos spécialités : Droit Civil, Droit Pénal, Droit du Travail, Droit des Affaires. " +
                    "Dans quel domaine avez-vous besoin d'assistance ?");
            }
        }
        
        return response;
    }
    
    private Specialty findMatchingLawSpecialty(String message, List<Specialty> specialties) {
        if (message.contains("divorce") || message.contains("succession") || 
            message.contains("civil") || message.contains("famille")) {
            return specialties.stream().filter(s -> s.getName().equals("Droit Civil")).findFirst().orElse(null);
        }
        if (message.contains("pénal") || message.contains("criminel") || 
            message.contains("garde à vue") || message.contains("vol") || message.contains("diffamation")) {
            return specialties.stream().filter(s -> s.getName().equals("Droit Pénal")).findFirst().orElse(null);
        }
        if (message.contains("travail") || message.contains("licenciement") || 
            message.contains("employeur") || message.contains("harcèlement")) {
            return specialties.stream().filter(s -> s.getName().equals("Droit du Travail")).findFirst().orElse(null);
        }
        if (message.contains("affaires") || message.contains("entreprise") || 
            message.contains("commercial") || message.contains("société")) {
            return specialties.stream().filter(s -> s.getName().equals("Droit des Affaires")).findFirst().orElse(null);
        }
        return null;
    }
    
    // ===== GESTION DU DOMAINE ESTHÉTIQUE =====
    private boolean containsEstheticKeywords(String message) {
        String[] keywords = {
            "esthétique", "beauté", "esthéticienne", "soins", "visage", "épilation",
            "massage", "manucure", "pédicure", "nettoyage", "laser", "cire"
        };
        return containsAnyKeyword(message, keywords);
    }
    
    private Map<String, Object> handleEstheticDomain(String message, ChatSession session) {
        Map<String, Object> response = new HashMap<>();
        Domain estheticDomain = domainRepository.findByName("Esthétique").orElse(null);
        
        if (estheticDomain != null) {
            List<Specialty> specialties = specialtyRepository.findByDomainIdAndActiveTrue(estheticDomain.getId());
            Specialty matchedSpecialty = findMatchingEstheticSpecialty(message, specialties);
            
            if (matchedSpecialty != null) {
                List<Practitioner> practitioners = practitionerRepository
                    .findBySpecialtyEntityIdAndIsVerifiedTrue(matchedSpecialty.getId());
                
                response.put("type", "specialty_match");
                response.put("domain", estheticDomain);
                response.put("specialty", matchedSpecialty);
                response.put("practitioners", practitioners);
                response.put("message", "Parfait ! J'ai trouvé " + practitioners.size() + 
                    " esthéticienne(s) spécialisée(s) en " + matchedSpecialty.getName() + ".");
                
                response.put("suggestedQuestions", questionsService.getRandomQuestionsForDomain("Esthétique", 3));
            } else {
                response.put("type", "domain_suggestion");
                response.put("domain", estheticDomain);
                response.put("specialties", specialties);
                response.put("message", "Vous cherchez des services esthétiques. " +
                    "Nos spécialités : Soins Visage, Épilation, Massage, Beauté Mains/Pieds. " +
                    "Quel service vous intéresse ?");
            }
        }
        
        return response;
    }
    
    private Specialty findMatchingEstheticSpecialty(String message, List<Specialty> specialties) {
        if (message.contains("visage") || message.contains("nettoyage") || 
            message.contains("peeling") || message.contains("hydrafacial")) {
            return specialties.stream().filter(s -> s.getName().equals("Soins Visage")).findFirst().orElse(null);
        }
        if (message.contains("épilation") || message.contains("laser") || message.contains("cire")) {
            return specialties.stream().filter(s -> s.getName().equals("Épilation")).findFirst().orElse(null);
        }
        if (message.contains("massage") && !message.contains("mains") && !message.contains("pieds")) {
            return specialties.stream().filter(s -> s.getName().equals("Massage")).findFirst().orElse(null);
        }
        if (message.contains("manucure") || message.contains("pédicure") || 
            message.contains("mains") || message.contains("pieds") || message.contains("ongles")) {
            return specialties.stream().filter(s -> s.getName().equals("Beauté Mains/Pieds")).findFirst().orElse(null);
        }
        return null;
    }
    
    // ===== GESTION DU DOMAINE BIEN-ÊTRE =====
    private boolean containsBienEtreKeywords(String message) {
        String[] keywords = {
            "bien-être", "bien être", "coach", "coaching", "développement personnel",
            "confiance", "carrière", "leadership", "nutrition", "méditation", "stress"
        };
        return containsAnyKeyword(message, keywords);
    }
    
    private Map<String, Object> handleBienEtreDomain(String message, ChatSession session) {
        Map<String, Object> response = new HashMap<>();
        Domain bienEtreDomain = domainRepository.findByName("Bien-être").orElse(null);
        
        if (bienEtreDomain != null) {
            List<Specialty> specialties = specialtyRepository.findByDomainIdAndActiveTrue(bienEtreDomain.getId());
            Specialty matchedSpecialty = findMatchingBienEtreSpecialty(message, specialties);
            
            if (matchedSpecialty != null) {
                List<Practitioner> practitioners = practitionerRepository
                    .findBySpecialtyEntityIdAndIsVerifiedTrue(matchedSpecialty.getId());
                
                response.put("type", "specialty_match");
                response.put("domain", bienEtreDomain);
                response.put("specialty", matchedSpecialty);
                response.put("practitioners", practitioners);
                response.put("message", "Excellent choix ! Voici " + practitioners.size() + 
                    " coach(es) spécialisé(s) en " + matchedSpecialty.getName() + ".");
                
                response.put("suggestedQuestions", questionsService.getRandomQuestionsForDomain("Bien-être", 3));
            } else {
                response.put("type", "domain_suggestion");
                response.put("domain", bienEtreDomain);
                response.put("specialties", specialties);
                response.put("message", "Je comprends que vous cherchez à améliorer votre bien-être. " +
                    "Nos domaines de coaching : Personnel, Professionnel, Nutrition & Bien-être, Méditation & Pleine Conscience. " +
                    "Lequel vous intéresse ?");
            }
        }
        
        return response;
    }
    
    private Specialty findMatchingBienEtreSpecialty(String message, List<Specialty> specialties) {
        if (message.contains("coaching personnel") || message.contains("développement personnel") || 
            message.contains("confiance en soi") || message.contains("confiance")) {
            return specialties.stream().filter(s -> s.getName().equals("Coaching Personnel")).findFirst().orElse(null);
        }
        if (message.contains("coaching professionnel") || message.contains("carrière") || 
            message.contains("leadership") || message.contains("reconversion")) {
            return specialties.stream().filter(s -> s.getName().equals("Coaching Professionnel")).findFirst().orElse(null);
        }
        if (message.contains("nutrition") || message.contains("alimentaire") || 
            message.contains("régime") || message.contains("poids")) {
            return specialties.stream().filter(s -> s.getName().equals("Nutrition & Bien-être")).findFirst().orElse(null);
        }
        if (message.contains("méditation") || message.contains("pleine conscience") || 
            message.contains("stress") || message.contains("relaxation")) {
            return specialties.stream().filter(s -> s.getName().equals("Méditation & Pleine Conscience")).findFirst().orElse(null);
        }
        return null;
    }
    
    // ===== GESTION DU DOMAINE ÉDUCATION =====
    private boolean containsEducationKeywords(String message) {
        String[] keywords = {
            "éducation", "cours", "formation", "apprentissage", "enseignement", "professeur",
            "soutien scolaire", "langue", "mathématiques", "préparation", "examen", "bac"
        };
        return containsAnyKeyword(message, keywords);
    }
    
    private Map<String, Object> handleEducationDomain(String message, ChatSession session) {
        Map<String, Object> response = new HashMap<>();
        Domain educationDomain = domainRepository.findByName("Éducation").orElse(null);
        
        if (educationDomain != null) {
            List<Specialty> specialties = specialtyRepository.findByDomainIdAndActiveTrue(educationDomain.getId());
            Specialty matchedSpecialty = findMatchingEducationSpecialty(message, specialties);
            
            if (matchedSpecialty != null) {
                List<Practitioner> practitioners = practitionerRepository
                    .findBySpecialtyEntityIdAndIsVerifiedTrue(matchedSpecialty.getId());
                
                response.put("type", "specialty_match");
                response.put("domain", educationDomain);
                response.put("specialty", matchedSpecialty);
                response.put("practitioners", practitioners);
                response.put("message", "Parfait ! J'ai trouvé " + practitioners.size() + 
                    " enseignant(s) spécialisé(s) en " + matchedSpecialty.getName() + ".");
                
                response.put("suggestedQuestions", questionsService.getRandomQuestionsForDomain("Éducation", 3));
            } else {
                response.put("type", "domain_suggestion");
                response.put("domain", educationDomain);
                response.put("specialties", specialties);
                response.put("message", "Vous souhaitez apprendre ou vous former ? " +
                    "Nos domaines : Cours Particuliers, Formation Professionnelle, Langues Étrangères, Préparation Examens. " +
                    "Que recherchez-vous ?");
            }
        }
        
        return response;
    }
    
    private Specialty findMatchingEducationSpecialty(String message, List<Specialty> specialties) {
        if (message.contains("cours particuliers") || message.contains("soutien scolaire") || 
            message.contains("aide aux devoirs") || message.contains("mathématiques")) {
            return specialties.stream().filter(s -> s.getName().equals("Cours Particuliers")).findFirst().orElse(null);
        }
        if (message.contains("formation professionnelle") || message.contains("certifiante") || 
            message.contains("adultes") || message.contains("comptabilité")) {
            return specialties.stream().filter(s -> s.getName().equals("Formation Professionnelle")).findFirst().orElse(null);
        }
        if (message.contains("langue") || message.contains("anglais") || 
            message.contains("français") || message.contains("espagnol")) {
            return specialties.stream().filter(s -> s.getName().equals("Langues Étrangères")).findFirst().orElse(null);
        }
        if (message.contains("préparation") || message.contains("examen") || 
            message.contains("bac") || message.contains("concours")) {
            return specialties.stream().filter(s -> s.getName().equals("Préparation Examens")).findFirst().orElse(null);
        }
        return null;
    }
    
    // ===== GESTION DU DOMAINE DÉCORATION =====
    private boolean containsDecorKeywords(String message) {
        String[] keywords = {
            "décoration", "design", "décorateur", "aménagement", "intérieur",
            "maison", "appartement", "mariage", "événement", "boutique", "staging"
        };
        return containsAnyKeyword(message, keywords);
    }
    
    private Map<String, Object> handleDecorDomain(String message, ChatSession session) {
        Map<String, Object> response = new HashMap<>();
        Domain decorDomain = domainRepository.findByName("Décoration").orElse(null);
        
        if (decorDomain != null) {
            List<Specialty> specialties = specialtyRepository.findByDomainIdAndActiveTrue(decorDomain.getId());
            Specialty matchedSpecialty = findMatchingDecorSpecialty(message, specialties);
            
            if (matchedSpecialty != null) {
                List<Practitioner> practitioners = practitionerRepository
                    .findBySpecialtyEntityIdAndIsVerifiedTrue(matchedSpecialty.getId());
                
                response.put("type", "specialty_match");
                response.put("domain", decorDomain);
                response.put("specialty", matchedSpecialty);
                response.put("practitioners", practitioners);
                response.put("message", "Magnifique ! Voici " + practitioners.size() + 
                    " décorateur(s) spécialisé(s) en " + matchedSpecialty.getName() + ".");
                
                response.put("suggestedQuestions", questionsService.getRandomQuestionsForDomain("Décoration", 3));
            } else {
                response.put("type", "domain_suggestion");
                response.put("domain", decorDomain);
                response.put("specialties", specialties);
                response.put("message", "Vous voulez embellir votre espace ? " +
                    "Nos services : Décoration Résidentielle, Décoration Événementielle, Design Commercial, Staging Immobilier. " +
                    "Quel type de projet avez-vous ?");
            }
        }
        
        return response;
    }
    
    private Specialty findMatchingDecorSpecialty(String message, List<Specialty> specialties) {
        if (message.contains("résidentielle") || message.contains("maison") || 
            message.contains("appartement") || message.contains("salon") || message.contains("cuisine")) {
            return specialties.stream().filter(s -> s.getName().equals("Décoration Résidentielle")).findFirst().orElse(null);
        }
        if (message.contains("événementielle") || message.contains("mariage") || 
            message.contains("fête") || message.contains("anniversaire")) {
            return specialties.stream().filter(s -> s.getName().equals("Décoration Événementielle")).findFirst().orElse(null);
        }
        if (message.contains("commercial") || message.contains("boutique") || 
            message.contains("restaurant") || message.contains("bureau")) {
            return specialties.stream().filter(s -> s.getName().equals("Design Commercial")).findFirst().orElse(null);
        }
        if (message.contains("staging") || message.contains("immobilier") || 
            message.contains("vendre") || message.contains("valoriser")) {
            return specialties.stream().filter(s -> s.getName().equals("Staging Immobilier")).findFirst().orElse(null);
        }
        return null;
    }
    
    // ===== GESTION DES RENDEZ-VOUS =====
    private boolean containsAppointmentKeywords(String message) {
        String[] keywords = {
            "rendez-vous", "rdv", "consultation", "réserver", "prendre", "planifier",
            "disponibilité", "créneau", "horaire", "quand", "aujourd'hui", "demain"
        };
        return containsAnyKeyword(message, keywords);
    }
    
    private Map<String, Object> handleAppointmentRequest(String message, ChatSession session) {
        Map<String, Object> response = new HashMap<>();
        
        response.put("type", "appointment_help");
        response.put("message", "Je peux vous aider à prendre rendez-vous ! " +
            "Quel type de professionnel recherchez-vous ? " +
            "Vous pouvez choisir parmi : Santé, Droit, Esthétique, Bien-être, Éducation, ou Décoration.");
        response.put("domains", domainRepository.findByActiveTrue());
        response.put("quickActions", Arrays.asList(
            "Cardiologue",
            "Avocat en droit civil",
            "Esthéticienne pour soins visage",
            "Coach personnel",
            "Professeur particulier",
            "Décorateur d'intérieur"
        ));
        
        return response;
    }
    
    // ===== GESTION DES REQUÊTES GÉNÉRALES =====
    private Map<String, Object> handleGeneralQuery(String message, ChatSession session) {
        Map<String, Object> response = new HashMap<>();
        
        if (message.contains("bonjour") || message.contains("salut") || message.contains("hello")) {
            response.put("type", "greeting");
            response.put("message", "Bonjour ! Je suis votre assistant virtuel EliteCom. " +
                "Je peux vous aider à trouver le bon professionnel parmi nos 6 domaines : " +
                "Santé, Droit, Esthétique, Bien-être, Éducation et Décoration. " +
                "Comment puis-je vous aider aujourd'hui ?");
        } else if (message.contains("aide") || message.contains("help")) {
            response.put("type", "help");
            response.put("message", "Je peux vous aider à :\n" +
                "🏥 Trouver un médecin (Cardiologie, Dermatologie, Pédiatrie, Psychiatrie)\n" +
                "⚖️ Consulter un avocat (Droit Civil, Pénal, du Travail, des Affaires)\n" +
                "💄 Réserver des soins esthétiques (Visage, Épilation, Massage, Beauté)\n" +
                "🧘 Trouver un coach bien-être (Personnel, Professionnel, Nutrition, Méditation)\n" +
                "📚 Cours et formations (Particuliers, Professionnelle, Langues, Examens)\n" +
                "🏠 Services de décoration (Résidentielle, Événementielle, Commercial, Staging)\n\n" +
                "Dites-moi simplement ce que vous cherchez !");
        } else {
            response.put("type", "general");
            response.put("message", "Je ne suis pas sûr de comprendre votre demande. " +
                "Pouvez-vous me dire quel type de service vous recherchez ? " +
                "Par exemple : 'Je cherche un cardiologue' ou 'J'ai besoin d'un avocat'.");
            
            // Ajouter des suggestions de questions
            List<String> suggestions = new ArrayList<>();
            suggestions.addAll(questionsService.getRandomQuestionsForDomain("Santé", 2));
            suggestions.addAll(questionsService.getRandomQuestionsForDomain("Droit", 2));
            suggestions.addAll(questionsService.getRandomQuestionsForDomain("Esthétique", 2));
            response.put("suggestedQuestions", suggestions);
        }
        
        response.put("domains", domainRepository.findByActiveTrue());
        return response;
    }
    
    // ===== MÉTHODES UTILITAIRES =====
    
    private boolean containsAnyKeyword(String message, String[] keywords) {
        for (String keyword : keywords) {
            if (message.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
    
    public Map<String, Object> getChatHistory(String sessionId) {
        ChatSession session = chatSessions.get(sessionId);
        Map<String, Object> history = new HashMap<>();
        
        if (session != null) {
            history.put("sessionId", sessionId);
            history.put("messages", session.getMessages());
            history.put("startTime", session.getStartTime());
        } else {
            history.put("error", "Session non trouvée");
        }
        
        return history;
    }
    
    public Map<String, Object> getQuickSuggestions() {
        Map<String, Object> suggestions = new HashMap<>();
        
        suggestions.put("greetings", Arrays.asList(
            "Bonjour, je cherche un cardiologue",
            "J'ai besoin d'un avocat en droit civil",
            "Je veux des soins du visage",
            "Je cherche un coach personnel",
            "Je veux des cours particuliers",
            "Je veux décorer mon salon"
        ));
        
        suggestions.put("domains", Arrays.asList(
            "Santé - Cardiologie, Dermatologie, Pédiatrie, Psychiatrie",
            "Droit - Civil, Pénal, Travail, Affaires",
            "Esthétique - Soins Visage, Épilation, Massage, Beauté Mains/Pieds",
            "Bien-être - Coaching Personnel/Professionnel, Nutrition, Méditation",
            "Éducation - Cours Particuliers, Formation Pro, Langues, Examens",
            "Décoration - Résidentielle, Événementielle, Commercial, Staging"
        ));
        
        return suggestions;
    }
    
    // Classe interne pour gérer les sessions de chat
    private static class ChatSession {
        private final String sessionId;
        private final Date startTime;
        private final List<Map<String, Object>> messages;
        
        public ChatSession(String sessionId) {
            this.sessionId = sessionId;
            this.startTime = new Date();
            this.messages = new ArrayList<>();
        }
        
        public void addMessage(String sender, String content) {
            Map<String, Object> message = new HashMap<>();
            message.put("sender", sender);
            message.put("content", content);
            message.put("timestamp", new Date());
            messages.add(message);
        }
        
        public String getSessionId() { return sessionId; }
        public Date getStartTime() { return startTime; }
        public List<Map<String, Object>> getMessages() { return messages; }
    }
}
