package com.Bank.bank_app.service.impl;

import com.Bank.bank_app.service.OtpService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class OtpServiceImpl implements OtpService {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.phone-number}")
    private String fromPhoneNumber;

    @Value("${otp.length:6}")
    private int otpLength;

    @Value("${otp.expiration-minutes:5}")
    private long otpExpirationMinutes;

    private final RedisTemplate<String, String> redisTemplate;

    public OtpServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void sendOtp(String mobileNumber) {
        try {
            String otp = generateOtp();
            storeOtpInCache(mobileNumber, otp);

            // Initialize Twilio
            Twilio.init(accountSid, authToken);

            // Format phone number to E.164 format
            String formattedNumber = formatPhoneNumber(mobileNumber);

            // Send SMS via Twilio
            Message message = Message.creator(
                    new PhoneNumber(formattedNumber),
                    new PhoneNumber(fromPhoneNumber),
                    "Your BankApp OTP is: " + otp + ". Do not share this with anyone. Valid for 5 minutes."
            ).create();

            System.out.println("OTP sent successfully. SID: " + message.getSid());
        } catch (Exception e) {
            throw new RuntimeException("Failed to send OTP: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean verifyOtp(String mobileNumber, String otp) {
        String storedOtp = redisTemplate.opsForValue().get("OTP:" + mobileNumber);

        if (storedOtp == null) {
            throw new RuntimeException("OTP expired or not found. Please request a new OTP.");
        }

        if (!storedOtp.equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        // OTP verified, delete it from cache
        redisTemplate.delete("OTP:" + mobileNumber);
        return true;
    }

    @Override
    public void resendOtp(String mobileNumber) {
        // Delete old OTP and send new one
        redisTemplate.delete("OTP:" + mobileNumber);
        sendOtp(mobileNumber);
    }

    private String generateOtp() {
        return RandomStringUtils.randomNumeric(otpLength);
    }

    private void storeOtpInCache(String mobileNumber, String otp) {
        redisTemplate.opsForValue().set(
                "OTP:" + mobileNumber,
                otp,
                otpExpirationMinutes,
                TimeUnit.MINUTES
        );
    }

    private String formatPhoneNumber(String mobileNumber) {
        // Convert 10-digit Indian number to E.164 format
        if (mobileNumber.length() == 10) {
            return "+91" + mobileNumber;
        } else if (mobileNumber.startsWith("0")) {
            return "+91" + mobileNumber.substring(1);
        } else if (mobileNumber.startsWith("+")) {
            return mobileNumber;
        } else {
            return "+91" + mobileNumber;
        }
    }
}
