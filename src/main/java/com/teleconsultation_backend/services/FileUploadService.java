package com.teleconsultation_backend.services;

import com.teleconsultation_backend.entities.Document;
import com.teleconsultation_backend.entities.DocumentType;
import com.teleconsultation_backend.repositories.DocumentRepository;
import com.teleconsultation_backend.repositories.PatientRepository;
import com.teleconsultation_backend.repositories.PractitionerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadService {
    
    private final DocumentRepository documentRepository;
    private final PatientRepository patientRepository;
    private final PractitionerRepository practitionerRepository;
    
    @Autowired
    public FileUploadService(DocumentRepository documentRepository,
                            PatientRepository patientRepository,
                            PractitionerRepository practitionerRepository) {
        this.documentRepository = documentRepository;
        this.patientRepository = patientRepository;
        this.practitionerRepository = practitionerRepository;
    }
    
    private final String uploadDir = "uploads/";
    
    public Document uploadDocument(MultipartFile file, Long patientId, Long practitionerId, 
                                 Long appointmentId, String documentType, String description) throws IOException {
        
        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
        
        // Save file to disk
        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath);
        
        // Create document record
        Document document = new Document();
        document.setFileName(originalFilename);
        document.setFileUrl("/uploads/" + uniqueFilename);
        document.setDocumentType(DocumentType.valueOf(documentType));
        document.setDescription(description);
        
        if (patientId != null) {
            document.setPatient(patientRepository.findById(patientId).orElse(null));
        }
        
        if (practitionerId != null) {
            document.setPractitioner(practitionerRepository.findById(practitionerId).orElse(null));
        }
        
        return documentRepository.save(document);
    }
}
