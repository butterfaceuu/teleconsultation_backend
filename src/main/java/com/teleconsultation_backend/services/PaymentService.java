package com.teleconsultation_backend.services;

import com.teleconsultation_backend.entities.Payment;
import com.teleconsultation_backend.dtos.PaymentRequest;
import com.teleconsultation_backend.repositories.PaymentRepository;
import com.teleconsultation_backend.repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {
    
    private final PaymentRepository paymentRepository;
    private final AppointmentRepository appointmentRepository;
    
    @Autowired
    public PaymentService(PaymentRepository paymentRepository,
                         AppointmentRepository appointmentRepository) {
        this.paymentRepository = paymentRepository;
        this.appointmentRepository = appointmentRepository;
    }
    
    public Map<String, String> createPaymentIntent(PaymentRequest request) {
        Optional<com.teleconsultation_backend.entities.Appointment> appointment = appointmentRepository.findById(request.getAppointmentId());
        if (appointment.isPresent()) {
            // Create payment record
            Payment payment = new Payment();
            payment.setAppointment(appointment.get());
            payment.setAmount(request.getAmount());
            payment.setPaymentMethod(request.getPaymentMethod());
            payment.setStatus("PENDING");
            
            // Generate a mock payment intent ID (in real implementation, use Stripe/PayPal)
            String paymentIntentId = "pi_" + UUID.randomUUID().toString().replace("-", "");
            payment.setPaymentIntentId(paymentIntentId);
            
            paymentRepository.save(payment);
            
            Map<String, String> response = new HashMap<>();
            response.put("paymentIntentId", paymentIntentId);
            response.put("clientSecret", "cs_" + UUID.randomUUID().toString().replace("-", ""));
            
            return response;
        }
        throw new RuntimeException("Appointment not found");
    }
    
    public Payment confirmPayment(String paymentIntentId) {
        Optional<Payment> optionalPayment = paymentRepository.findByPaymentIntentId(paymentIntentId);
        if (optionalPayment.isPresent()) {
            Payment payment = optionalPayment.get();
            payment.setStatus("COMPLETED");
            payment.setCompletedAt(LocalDateTime.now());
            payment.setTransactionId("txn_" + UUID.randomUUID().toString().replace("-", ""));
            
            return paymentRepository.save(payment);
        }
        throw new RuntimeException("Payment not found");
    }
    
    public Payment refundPayment(Long paymentId) {
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);
        if (optionalPayment.isPresent()) {
            Payment payment = optionalPayment.get();
            if (payment.getStatus().equals("COMPLETED")) {
                payment.setStatus("REFUNDED");
                payment.setRefundedAt(LocalDateTime.now());
                return paymentRepository.save(payment);
            }
            throw new RuntimeException("Payment cannot be refunded");
        }
        throw new RuntimeException("Payment not found");
    }
    
    public Payment getPaymentByAppointment(Long appointmentId) {
        Optional<Payment> payment = paymentRepository.findByAppointmentId(appointmentId);
        return payment.orElse(null);
    }
    
    public List<Payment> getPatientPayments(Long patientId) {
        return paymentRepository.findByAppointmentPatientId(patientId);
    }
    
    public List<Payment> getPractitionerPayments(Long practitionerId) {
        return paymentRepository.findByAppointmentPractitionerId(practitionerId);
    }
}
