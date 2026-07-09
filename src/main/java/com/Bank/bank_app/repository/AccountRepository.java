package com.Bank.bank_app.repository;

import com.Bank.bank_app.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);

    Optional<Account> findByUsernameAndPassword(String username, String password);
    Optional<Account> findByAccountNumberAndIfscCode(String accountNumber, String ifscCode);
}