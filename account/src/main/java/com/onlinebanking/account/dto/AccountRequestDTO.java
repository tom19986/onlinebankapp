package com.onlinebanking.account.dto;

import com.onlinebanking.account.model.AccountType;
import com.onlinebanking.account.model.Currency;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequestDTO {
    @NotBlank(message = "Account number is mandatory")
    private String accountNumber;
    @NotBlank(message = "Account type is mandatory")
    private String accountType;

    @NotBlank(message = "Balance is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "Balance must be positive")
    private BigDecimal balance;

    @NotBlank(message = "Balance is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "Balance must be positive")
    private BigDecimal addAmount;

    @NotBlank(message = "Currency is mandatory")
    private String currency;

    @NotBlank(message = "user ID is mandatory")
    private Long userId;
    @NotBlank(message = "account type is mandatory")
    private String status;






}
