package com.teleconsultation_backend.controllers;

import com.teleconsultation_backend.entities.Payment;
import com.teleconsultation_backend.dtos.PaymentRequest;
import com.teleconsultation_backend.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    
    @Autowired
    private PaymentService paymentService;
    
    @PostMapping("/create-intent")
    public ResponseEntity<Map<String, String>> createPaymentIntent(@RequestBody PaymentRequest request) {
        try {
            Map<String, String> response = paymentService.createPaymentIntent(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/confirm")
    public ResponseEntity<Payment> confirmPayment(@RequestBody Map<String, String> request) {
        try {
            Payment payment = paymentService.confirmPayment(request.get("paymentIntentId"));
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/refund/{paymentId}")
    public ResponseEntity<Payment> refundPayment(@PathVariable Long paymentId) {
        try {
            Payment payment = paymentService.refundPayment(paymentId);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<Payment> getPaymentByAppointment(@PathVariable Long appointmentId) {
        Payment payment = paymentService.getPaymentByAppointment(appointmentId);
        if (payment != null) {
            return ResponseEntity.ok(payment);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Payment>> getPatientPayments(@PathVariable Long patientId) {
        List<Payment> payments = paymentService.getPatientPayments(patientId);
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping("/practitioner/{practitionerId}")
    public ResponseEntity<List<Payment>> getPractitionerPayments(@PathVariable Long practitionerId) {
        List<Payment> payments = paymentService.getPractitionerPayments(practitionerId);
        return ResponseEntity.ok(payments);
    }
}
