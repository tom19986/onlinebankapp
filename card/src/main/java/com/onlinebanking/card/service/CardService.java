package com.onlinebanking.card.service;

import com.onlinebanking.card.dto.CardRequestDTO;
import com.onlinebanking.card.dto.CardResponseDTO;
import com.onlinebanking.card.model.CardStatus;
import com.onlinebanking.card.model.CardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface CardService {
    public CardResponseDTO createCard(CardRequestDTO cardRequestDTO);
    public CardResponseDTO getCardByCriteria(Long id,String cardNumber,String cardHolderName,Long accountId);
    public Page<CardResponseDTO> getCardsByCriteria(String cardHolderName, String cardNumber, List<CardStatus> cardStatus, List<CardType> cardType, Pageable pageable);
    public Page<CardResponseDTO> getAllCards(Pageable pageable);
    public CardResponseDTO updateCardStatus(String cardNumber, CardStatus status);
    public CardResponseDTO addFunds(String cardNumber, BigDecimal amount);
    public CardResponseDTO deactivateCard(String cardNumber);
    public String deleteCard(String cardNumber);
}
