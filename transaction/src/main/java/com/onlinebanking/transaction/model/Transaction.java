package com.onlinebanking.transaction.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "banktransactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private Long accountId;
    private BigDecimal amount;
    private String description;
    private LocalDate transactionDate;

}
