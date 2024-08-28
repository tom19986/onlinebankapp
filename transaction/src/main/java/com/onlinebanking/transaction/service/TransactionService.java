package com.onlinebanking.transaction.service;

import com.onlinebanking.transaction.dto.TransactionDateDTO;
import com.onlinebanking.transaction.dto.TransactionRequestDTO;
import com.onlinebanking.transaction.dto.TransactionResponseDTO;
import com.onlinebanking.transaction.dto.TransactionSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionService {
    TransactionResponseDTO createTransaction(TransactionRequestDTO transactionRequestDTO);
    TransactionResponseDTO getTransactionById(Long transactionId);
   List<TransactionResponseDTO> getAllTransactions();
   public Page<TransactionResponseDTO> getAllTransactions(int page, int size, String sortBy, String sortDir);
    List<TransactionResponseDTO> getTransactionsByAccountId(Long accountId);
    String deleteTransaction(Long transactionId);
    public Page<TransactionResponseDTO> getTransactionsByDateRange(TransactionDateDTO transactionDateDTO, Pageable pageable);
    TransactionSummaryDTO getTransactionSummary(LocalDate startDate, LocalDate endDate);
}
