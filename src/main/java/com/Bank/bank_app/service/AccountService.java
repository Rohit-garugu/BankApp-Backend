package com.Bank.bank_app.service;

import com.Bank.bank_app.dto.Account_dto;

public interface AccountService {

    Account_dto createAccount(Account_dto dto);

    Account_dto login(String username, String password);  // ✅ FIXED
}