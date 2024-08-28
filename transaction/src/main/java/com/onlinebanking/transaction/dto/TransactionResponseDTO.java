package com.onlinebanking.transaction.dto;

import com.onlinebanking.transaction.model.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionResponseDTO {
    private Long transactionId;
    private Long accountId;
    private BigDecimal amount;
    private String description;
    private LocalDate transactionDate;
    private TransactionType transactionType;
    private String message; // For any success or error messages
}
