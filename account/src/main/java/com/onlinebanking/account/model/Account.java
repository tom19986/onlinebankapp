package com.onlinebanking.account.model;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bankAccount")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String accountNumber;
    private String accountHolderName;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    private BigDecimal balance;
    private LocalDateTime lastUpdateBalanceDate;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    @Column(unique = true, nullable = false)
    private Long userId; // Reference to user ID
    private LocalDateTime createdDate;
    private LocalDateTime lastUpdatedDate;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;


}

