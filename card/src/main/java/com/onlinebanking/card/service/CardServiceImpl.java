package com.onlinebanking.card.service;

import com.onlinebanking.card.dto.CardRequestDTO;
import com.onlinebanking.card.dto.CardResponseDTO;
import com.onlinebanking.card.exceptions.CardNotFoundException;
import com.onlinebanking.card.exceptions.InvalidCardOperationException;
import com.onlinebanking.card.model.Card;
import com.onlinebanking.card.model.CardStatus;
import com.onlinebanking.card.model.CardType;
import com.onlinebanking.card.repository.CardRepository;
import com.onlinebanking.card.util.ErrorMessageUtil;
import com.onlinebanking.card.util.SuccessMessageUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final ModelMapper modelMapper;

    private static final int CARD_NUMBER_LENGTH = 16; // Desired length of the card number
    private static final SecureRandom random = new SecureRandom();

    private static String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder(CARD_NUMBER_LENGTH);

        // Generate digits only
        for (int i = 0; i < CARD_NUMBER_LENGTH; i++) {
            cardNumber.append(random.nextInt(10)); // Append a digit from 0 to 9
        }

        return cardNumber.toString();
    }

    private String generateCvv() {
        // Generate a 3-digit CVV
        return String.valueOf((int) (Math.random() * 900 + 100));
    }

    @Override
    public CardResponseDTO createCard(CardRequestDTO cardRequestDTO) {
        Card card = modelMapper.map(cardRequestDTO, Card.class);
        card.setCardNumber(generateCardNumber());
        card.setExpiryDate(LocalDateTime.now().plusYears(2)); // Set expiry date 2 years from now
        card.setCvv(generateCvv());
        card.setStatus(CardStatus.ACTIVE); // Set status to ACTIVE upon creation
        card.setCreatedDate(LocalDateTime.now());

        Card savedCard = cardRepository.save(card);
        return modelMapper.map(savedCard, CardResponseDTO.class);
    }
    //--private------
    private Card getByCardNumber(String cardNumber){
        Card card= cardRepository.findByCardNumber(cardNumber);
        if (card == null) {
          throw new CardNotFoundException(ErrorMessageUtil.CARD_NOT_FOUND);
        }return card;
    }
    private Card getByCardId(Long id){
        return cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(ErrorMessageUtil.CARD_NOT_FOUND));

    }

    private Card getByAccountId(Long accountId){
        Card card= cardRepository.findByAccountId(accountId);
        if (card == null) {
            throw   new CardNotFoundException(ErrorMessageUtil.CARD_NOT_FOUND);
        }return card;
    }
    private Card getByCardHolderName(String cardHolderName){
        Card card= cardRepository.findByCardHolderName(cardHolderName);
        if (card == null) {
            throw new CardNotFoundException(ErrorMessageUtil.CARD_NOT_FOUND);
        }return card;
    }
    //----------------
    public CardResponseDTO getCardByCriteria(Long id,String cardNumber,String cardHolderName,Long accountId) {
        if (id == null && cardNumber == null && cardHolderName == null && accountId == null) {
            throw new IllegalArgumentException("At least one search criteria must be provided");
        }
        Card card = null;
        if (id != null){card=getByCardId(id);}
         if (cardHolderName!=null) {card=getByCardHolderName(cardHolderName);}
         if (cardNumber != null) {card = getByCardNumber(cardNumber);}
        if (accountId != null) {card = getByAccountId(accountId);}

        return modelMapper.map(card, CardResponseDTO.class);}
    //--done-----
    public Page<CardResponseDTO> getCardsByCriteria(String cardHolderName,String cardNumber, List<CardStatus> cardStatus, List<CardType> cardType, Pageable pageable) {
        Page<Card> cardPage;

        if (cardHolderName != null && !cardHolderName.isEmpty()) {
            cardPage = cardRepository.findByCardHolderNameContainingIgnoreCase(cardHolderName, pageable);
        } else if (cardNumber != null && !cardNumber.isEmpty()) {
            cardPage = cardRepository.findByCardNumberContainingIgnoreCase(cardNumber, pageable);
        }
        else if (cardStatus != null && !cardStatus.isEmpty()) {
            cardPage = cardRepository.findByStatusIn(cardStatus, pageable);
        }
        else if (cardType != null && !cardType.isEmpty()) {
            cardPage = cardRepository.findByCardTypeIn(cardType, pageable);
        }
        else {
            cardPage = Page.empty(pageable); // Return an empty page if no criteria are provided
        }

        return cardPage.map(account -> modelMapper.map(account, CardResponseDTO.class));
    }
    //---done-------



    public Page<CardResponseDTO> getAllCards(Pageable pageable) {
        // Fetch paginated and sorted users from the repository
        Page<Card> cardPage = cardRepository.findAll(pageable);

        // Convert each User entity to a UserResponseDTO using ModelMapper
        return cardPage.map(card -> modelMapper.map(card, CardResponseDTO.class));
    }

    @Override
    public CardResponseDTO updateCardStatus(String cardNumber, CardStatus status) {
        Card card = getByCardNumber(cardNumber);
        card.setStatus(status);
        card.setLastUpdatedDate(LocalDateTime.now());
        Card updatedCard = cardRepository.save(card);
        CardResponseDTO responseDto = modelMapper.map(updatedCard, CardResponseDTO.class);
        responseDto.setMessage(SuccessMessageUtil.CARD_STATUS_UPDATED_SUCCESSFULLY);
        return responseDto;
    }

    @Override
    public CardResponseDTO addFunds(String cardNumber, BigDecimal amount) {
        Card card = getByCardNumber(cardNumber) ;

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidCardOperationException(ErrorMessageUtil.AMOUNT_MUST_BE_GREATER_THAN_ZERO);
        }
        card.setBalance(card.getBalance().add(amount));
        card.setLastUpdatedDate(LocalDateTime.now());
        Card updatedCard = cardRepository.save(card);
        CardResponseDTO responseDto = modelMapper.map(updatedCard, CardResponseDTO.class);
        responseDto.setMessage(SuccessMessageUtil.FUNDS_ADDED_SUCCESSFULLY);
        return responseDto;
    }

    @Override
    public CardResponseDTO deactivateCard(String cardNumber) {
        Card card = getByCardNumber(cardNumber);
        if (card.getStatus() == CardStatus.DEACTIVATED) {
            throw new InvalidCardOperationException(ErrorMessageUtil.CARD_ALREADY_DEACTIVATED);
        }
        card.setStatus(CardStatus.DEACTIVATED);
        card.setLastUpdatedDate(LocalDateTime.now());
        Card updatedCard = cardRepository.save(card);
        CardResponseDTO responseDto = modelMapper.map(updatedCard, CardResponseDTO.class);
        responseDto.setMessage(SuccessMessageUtil.CARD_DEACTIVATED_SUCCESSFULLY);
        return responseDto;
    }

    @Override
    public String deleteCard(String cardNumber) {
        Card card = getByCardNumber(cardNumber);
        cardRepository.delete(card);
        return SuccessMessageUtil.CARD_DELETED_SUCCESSFULLY;
    }
}
