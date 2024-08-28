package com.onlinebanking.card.dto;

import com.onlinebanking.card.model.CardType;
import com.onlinebanking.card.model.CardStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CardResponseDTO {

    private String cardNumber;
    private String cardHolderName;
    private LocalDateTime expiryDate;
    private CardType cardType;
    private CardStatus status;
    private BigDecimal balance;
    private Long accountId;
    private String message;
}
