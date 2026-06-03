package com.Bank.bank_app.service.Serviceimpl;

import com.Bank.bank_app.dto.Account_dto;
import com.Bank.bank_app.entity.Account;
import com.Bank.bank_app.mapper.AccountMapper;
import com.Bank.bank_app.repository.AccountRepository;
import com.Bank.bank_app.service.AccountService;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository repo;

    public AccountServiceImpl(AccountRepository repo) {
        this.repo = repo;
    }

    // ✅ CREATE ACCOUNT
    @Override
    public Account_dto createAccount(Account_dto dto) {
        Account acc = AccountMapper.mapToAccount(dto);
        return AccountMapper.mapToAccountDto(repo.save(acc));
    }

    // ✅ LOGIN METHOD (YOUR CODE GOES HERE)
    public Account_dto login(String username, String password) {

        Account acc = repo.findByUsernameAndPassword(username, password)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        return AccountMapper.mapToAccountDto(acc);
    }
}
