package com.Bank.bank_app.controller;

import com.Bank.bank_app.dto.Account_dto;
import com.Bank.bank_app.service.AccountService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("api/accounts")
@CrossOrigin(origins = "http://localhost:4200")
public class AccountController {

    private AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Account_dto> create(@RequestBody Account_dto dto) {
        return ResponseEntity.ok(service.createAccount(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<Account_dto> login(@RequestBody Account_dto dto) {
        return ResponseEntity.ok((Account_dto) service.login(dto.getUsername(), dto.getPassword()));
    }
}
