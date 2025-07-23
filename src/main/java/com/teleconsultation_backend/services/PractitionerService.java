package com.teleconsultation_backend.services;

import com.teleconsultation_backend.dtos.PractitionerRegistrationRequest;
import com.teleconsultation_backend.entities.Practitioner;
import com.teleconsultation_backend.entities.VerificationCode;
import com.teleconsultation_backend.repositories.PractitionerRepository;
import com.teleconsultation_backend.repositories.VerificationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PractitionerService {
    @Autowired
    private PractitionerRepository practitionerRepository;
    @Autowired
    private VerificationCodeRepository verificationCodeRepository;
    @Autowired
    private EmailService emailService;

    public Practitioner registerPractitioner(PractitionerRegistrationRequest request) throws Exception {
        if (practitionerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new Exception("Email already registered");
        }
        Practitioner practitioner = new Practitioner();
        practitioner.setFirstName(request.getFirstName());
        practitioner.setLastName(request.getLastName());
        practitioner.setEmail(request.getEmail());
        practitioner.setSpecialty(request.getSpecialty());
        practitioner.setPhone(request.getPhone());
        practitioner.setGender(request.getGender());
        practitioner.setSource(request.getSource());
        practitioner.setPassword(request.getPassword()); // TODO: hash password

        MultipartFile file = request.getProfessionalCard();
        if (file != null && !file.isEmpty()) {
            practitioner.setProfessionalCard(file.getBytes());
            practitioner.setProfessionalCardFileName(file.getOriginalFilename());
            practitioner.setProfessionalCardFileType(file.getContentType());
        }
        practitioner.setVerified(false);
        practitionerRepository.save(practitioner);

        // Generate and send verification code
        String code = UUID.randomUUID().toString().substring(0, 6);
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(practitioner.getEmail());
        verificationCode.setCode(code);
        verificationCode.setType("registration");
        verificationCode.setCreatedAt(LocalDateTime.now());
        verificationCode.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        verificationCode.setUsed(false);
        verificationCodeRepository.save(verificationCode);
        emailService.sendVerificationCode(practitioner.getEmail(), code);

        return practitioner;
    }

    public boolean verifyPractitioner(String email, String code) {
        Optional<VerificationCode> optionalCode = verificationCodeRepository.findTopByEmailAndTypeAndUsedFalseAndExpiresAtAfterOrderByCreatedAtDesc(
                email, "registration", LocalDateTime.now());
        if (optionalCode.isPresent() && optionalCode.get().getCode().equals(code)) {
            VerificationCode verificationCode = optionalCode.get();
            verificationCode.setUsed(true);
            verificationCodeRepository.save(verificationCode);
            Optional<Practitioner> practitionerOpt = practitionerRepository.findByEmail(email);
            if (practitionerOpt.isPresent()) {
                Practitioner practitioner = practitionerOpt.get();
                practitioner.setVerified(true);
                practitionerRepository.save(practitioner);
                return true;
            }
        }
        return false;
    }

    public List<Practitioner> getAllPractitioners() {
        return practitionerRepository.findAll();
    }

    public Optional<Practitioner> getPractitionerById(Long id) {
        return practitionerRepository.findById(id);
    }

    public boolean activatePractitioner(Long id) {
        Optional<Practitioner> practitionerOpt = practitionerRepository.findById(id);
        if (practitionerOpt.isPresent()) {
            Practitioner practitioner = practitionerOpt.get();
            practitioner.setVerified(true);
            practitionerRepository.save(practitioner);
            return true;
        }
        return false;
    }

    public boolean deactivatePractitioner(Long id) {
        Optional<Practitioner> practitionerOpt = practitionerRepository.findById(id);
        if (practitionerOpt.isPresent()) {
            Practitioner practitioner = practitionerOpt.get();
            practitioner.setVerified(false);
            practitionerRepository.save(practitioner);
            return true;
        }
        return false;
    }

    public Optional<byte[]> getProfessionalCardFile(Long id) {
        Optional<Practitioner> practitionerOpt = practitionerRepository.findById(id);
        return practitionerOpt.map(Practitioner::getProfessionalCard);
    }
} 