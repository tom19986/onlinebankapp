package com.onlinebanking.transaction.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionSummaryDTO {
    private BigDecimal totalAmount;
    private int totalTransactions;

    public TransactionSummaryDTO(BigDecimal totalAmount, int totalTransactions) {
        this.totalAmount = totalAmount;
        this.totalTransactions = totalTransactions;
    }
}
