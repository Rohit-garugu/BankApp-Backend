package com.Bank.bank_app.controller;

import com.Bank.bank_app.dto.PaymentResponse;
import com.Bank.bank_app.dto.UpiPaymentRequest;
import com.Bank.bank_app.entity.Payment;
import com.Bank.bank_app.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/upi")
    public ResponseEntity<PaymentResponse> initiateUpiPayment(@Valid @RequestBody UpiPaymentRequest request) {
        PaymentResponse response = paymentService.initiateUpiPayment(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{transactionId}")
    public ResponseEntity<PaymentResponse> getPaymentStatus(@PathVariable String transactionId) {
        PaymentResponse response = paymentService.getPaymentStatus(transactionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history/{username}")
    public ResponseEntity<List<Payment>> getPaymentHistory(@PathVariable String username) {
        List<Payment> payments = paymentService.getPaymentHistory(username);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/sent/{username}")
    public ResponseEntity<List<Payment>> getSentPayments(@PathVariable String username) {
        List<Payment> payments = paymentService.getSentPayments(username);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/received/{username}")
    public ResponseEntity<List<Payment>> getReceivedPayments(@PathVariable String username) {
        List<Payment> payments = paymentService.getReceivedPayments(username);
        return ResponseEntity.ok(payments);
    }
}
