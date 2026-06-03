package com.Bank.bank_app.mapper;

import com.Bank.bank_app.dto.Account_dto;
import com.Bank.bank_app.entity.Account;

public class AccountMapper {

    public static Account mapToAccount(Account_dto dto) {
        return new Account(
                dto.getId(),
                dto.getAccount_name(),
                dto.getAccount_type(),
                dto.getBalance(),
                dto.getAccount_holder_name(),
                dto.getUsername(),
                dto.getPassword()
        );
    }

    public static Account_dto mapToAccountDto(Account acc) {
        return new Account_dto(
                acc.getId(),
                acc.getAccount_name(),
                acc.getAccount_type(),
                acc.getBalance(),
                acc.getAccount_holder_name(),
                acc.getUsername(),
                acc.getPassword()
        );
    }
}