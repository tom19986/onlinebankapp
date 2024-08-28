package com.onlinebanking.account.controller;

import com.onlinebanking.account.dto.AccountRequestDTO;
import com.onlinebanking.account.dto.AccountResponseDTO;
import com.onlinebanking.account.model.AccountStatus;
import com.onlinebanking.account.service.AccountService;
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
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private final AccountService accountService;

    // Create a new account
    @PostMapping("/create")
    public AccountResponseDTO createAccount(@RequestBody AccountRequestDTO accountRequestDTO) {
        return accountService.createAccount(accountRequestDTO);
    }

    // Get account by various criteria
    @GetMapping("/search")
    public AccountResponseDTO getAccountByCriteria(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String accountNumber,
            @RequestParam(required = false) String AccountHolderName,
            @RequestParam(required = false) Long userId) {
        return accountService.getAccountByCriteria(id, accountNumber,AccountHolderName, userId);
    }

    // Get all accounts with pagination and sorting
    @GetMapping("/all")
    public Page<AccountResponseDTO> getAllAccounts(Pageable pageable) {
        return accountService.getAllAccounts(pageable);
    }

    // Get accounts by criteria with pagination and sorting
    @GetMapping("/searchList")

    public ResponseEntity<Page<AccountResponseDTO>> getUsersByCriteria(
            @RequestParam(required = false) String accountHolderName,
            @RequestParam(required = false) String accountNumber,
            @RequestParam(required = false) List<AccountStatus> status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        // Determine sort direction
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AccountResponseDTO> accountPage = accountService.getAccountsByCriteria(accountHolderName,accountNumber, status, pageable);

        if (accountPage.hasContent()) {
            return new ResponseEntity<>(accountPage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Change account type
    @PutMapping("/{accountNumber}/change-type")
    public AccountResponseDTO changeAccountType(
            @PathVariable String accountNumber,
            @RequestParam String newType) {
        return accountService.changeAccountType(accountNumber, newType);
    }

    // Change account status
    @PutMapping("/{accountNumber}/change-status")
    public AccountResponseDTO changeAccountStatus(
            @PathVariable String accountNumber,
            @RequestParam String status) {
        return accountService.changeAccountStatus(accountNumber, status);
    }

    // Get account balance
    @GetMapping("/balance")
    public BigDecimal getBalance(@RequestBody AccountRequestDTO accountRequestDTO) {
        return accountService.getBalance(accountRequestDTO);
    }

    // Set account balance
    @PutMapping("/{accountNumber}/set-balance")
    public AccountResponseDTO setBalance(
            @PathVariable String accountNumber,
            @RequestParam BigDecimal amount) {
        return accountService.setBalance(accountNumber, amount);
    }

    // Activate bank account
    @PutMapping("/{accountNumber}/activate")
    public AccountResponseDTO activateBankAccount(@PathVariable String accountNumber) {
        return accountService.activateBankAccount(accountNumber);
    }

    // Add amount to account balance
    @PutMapping("/{accountNumber}/add-amount")
    public AccountResponseDTO addAmount(
            @PathVariable String accountNumber,
            @RequestParam BigDecimal amount) {
        return accountService.AddAmount(accountNumber, amount);
    }
}
