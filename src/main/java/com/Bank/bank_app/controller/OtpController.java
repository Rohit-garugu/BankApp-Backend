package com.Bank.bank_app.controller;

import com.Bank.bank_app.dto.OtpRequest;
import com.Bank.bank_app.dto.OtpVerifyRequest;
import com.Bank.bank_app.service.OtpService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/otp")
@CrossOrigin(origins = "http://localhost:4200")
public class OtpController {

    private final OtpService otpService;

    public OtpController(OtpService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendOtp(@Valid @RequestBody OtpRequest request) {
        otpService.sendOtp(request.getMobileNumber());
        Map<String, String> response = new HashMap<>();
        response.put("message", "OTP sent successfully to " + request.getMobileNumber());
        response.put("mobileNumber", request.getMobileNumber());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, String>> verifyOtp(@Valid @RequestBody OtpVerifyRequest request) {
        boolean isVerified = otpService.verifyOtp(request.getMobileNumber(), request.getOtp());
        Map<String, String> response = new HashMap<>();
        response.put("verified", String.valueOf(isVerified));
        response.put("message", "OTP verified successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/resend")
    public ResponseEntity<Map<String, String>> resendOtp(@Valid @RequestBody OtpRequest request) {
        otpService.resendOtp(request.getMobileNumber());
        Map<String, String> response = new HashMap<>();
        response.put("message", "OTP resent successfully to " + request.getMobileNumber());
        return ResponseEntity.ok(response);
    }
}
