package com.onlinebanking.card.controller;

import com.onlinebanking.card.dto.CardRequestDTO;
import com.onlinebanking.card.dto.CardResponseDTO;
import com.onlinebanking.card.model.CardStatus;
import com.onlinebanking.card.model.CardType;
import com.onlinebanking.card.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;



    @PostMapping
    public ResponseEntity<CardResponseDTO> createCard(@Valid @RequestBody CardRequestDTO cardRequestDTO) {
        CardResponseDTO response = cardService.createCard(cardRequestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<CardResponseDTO> getCardByCriteria(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String cardNumber,
            @RequestParam(required = false) String cardHolderName,
            @RequestParam(required = false) Long accountId) {
        CardResponseDTO response = cardService.getCardByCriteria(id, cardNumber, cardHolderName, accountId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/searchList")
    public ResponseEntity<Page<CardResponseDTO>> getCardsByCriteria(
            @RequestParam(required = false) String cardHolderName,
            @RequestParam(required = false) String cardNumber,
            @RequestParam(required = false) List<CardStatus> cardStatus,
            @RequestParam(required = false) List<CardType> cardType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "cardNumber") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        // Determine sort direction
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CardResponseDTO> cardPage = cardService.getCardsByCriteria(cardHolderName,cardNumber, cardStatus, cardType,pageable);

        if (cardPage.hasContent()) {
            return new ResponseEntity<>(cardPage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Page<CardResponseDTO>> getAllCards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CardResponseDTO> cardPage = cardService.getAllCards(pageable);

        return ResponseEntity.ok(cardPage);
    }


    @PutMapping("/{cardNumber}/status")
    public ResponseEntity<CardResponseDTO> updateCardStatus(@PathVariable String cardNumber, @RequestParam CardStatus status) {
        CardResponseDTO response = cardService.updateCardStatus(cardNumber, status);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{cardNumber}/add-funds")
    public ResponseEntity<CardResponseDTO> addFunds(@PathVariable String cardNumber, @RequestParam BigDecimal amount) {
        CardResponseDTO response = cardService.addFunds(cardNumber, amount);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{cardNumber}/deactivate")
    public ResponseEntity<CardResponseDTO> deactivateCard(@PathVariable String cardNumber) {
        CardResponseDTO response = cardService.deactivateCard(cardNumber);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{cardNumber}")
    public ResponseEntity<String> deleteCard(@PathVariable String cardNumber) {
        String response = cardService.deleteCard(cardNumber);
        return ResponseEntity.ok(response);
    }
}
