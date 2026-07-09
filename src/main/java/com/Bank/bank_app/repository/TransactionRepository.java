package com.Bank.bank_app.repository;

import com.Bank.bank_app.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUsernameOrderByIdDesc(String username);
}