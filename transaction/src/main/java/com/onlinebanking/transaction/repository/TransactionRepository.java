package com.onlinebanking.transaction.repository;

import com.onlinebanking.transaction.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByTransactionId(Long transactionId);
    List<Transaction> findByAccountId(Long accountId);
    Page<Transaction> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    List<Transaction> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate);
}
