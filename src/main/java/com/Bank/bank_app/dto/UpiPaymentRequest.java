package com.Bank.bank_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class UpiPaymentRequest {

    @NotBlank(message = "From UPI ID is required")
    private String fromUpiId;

    @NotBlank(message = "To UPI ID is required")
    private String toUpiId;

    @Positive(message = "Amount must be greater than zero")
    private double amount;

    @NotBlank(message = "OTP is required")
    private String otp;

    private String remarks;

    public String getFromUpiId() {
        return fromUpiId;
    }

    public void setFromUpiId(String fromUpiId) {
        this.fromUpiId = fromUpiId;
    }

    public String getToUpiId() {
        return toUpiId;
    }

    public void setToUpiId(String toUpiId) {
        this.toUpiId = toUpiId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
