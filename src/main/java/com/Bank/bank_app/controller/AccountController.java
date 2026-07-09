package com.Bank.bank_app.controller;

import com.Bank.bank_app.dto.Account_dto;
import com.Bank.bank_app.entity.Transaction;
import com.Bank.bank_app.repository.TransactionRepository;
import com.Bank.bank_app.service.AccountService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("api/accounts")
@CrossOrigin(origins = "http://localhost:4200")
public class AccountController {

    private final AccountService service;
    private final TransactionRepository txnRepo;

    public AccountController(AccountService service, TransactionRepository txnRepo) {
        this.service = service;
        this.txnRepo = txnRepo;
    }

    @PostMapping
    public ResponseEntity<Account_dto> create(@RequestBody Account_dto dto) {
        return ResponseEntity.ok(service.createAccount(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<Account_dto> login(@RequestBody Account_dto dto) {
        return ResponseEntity.ok(service.login(dto.getUsername(), dto.getPassword()));
    }

    @GetMapping("/{username}")
    public ResponseEntity<Account_dto> getAccount(@PathVariable String username) {
        return ResponseEntity.ok(service.getByUsername(username));
    }

    // ✅ ADD MONEY
    @PutMapping("/add-money/{username}/{amount}")
    public ResponseEntity<Account_dto> addMoney(
            @PathVariable String username,
            @PathVariable double amount) {

        return ResponseEntity.ok(service.addMoney(username, amount));
    }

    // ✅ WITHDRAW (FIXED)
    @PutMapping("/withdraw/{username}/{amount}")
    public ResponseEntity<?> withdraw(
            @PathVariable String username,
            @PathVariable double amount) {
        try {
            return ResponseEntity.ok(service.withdraw(username, amount));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ FIXED TRANSACTIONS API
    @GetMapping("/transactions/{username}")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable String username) {
        return ResponseEntity.ok(
                txnRepo.findByUsernameOrderByIdDesc(username)
        );
    }


    @PutMapping("/transfer/{username}/{toAcc}/{ifsc}/{amount}")
    public ResponseEntity<?> transfer(
            @PathVariable String username,
            @PathVariable String toAcc,
            @PathVariable String ifsc,
            @PathVariable double amount) {

        try {
            return ResponseEntity.ok(service.transfer(username, toAcc, ifsc, amount));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

