package com.teleconsultation_backend.services;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ChatbotQuestionsService {
    
    // ===== QUESTIONS DOMAINE SANTÉ =====
    public List<Map<String, String>> getHealthQuestions() {
        List<Map<String, String>> questions = new ArrayList<>();
        
        questions.add(createQuestion("Je cherche un cardiologue à Casablanca", "cardiologie"));
        questions.add(createQuestion("J'ai des problèmes de peau, qui consulter ?", "dermatologie"));
        questions.add(createQuestion("Mon enfant a de la fièvre, besoin d'un pédiatre", "pédiatrie"));
        questions.add(createQuestion("Je me sens déprimé, je veux voir un psychiatre", "psychiatrie"));
        questions.add(createQuestion("Douleurs au cœur, consultation urgente", "cardiologie"));
        questions.add(createQuestion("Acné persistante, dermatologue disponible ?", "dermatologie"));
        questions.add(createQuestion("Vaccination pour bébé de 6 mois", "pédiatrie"));
        questions.add(createQuestion("Troubles du sommeil et anxiété", "psychiatrie"));
        questions.add(createQuestion("Hypertension artérielle, suivi cardiologique", "cardiologie"));
        questions.add(createQuestion("Eczéma sur les mains, traitement ?", "dermatologie"));
        questions.add(createQuestion("Mon fils refuse de manger", "pédiatrie"));
        questions.add(createQuestion("Attaques de panique fréquentes", "psychiatrie"));
        questions.add(createQuestion("Consultation cardiologie préventive", "cardiologie"));
        questions.add(createQuestion("Taches brunes sur le visage", "dermatologie"));
        questions.add(createQuestion("Développement de mon enfant de 2 ans", "pédiatrie"));
        
        return questions;
    }
    
    // ===== QUESTIONS DOMAINE DROIT =====
    public List<Map<String, String>> getLawQuestions() {
        List<Map<String, String>> questions = new ArrayList<>();
        
        questions.add(createQuestion("Je veux divorcer, besoin d'un avocat", "droit civil"));
        questions.add(createQuestion("Problème avec mon employeur, licenciement abusif", "droit du travail"));
        questions.add(createQuestion("Accusé de vol, besoin d'une défense pénale", "droit pénal"));
        questions.add(createQuestion("Création d'entreprise, conseils juridiques", "droit des affaires"));
        questions.add(createQuestion("Succession de mes parents, partage des biens", "droit civil"));
        questions.add(createQuestion("Harcèlement au travail, que faire ?", "droit du travail"));
        questions.add(createQuestion("Garde à vue, besoin d'un avocat pénaliste", "droit pénal"));
        questions.add(createQuestion("Contrat commercial litigieux", "droit des affaires"));
        questions.add(createQuestion("Pension alimentaire non payée", "droit civil"));
        questions.add(createQuestion("Rupture conventionnelle refusée", "droit du travail"));
        questions.add(createQuestion("Plainte pour diffamation", "droit pénal"));
        questions.add(createQuestion("Associé malhonnête dans ma société", "droit des affaires"));
        questions.add(createQuestion("Voisin qui construit sur mon terrain", "droit civil"));
        questions.add(createQuestion("Discrimination à l'embauche", "droit du travail"));
        questions.add(createQuestion("Escroquerie sur internet", "droit pénal"));
        
        return questions;
    }
    
    // ===== QUESTIONS DOMAINE ESTHÉTIQUE =====
    public List<Map<String, String>> getEstheticQuestions() {
        List<Map<String, String>> questions = new ArrayList<>();
        
        questions.add(createQuestion("Nettoyage de peau en profondeur", "soins visage"));
        questions.add(createQuestion("Épilation laser définitive", "épilation"));
        questions.add(createQuestion("Massage relaxant après le travail", "massage"));
        questions.add(createQuestion("Manucure pour mariage", "beauté mains/pieds"));
        questions.add(createQuestion("Soins anti-âge pour le visage", "soins visage"));
        questions.add(createQuestion("Épilation des jambes à la cire", "épilation"));
        questions.add(createQuestion("Massage drainant pour cellulite", "massage"));
        questions.add(createQuestion("Pédicure avec vernis semi-permanent", "beauté mains/pieds"));
        questions.add(createQuestion("Hydrafacial pour peau terne", "soins visage"));
        questions.add(createQuestion("Épilation du maillot", "épilation"));
        questions.add(createQuestion("Massage thérapeutique du dos", "massage"));
        questions.add(createQuestion("Pose de faux ongles", "beauté mains/pieds"));
        questions.add(createQuestion("Peeling chimique pour cicatrices", "soins visage"));
        questions.add(createQuestion("Épilation laser du visage", "épilation"));
        questions.add(createQuestion("Massage aux pierres chaudes", "massage"));
        
        return questions;
    }
    
    // ===== QUESTIONS DOMAINE BIEN-ÊTRE =====
    public List<Map<String, String>> getBienEtreQuestions() {
        List<Map<String, String>> questions = new ArrayList<>();
        
        questions.add(createQuestion("Manque de confiance en moi", "coaching personnel"));
        questions.add(createQuestion("Reconversion professionnelle", "coaching professionnel"));
        questions.add(createQuestion("Perdre du poids sainement", "nutrition & bien-être"));
        questions.add(createQuestion("Gérer mon stress au travail", "méditation & pleine conscience"));
        questions.add(createQuestion("Développer mon leadership", "coaching professionnel"));
        questions.add(createQuestion("Surmonter une rupture", "coaching personnel"));
        questions.add(createQuestion("Régime alimentaire personnalisé", "nutrition & bien-être"));
        questions.add(createQuestion("Apprendre la méditation", "méditation & pleine conscience"));
        questions.add(createQuestion("Améliorer mes relations", "coaching personnel"));
        questions.add(createQuestion("Négociation salariale", "coaching professionnel"));
        questions.add(createQuestion("Troubles alimentaires", "nutrition & bien-être"));
        questions.add(createQuestion("Insomnie et anxiété", "méditation & pleine conscience"));
        questions.add(createQuestion("Estime de soi après échec", "coaching personnel"));
        questions.add(createQuestion("Management d'équipe", "coaching professionnel"));
        questions.add(createQuestion("Alimentation végétarienne équilibrée", "nutrition & bien-être"));
        
        return questions;
    }
    
    // ===== QUESTIONS DOMAINE ÉDUCATION =====
    public List<Map<String, String>> getEducationQuestions() {
        List<Map<String, String>> questions = new ArrayList<>();
        
        questions.add(createQuestion("Cours de maths niveau lycée", "cours particuliers"));
        questions.add(createQuestion("Formation en comptabilité", "formation professionnelle"));
        questions.add(createQuestion("Apprendre l'anglais rapidement", "langues étrangères"));
        questions.add(createQuestion("Préparation au baccalauréat", "préparation examens"));
        questions.add(createQuestion("Aide aux devoirs primaire", "cours particuliers"));
        questions.add(createQuestion("Certification en informatique", "formation professionnelle"));
        questions.add(createQuestion("Cours d'espagnol débutant", "langues étrangères"));
        questions.add(createQuestion("Préparation concours médecine", "préparation examens"));
        questions.add(createQuestion("Soutien scolaire en français", "cours particuliers"));
        questions.add(createQuestion("Formation en marketing digital", "formation professionnelle"));
        questions.add(createQuestion("Cours d'allemand professionnel", "langues étrangères"));
        questions.add(createQuestion("Préparation TOEFL", "préparation examens"));
        questions.add(createQuestion("Cours de physique-chimie", "cours particuliers"));
        questions.add(createQuestion("Formation en ressources humaines", "formation professionnelle"));
        questions.add(createQuestion("Conversation en anglais", "langues étrangères"));
        
        return questions;
    }
    
    // ===== QUESTIONS DOMAINE DÉCORATION =====
    public List<Map<String, String>> getDecorQuestions() {
        List<Map<String, String>> questions = new ArrayList<>();
        
        questions.add(createQuestion("Décorer mon salon moderne", "décoration résidentielle"));
        questions.add(createQuestion("Organisation de mariage", "décoration événementielle"));
        questions.add(createQuestion("Aménager ma boutique", "design commercial"));
        questions.add(createQuestion("Vendre ma maison rapidement", "staging immobilier"));
        questions.add(createQuestion("Rénovation de ma cuisine", "décoration résidentielle"));
        questions.add(createQuestion("Décoration pour anniversaire", "décoration événementielle"));
        questions.add(createQuestion("Design de restaurant", "design commercial"));
        questions.add(createQuestion("Valoriser mon appartement", "staging immobilier"));
        questions.add(createQuestion("Chambre d'enfant thématique", "décoration résidentielle"));
        questions.add(createQuestion("Décoration de baptême", "décoration événementielle"));
        questions.add(createQuestion("Aménagement bureau professionnel", "design commercial"));
        questions.add(createQuestion("Home staging avant vente", "staging immobilier"));
        questions.add(createQuestion("Style marocain moderne", "décoration résidentielle"));
        questions.add(createQuestion("Mariage traditionnel marocain", "décoration événementielle"));
        questions.add(createQuestion("Showroom pour magasin", "design commercial"));
        
        return questions;
    }
    
    // ===== MÉTHODES UTILITAIRES =====
    
    private Map<String, String> createQuestion(String question, String specialty) {
        Map<String, String> q = new HashMap<>();
        q.put("question", question);
        q.put("specialty", specialty);
        return q;
    }
    
    public Map<String, List<Map<String, String>>> getAllQuestionsByDomain() {
        Map<String, List<Map<String, String>>> allQuestions = new HashMap<>();
        
        allQuestions.put("Santé", getHealthQuestions());
        allQuestions.put("Droit", getLawQuestions());
        allQuestions.put("Esthétique", getEstheticQuestions());
        allQuestions.put("Bien-être", getBienEtreQuestions());
        allQuestions.put("Éducation", getEducationQuestions());
        allQuestions.put("Décoration", getDecorQuestions());
        
        return allQuestions;
    }
    
    public List<String> getRandomQuestionsForDomain(String domainName, int count) {
        List<Map<String, String>> domainQuestions = new ArrayList<>();
        
        switch (domainName.toLowerCase()) {
            case "santé":
                domainQuestions = getHealthQuestions();
                break;
            case "droit":
                domainQuestions = getLawQuestions();
                break;
            case "esthétique":
                domainQuestions = getEstheticQuestions();
                break;
            case "bien-être":
                domainQuestions = getBienEtreQuestions();
                break;
            case "éducation":
                domainQuestions = getEducationQuestions();
                break;
            case "décoration":
                domainQuestions = getDecorQuestions();
                break;
        }
        
        Collections.shuffle(domainQuestions);
        return domainQuestions.stream()
                .limit(count)
                .map(q -> q.get("question"))
                .collect(java.util.stream.Collectors.toList());
    }
}
