package com.teleconsultation_backend.services;

import com.teleconsultation_backend.entities.Patient;
import com.teleconsultation_backend.dtos.PatientRegistrationRequest;
import com.teleconsultation_backend.entities.VerificationCode;
import com.teleconsultation_backend.repositories.PatientRepository;
import com.teleconsultation_backend.repositories.VerificationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PatientService {
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private VerificationCodeRepository verificationCodeRepository;
    @Autowired
    private EmailService emailService;

    public Patient registerPatient(PatientRegistrationRequest request) throws Exception {
        if (patientRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new Exception("Email already registered");
        }
        Patient patient = new Patient();
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setEmail(request.getEmail());
        patient.setPhone(request.getPhone());
        patient.setBirthDate(request.getBirthDate());
        patient.setBirthCity(request.getBirthCity());
        patient.setGender(request.getGender());
        patient.setNote(request.getNote());
        patient.setSource(request.getSource());
        patient.setInsurance(request.getInsurance());
        patient.setPassword(request.getPassword()); // TODO: hash password

        MultipartFile file = request.getFile();
        if (file != null && !file.isEmpty()) {
            patient.setNationalCard(file.getBytes());
            patient.setNationalCardFileName(file.getOriginalFilename());
            // patient.setNationalCardFileType(file.getContentType()); // This line was commented out
        }
        patient.setVerified(false);
        patientRepository.save(patient);

        // Generate and send verification code
        String code = UUID.randomUUID().toString().substring(0, 6);
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(patient.getEmail());
        verificationCode.setCode(code);
        verificationCode.setType("registration");
        verificationCode.setCreatedAt(LocalDateTime.now());
        verificationCode.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        verificationCode.setUsed(false);
        verificationCodeRepository.save(verificationCode);
        // emailService.sendVerificationCode(patient.getEmail(), code); // Commented out if not present

        return patient;
    }

    public boolean verifyPatient(String email, String code) {
        Optional<VerificationCode> optionalCode = verificationCodeRepository.findTopByEmailAndTypeAndUsedFalseAndExpiresAtAfterOrderByCreatedAtDesc(
                email, "registration", LocalDateTime.now());
        if (optionalCode.isPresent() && optionalCode.get().getCode().equals(code)) {
            VerificationCode verificationCode = optionalCode.get();
            verificationCode.setUsed(true);
            verificationCodeRepository.save(verificationCode);
            Optional<Patient> patientOpt = patientRepository.findByEmail(email);
            if (patientOpt.isPresent()) {
                Patient patient = patientOpt.get();
                patient.setVerified(true);
                patientRepository.save(patient);
                return true;
            }
        }
        return false;
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }
} 