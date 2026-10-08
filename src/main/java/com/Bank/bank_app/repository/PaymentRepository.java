package com.Bank.bank_app.repository;

import com.Bank.bank_app.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTransactionId(String transactionId);

    List<Payment> findByFromUsernameOrderByCreatedAtDesc(String username);

    List<Payment> findByToUsernameOrderByCreatedAtDesc(String username);

    List<Payment> findByStatusAndFromUsername(String status, String username);
}
