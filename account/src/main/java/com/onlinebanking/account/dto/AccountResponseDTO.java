package com.onlinebanking.account.dto;



import com.onlinebanking.account.model.AccountStatus;
import com.onlinebanking.account.model.AccountType;
import com.onlinebanking.account.model.Currency;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class AccountResponseDTO {

    private String message;
    private Long id;
    private String accountNumber;
    private AccountType accountType;
    private BigDecimal balance;
    private Currency currency;
    private Long userId; // Reference to user ID
    private LocalDateTime createdDate;
    private LocalDateTime lastUpdatedDate;
    private AccountStatus status;


}

