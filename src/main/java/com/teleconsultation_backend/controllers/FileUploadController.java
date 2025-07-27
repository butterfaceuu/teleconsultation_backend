package com.teleconsultation_backend.controllers;

import com.teleconsultation_backend.entities.Document;
import com.teleconsultation_backend.services.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {
    
    @Autowired
    private FileUploadService fileUploadService;
    
    @PostMapping("/upload")
    public ResponseEntity<Document> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "patientId", required = false) Long patientId,
            @RequestParam(value = "practitionerId", required = false) Long practitionerId,
            @RequestParam(value = "appointmentId", required = false) Long appointmentId,
            @RequestParam("documentType") String documentType,
            @RequestParam(value = "description", required = false) String description) {
        
        try {
            Document document = fileUploadService.uploadDocument(
                file, patientId, practitionerId, appointmentId, documentType, description);
            return ResponseEntity.ok(document);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
