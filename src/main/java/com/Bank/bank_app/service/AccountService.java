package com.Bank.bank_app.service;

import com.Bank.bank_app.dto.Account_dto;

public interface AccountService {

    Account_dto createAccount(Account_dto dto);

    Account_dto login(String username, String password);  // ✅ FIXED

    Account_dto getByUsername(String username);

    Account_dto addMoney(String username, double amount);

    Account_dto withdraw(String username, double amount);
    Account_dto transfer(String fromUsername, String toAccountNumber, String toIfsc, double amount);
}