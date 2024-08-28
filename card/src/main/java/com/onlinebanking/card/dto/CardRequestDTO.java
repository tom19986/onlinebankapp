package com.onlinebanking.card.dto;

import com.onlinebanking.card.model.CardType;
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
public class CardRequestDTO {

    @NotBlank(message = "Card holder name is required.")
    @Size(max = 100, message = "Card holder name should not exceed 100 characters.")
    private String cardHolderName;

    @NotBlank(message = "PIN is required.")
    @Pattern(regexp = "\\d{4}", message = "PIN must be exactly 4 digits.")
    private String pin; // Assuming the PIN is provided during card creation

    @NotNull(message = "Card type is required.")
    private CardType cardType;

    @NotNull(message = "Initial balance is required.")
    @DecimalMin(value = "0.0", inclusive = true, message = "Initial balance must be at least 0.")
    private BigDecimal initialBalance; // Balance at the time of card creation

    @NotNull(message = "Account ID is required.")
    private Long accountId;
}
