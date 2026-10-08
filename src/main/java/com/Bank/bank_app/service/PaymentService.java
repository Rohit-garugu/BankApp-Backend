package com.Bank.bank_app.service;

import com.Bank.bank_app.dto.PaymentResponse;
import com.Bank.bank_app.dto.UpiPaymentRequest;
import com.Bank.bank_app.entity.Payment;
import java.util.List;

public interface PaymentService {

    PaymentResponse initiateUpiPayment(UpiPaymentRequest request);

    PaymentResponse getPaymentStatus(String transactionId);

    List<Payment> getPaymentHistory(String username);

    List<Payment> getSentPayments(String username);

    List<Payment> getReceivedPayments(String username);
}
