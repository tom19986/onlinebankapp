package com.onlinebanking.transaction.controller;

import com.onlinebanking.transaction.dto.TransactionDateDTO;
import com.onlinebanking.transaction.dto.TransactionRequestDTO;
import com.onlinebanking.transaction.dto.TransactionResponseDTO;
import com.onlinebanking.transaction.dto.TransactionSummaryDTO;
import com.onlinebanking.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(@Valid @RequestBody TransactionRequestDTO transactionRequestDTO) {
        TransactionResponseDTO response = transactionService.createTransaction(transactionRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(@PathVariable Long transactionId) {
        TransactionResponseDTO response = transactionService.getTransactionById(transactionId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions() {
        List<TransactionResponseDTO> responses = transactionService.getAllTransactions();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
    // New endpoint for paginated and sorted transactions
    @GetMapping("/paged")
    public ResponseEntity<Page<TransactionResponseDTO>> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "transactionDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Page<TransactionResponseDTO> response = transactionService.getAllTransactions(page, size, sortBy, sortDir);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByAccountId(@PathVariable Long accountId) {
        List<TransactionResponseDTO> responses = transactionService.getTransactionsByAccountId(accountId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }


    @DeleteMapping("/{transactionId}")
    public ResponseEntity<String> deleteTransaction(@PathVariable Long transactionId) {
        String message = transactionService.deleteTransaction(transactionId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/date-range")
    public ResponseEntity<Page<TransactionResponseDTO>> getTransactionsByDateRange(
            @Valid @RequestBody TransactionDateDTO transactionDateDTO,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "transactionDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        transactionDateDTO.validateDates();

        // Create a Pageable object with pagination and sorting
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<TransactionResponseDTO> responses = transactionService.getTransactionsByDateRange(transactionDateDTO, pageable);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }


    @GetMapping("/summary")
    public ResponseEntity<TransactionSummaryDTO> getTransactionSummary(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        TransactionSummaryDTO summary = transactionService.getTransactionSummary(startDate, endDate);
        return new ResponseEntity<>(summary, HttpStatus.OK);
    }
}
