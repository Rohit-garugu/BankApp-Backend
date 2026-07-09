package com.Bank.bank_app.service.Serviceimpl;

import com.Bank.bank_app.dto.Account_dto;
import com.Bank.bank_app.entity.Account;
import com.Bank.bank_app.entity.Transaction;
import com.Bank.bank_app.mapper.AccountMapper;
import com.Bank.bank_app.repository.AccountRepository;
import com.Bank.bank_app.repository.TransactionRepository;
import com.Bank.bank_app.service.AccountService;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repo;
    private final TransactionRepository txnRepo;

    private String generateAccountNumber() {
        return "AC" + System.currentTimeMillis();
    }

    private String generateIFSC() {
        return "BANK0001234";
    }



    public AccountServiceImpl(AccountRepository repo, TransactionRepository txnRepo) {
        this.repo = repo;
        this.txnRepo = txnRepo;
    }

    // ✅ CREATE ACCOUNT

    @Override
    public Account_dto createAccount(Account_dto dto) {

        Account acc = AccountMapper.mapToAccount(dto);
        acc.setAccountNumber(generateAccountNumber());
        acc.setIfscCode(generateIFSC());

        return AccountMapper.mapToAccountDto(repo.save(acc));
    }


    // ✅ LOGIN METHOD
    @Override
    public Account_dto login(String username, String password) {

        Account acc = repo.findByUsernameAndPassword(username, password)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        return AccountMapper.mapToAccountDto(acc);
    }

    // ✅ GET ACCOUNT BY USERNAME (FIXED PROPERLY)
    @Override
    public Account_dto getByUsername(String username) {

        Account acc = repo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ FIX OLD USERS
        if (acc.getAccountNumber() == null) {
            acc.setAccountNumber("AC" + System.currentTimeMillis());
        }

        if (acc.getIfscCode() == null) {
            acc.setIfscCode("BANK0001234");
        }

        repo.save(acc);

        return AccountMapper.mapToAccountDto(acc);
    }

    @Override
    public Account_dto addMoney(String username, double amount) {

        Account acc = repo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        acc.setBalance(acc.getBalance() + amount);
        repo.save(acc);

        Transaction txn = new Transaction(
                username, "credit", amount, acc.getBalance(),
                "Deposit", java.time.LocalDate.now()
        );

        txnRepo.save(txn);

        return AccountMapper.mapToAccountDto(acc);
    }


    @Override
    public Account_dto withdraw(String username, double amount) {

        Account acc = repo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (acc.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        acc.setBalance(acc.getBalance() - amount);
        repo.save(acc);

        Transaction txn = new Transaction(
                username, "debit", amount, acc.getBalance(),
                "Withdraw", java.time.LocalDate.now()
        );

        txnRepo.save(txn);

        return AccountMapper.mapToAccountDto(acc);
    }

    @Override
    public Account_dto transfer(String username, String toAccNo, String toIfsc, double amount) {

        Account sender = repo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        Account receiver = repo.findByAccountNumberAndIfscCode(toAccNo, toIfsc)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        if (sender.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        // ✅ debit
        sender.setBalance(sender.getBalance() - amount);
        repo.save(sender);

        // ✅ credit
        receiver.setBalance(receiver.getBalance() + amount);
        repo.save(receiver);

        // ✅ transactions
        txnRepo.save(new Transaction(
                username, "debit", amount, sender.getBalance(),
                "Transfer to " + toAccNo, LocalDate.now()
        ));

        txnRepo.save(new Transaction(
                receiver.getUsername(), "credit", amount, receiver.getBalance(),
                "Received from " + sender.getAccountNumber(), LocalDate.now()
        ));

        return AccountMapper.mapToAccountDto(sender);
    }

}
