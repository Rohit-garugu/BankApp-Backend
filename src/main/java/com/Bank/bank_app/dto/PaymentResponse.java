package com.Bank.bank_app.dto;

import java.time.LocalDateTime;

public class PaymentResponse {

    private Long id;
    private String transactionId;
    private String status;
    private double amount;
    private String paymentMethod;
    private LocalDateTime createdAt;
    private String remarks;

    public PaymentResponse(Long id, String transactionId, String status, double amount, String paymentMethod, LocalDateTime createdAt, String remarks) {
        this.id = id;
        this.transactionId = transactionId;
        this.status = status;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.createdAt = createdAt;
        this.remarks = remarks;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
