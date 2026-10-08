package com.Bank.bank_app.service.impl;

import com.Bank.bank_app.dto.PaymentResponse;
import com.Bank.bank_app.dto.UpiPaymentRequest;
import com.Bank.bank_app.entity.Account;
import com.Bank.bank_app.entity.Payment;
import com.Bank.bank_app.entity.Transaction;
import com.Bank.bank_app.exception.ResourceNotFoundException;
import com.Bank.bank_app.repository.AccountRepository;
import com.Bank.bank_app.repository.PaymentRepository;
import com.Bank.bank_app.repository.TransactionRepository;
import com.Bank.bank_app.service.OtpService;
import com.Bank.bank_app.service.PaymentService;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final OtpService otpService;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                             AccountRepository accountRepository,
                             TransactionRepository transactionRepository,
                             OtpService otpService) {
        this.paymentRepository = paymentRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.otpService = otpService;
    }

    @Override
    @Transactional
    public PaymentResponse initiateUpiPayment(UpiPaymentRequest request) {
        try {
            // Verify OTP first
            Account sender = accountRepository.findByUpiId(request.getFromUpiId())
                    .orElseThrow(() -> new ResourceNotFoundException("Sender UPI ID not found: " + request.getFromUpiId()));

            otpService.verifyOtp(sender.getMobileNumber(), request.getOtp());

            // Find receiver
            Account receiver = accountRepository.findByUpiId(request.getToUpiId())
                    .orElseThrow(() -> new ResourceNotFoundException("Receiver UPI ID not found: " + request.getToUpiId()));

            // Validation
            if (request.getAmount() <= 0) {
                throw new RuntimeException("Amount must be greater than zero");
            }

            if (sender.getBalance() < request.getAmount()) {
                throw new RuntimeException("Insufficient balance");
            }

            if (!sender.isMobileVerified()) {
                throw new RuntimeException("Sender mobile number not verified");
            }

            if (!receiver.isMobileVerified()) {
                throw new RuntimeException("Receiver mobile number not verified");
            }

            // Create Payment record
            String transactionId = generateTransactionId();
            Payment payment = new Payment(
                    sender.getUsername(),
                    receiver.getUsername(),
                    request.getAmount(),
                    "UPI",
                    transactionId
            );
            payment.setUpiId(request.getToUpiId());
            payment.setRemarks(request.getRemarks());

            // Perform real-time transfer
            sender.setBalance(sender.getBalance() - request.getAmount());
            receiver.setBalance(receiver.getBalance() + request.getAmount());

            accountRepository.save(sender);
            accountRepository.save(receiver);

            // Create transaction records
            transactionRepository.save(new Transaction(
                    sender.getUsername(),
                    "debit",
                    request.getAmount(),
                    sender.getBalance(),
                    "UPI Transfer to " + request.getToUpiId(),
                    LocalDate.now()
            ));

            transactionRepository.save(new Transaction(
                    receiver.getUsername(),
                    "credit",
                    request.getAmount(),
                    receiver.getBalance(),
                    "UPI Received from " + request.getFromUpiId(),
                    LocalDate.now()
            ));

            // Update payment status
            payment.setStatus("SUCCESS");
            payment.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(payment);

            return new PaymentResponse(
                    payment.getId(),
                    transactionId,
                    "SUCCESS",
                    request.getAmount(),
                    "UPI",
                    payment.getCreatedAt(),
                    "UPI payment successful"
            );

        } catch (Exception e) {
            String transactionId = generateTransactionId();
            Payment failedPayment = new Payment(
                    accountRepository.findByUpiId(request.getFromUpiId())
                            .map(Account::getUsername).orElse("UNKNOWN"),
                    accountRepository.findByUpiId(request.getToUpiId())
                            .map(Account::getUsername).orElse("UNKNOWN"),
                    request.getAmount(),
                    "UPI",
                    transactionId
            );
            failedPayment.setStatus("FAILED");
            failedPayment.setRemarks(e.getMessage());
            failedPayment.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(failedPayment);

            throw new RuntimeException("Payment failed: " + e.getMessage(), e);
        }
    }

    @Override
    public PaymentResponse getPaymentStatus(String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with transaction ID: " + transactionId));

        return new PaymentResponse(
                payment.getId(),
                payment.getTransactionId(),
                payment.getStatus(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getCreatedAt(),
                payment.getRemarks()
        );
    }

    @Override
    public List<Payment> getPaymentHistory(String username) {
        return paymentRepository.findByFromUsernameOrderByCreatedAtDesc(username);
    }

    @Override
    public List<Payment> getSentPayments(String username) {
        return paymentRepository.findByFromUsernameOrderByCreatedAtDesc(username);
    }

    @Override
    public List<Payment> getReceivedPayments(String username) {
        return paymentRepository.findByToUsernameOrderByCreatedAtDesc(username);
    }

    private String generateTransactionId() {
        return "TXN" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
