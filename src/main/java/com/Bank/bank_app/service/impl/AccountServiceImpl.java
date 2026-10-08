package com.Bank.bank_app.service.impl;

import com.Bank.bank_app.dto.Account_dto;
import com.Bank.bank_app.entity.Account;
import com.Bank.bank_app.entity.Transaction;
import com.Bank.bank_app.exception.ResourceNotFoundException;
import com.Bank.bank_app.mapper.AccountMapper;
import com.Bank.bank_app.repository.AccountRepository;
import com.Bank.bank_app.repository.TransactionRepository;
import com.Bank.bank_app.service.AccountService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repo;
    private final TransactionRepository txnRepo;
    private final PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AccountRepository repo, TransactionRepository txnRepo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.txnRepo = txnRepo;
        this.passwordEncoder = passwordEncoder;
    }

    private String generateAccountNumber() {
        return "AC" + System.currentTimeMillis();
    }

    private String generateIFSC() {
        return "BANK0001234";
    }

    private String generateUpiId(String username) {
        return username + "@bankapp";
    }

    private boolean matchesPassword(String rawPassword, String storedPassword) {
        if (storedPassword == null || storedPassword.isBlank()) {
            return rawPassword == null || rawPassword.isBlank();
        }

        if (storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$") || storedPassword.startsWith("$2y$")) {
            return passwordEncoder.matches(rawPassword, storedPassword);
        }

        return storedPassword.equals(rawPassword);
    }

    @Override
    public Account_dto createAccount(Account_dto dto) {
        if (repo.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        Account acc = AccountMapper.mapToAccount(dto);
        acc.setAccountNumber(generateAccountNumber());
        acc.setIfscCode(generateIFSC());
        acc.setUpiId(generateUpiId(dto.getUsername()));
        acc.setPassword(passwordEncoder.encode(dto.getPassword()));
        acc.setMobileVerified(false);

        return AccountMapper.mapToAccountDto(repo.save(acc));
    }

    @Override
    public Account_dto login(String username, String password) {
        Account acc = repo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        if (!matchesPassword(password, acc.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return AccountMapper.mapToAccountDto(acc);
    }

    @Override
    public Account_dto getByUsername(String username) {
        Account acc = repo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        if (acc.getAccountNumber() == null || acc.getAccountNumber().isBlank()) {
            acc.setAccountNumber(generateAccountNumber());
        }

        if (acc.getIfscCode() == null || acc.getIfscCode().isBlank()) {
            acc.setIfscCode(generateIFSC());
        }

        if (acc.getUpiId() == null || acc.getUpiId().isBlank()) {
            acc.setUpiId(generateUpiId(username));
        }

        repo.save(acc);
        return AccountMapper.mapToAccountDto(acc);
    }

    @Override
    public Account_dto addMoney(String username, double amount) {
        Account acc = repo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        if (amount <= 0) {
            throw new RuntimeException("Deposit amount must be greater than zero");
        }

        acc.setBalance(acc.getBalance() + amount);
        repo.save(acc);

        txnRepo.save(new Transaction(username, "credit", amount, acc.getBalance(), "Deposit", LocalDate.now()));

        return AccountMapper.mapToAccountDto(acc);
    }

    @Override
    public Account_dto withdraw(String username, double amount) {
        Account acc = repo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        if (amount <= 0) {
            throw new RuntimeException("Withdrawal amount must be greater than zero");
        }

        if (acc.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        acc.setBalance(acc.getBalance() - amount);
        repo.save(acc);

        txnRepo.save(new Transaction(username, "debit", amount, acc.getBalance(), "Withdraw", LocalDate.now()));

        return AccountMapper.mapToAccountDto(acc);
    }

    @Override
    public Account_dto transfer(String username, String toAccNo, String toIfsc, double amount) {
        Account sender = repo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found: " + username));

        Account receiver = repo.findByAccountNumberAndIfscCode(toAccNo, toIfsc)
                .orElseThrow(() -> new ResourceNotFoundException("Receiver not found for account number: " + toAccNo));

        if (amount <= 0) {
            throw new RuntimeException("Transfer amount must be greater than zero");
        }

        if (sender.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);
        repo.save(sender);
        repo.save(receiver);

        txnRepo.save(new Transaction(username, "debit", amount, sender.getBalance(), "Transfer to " + toAccNo, LocalDate.now()));
        txnRepo.save(new Transaction(receiver.getUsername(), "credit", amount, receiver.getBalance(), "Received from " + sender.getAccountNumber(), LocalDate.now()));

        return AccountMapper.mapToAccountDto(sender);
    }
}
