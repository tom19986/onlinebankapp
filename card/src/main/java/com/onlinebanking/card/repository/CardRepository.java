package com.onlinebanking.card.repository;

import com.onlinebanking.card.model.Card;
import com.onlinebanking.card.model.CardStatus;
import com.onlinebanking.card.model.CardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Card findByCardNumber(String cardNumber);

    Card findByAccountId(Long accountId);

    Card findByCardHolderName(String cardHolderName);

    Page<Card> findByCardHolderNameContainingIgnoreCase(String cardHolderName, Pageable pageable);

    Page<Card> findByCardNumberContainingIgnoreCase(String cardNumber, Pageable pageable);
    Page<Card> findByStatusIn(List<CardStatus> cardStatus, Pageable pageable);

    Page<Card> findByCardTypeIn(List<CardType> cardType, Pageable pageable);




}
