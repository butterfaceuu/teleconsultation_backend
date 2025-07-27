package com.teleconsultation_backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/cors")
    public ResponseEntity<Map<String, Object>> testCors() {
        return ResponseEntity.ok(Map.of(
            "message", "CORS is working!",
            "timestamp", System.currentTimeMillis(),
            "status", "success"
        ));
    }

    @PostMapping("/cors")
    public ResponseEntity<Map<String, Object>> testCorsPost(@RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(Map.of(
            "message", "CORS POST is working!",
            "receivedData", request,
            "timestamp", System.currentTimeMillis(),
            "status", "success"
        ));
    }
} 