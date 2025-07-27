package com.teleconsultation_backend.controllers;

import com.teleconsultation_backend.services.ChatbotService;
import com.teleconsultation_backend.services.ChatbotQuestionsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;
import java.util.Collections;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {
    private final ChatbotService chatbotService;
    private final ChatbotQuestionsService questionsService;

    public ChatbotController(ChatbotService chatbotService, ChatbotQuestionsService questionsService) {
        this.chatbotService = chatbotService;
        this.questionsService = questionsService;
    }

    @PostMapping("/message")
    public ResponseEntity<Map<String, Object>> processMessage(@RequestBody Map<String, String> request) {
        try {
            String message = request.get("message");
            String sessionId = request.get("sessionId");
            if (message == null || message.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Message vide"));
            }
            if (sessionId == null || sessionId.isEmpty()) {
                sessionId = chatbotService.startNewSession();
            }
            Map<String, Object> response = chatbotService.processMessage(message, sessionId);
            response.put("sessionId", sessionId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/start-session")
    public ResponseEntity<Map<String, String>> startChatSession() {
        try {
            String sessionId = chatbotService.startNewSession();
            return ResponseEntity.ok(Map.of(
                "sessionId", sessionId,
                "welcomeMessage", "Bonjour ! Je suis votre assistant virtuel. Comment puis-je vous aider aujourd'hui ?"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/session/{sessionId}/history")
    public ResponseEntity<Map<String, Object>> getChatHistory(@PathVariable String sessionId) {
        try {
            Map<String, Object> history = chatbotService.getChatHistory(sessionId);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/quick-suggestions")
    public ResponseEntity<Map<String, Object>> getQuickSuggestions() {
        try {
            Map<String, Object> suggestions = chatbotService.getQuickSuggestions();
            return ResponseEntity.ok(suggestions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/questions/all")
    public ResponseEntity<Map<String, List<Map<String, String>>>> getAllQuestions() {
        try {
            Map<String, List<Map<String, String>>> questions = questionsService.getAllQuestionsByDomain();
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.emptyMap());
        }
    }

    @GetMapping("/questions/{domainName}")
    public ResponseEntity<List<String>> getQuestionsForDomain(
            @PathVariable String domainName,
            @RequestParam(defaultValue = "5") int count) {
        try {
            List<String> questions = questionsService.getRandomQuestionsForDomain(domainName, count);
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(List.of("Erreur lors de la récupération des questions"));
        }
    }

    @GetMapping("/questions/home-suggestions")
    public ResponseEntity<Map<String, List<String>>> getHomeSuggestions() {
        try {
            Map<String, List<String>> suggestions = Map.of(
                "Santé", questionsService.getRandomQuestionsForDomain("Santé", 3),
                "Droit", questionsService.getRandomQuestionsForDomain("Droit", 3),
                "Esthétique", questionsService.getRandomQuestionsForDomain("Esthétique", 3),
                "Bien-être", questionsService.getRandomQuestionsForDomain("Bien-être", 3),
                "Éducation", questionsService.getRandomQuestionsForDomain("Éducation", 3),
                "Décoration", questionsService.getRandomQuestionsForDomain("Décoration", 3)
            );
            return ResponseEntity.ok(suggestions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", List.of("Erreur lors de la récupération")));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> intelligentSearch(@RequestParam String query) {
        try {
            String sessionId = chatbotService.startNewSession();
            Map<String, Object> response = chatbotService.processMessage(query, sessionId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
