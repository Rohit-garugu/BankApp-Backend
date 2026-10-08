package com.Bank.bank_app.service;

public interface OtpService {

    void sendOtp(String mobileNumber);

    boolean verifyOtp(String mobileNumber, String otp);

    void resendOtp(String mobileNumber);
}
