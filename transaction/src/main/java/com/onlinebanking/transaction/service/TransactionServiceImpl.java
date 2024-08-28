package com.onlinebanking.transaction.service;

import com.onlinebanking.transaction.dto.TransactionDateDTO;
import com.onlinebanking.transaction.dto.TransactionRequestDTO;
import com.onlinebanking.transaction.dto.TransactionResponseDTO;
import com.onlinebanking.transaction.dto.TransactionSummaryDTO;
import com.onlinebanking.transaction.exception.TransactionNotFoundException;
import com.onlinebanking.transaction.model.Transaction;
import com.onlinebanking.transaction.repository.TransactionRepository;

import com.onlinebanking.transaction.util.ErrorMessageUtil;
import com.onlinebanking.transaction.util.SuccessMessageUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;

    @Override
    public TransactionResponseDTO createTransaction(TransactionRequestDTO transactionRequestDTO) {
        Transaction transaction = modelMapper.map(transactionRequestDTO, Transaction.class);
        transaction.setTransactionDate(LocalDate.now()); // Set transaction date to today
        Transaction savedTransaction = transactionRepository.save(transaction);

        TransactionResponseDTO transactionResponseDTO = modelMapper.map(savedTransaction, TransactionResponseDTO.class);
        transactionResponseDTO.setMessage(SuccessMessageUtil.TRANSACTION_CREATED_SUCCESSFULLY); // Set success message

        return transactionResponseDTO;
    }

    @Override
    public TransactionResponseDTO getTransactionById(Long transactionId) {
        Transaction transaction = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(ErrorMessageUtil.TRANSACTION_NOT_FOUND));
        return modelMapper.map(transaction, TransactionResponseDTO.class);
    }

    @Override
    public List<TransactionResponseDTO> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .map(transaction -> modelMapper.map(transaction, TransactionResponseDTO.class))
                .collect(Collectors.toList());
    }
    // Method to get transactions with pagination and sorting
    @Override
    public Page<TransactionResponseDTO> getAllTransactions(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Transaction> transactionsPage = transactionRepository.findAll(pageable);
        return transactionsPage.map(transaction -> modelMapper.map(transaction, TransactionResponseDTO.class));
    }



    @Override
    public List<TransactionResponseDTO> getTransactionsByAccountId(Long accountId) {
        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);
        return transactions.stream()
                .map(transaction -> modelMapper.map(transaction, TransactionResponseDTO.class))
                .collect(Collectors.toList());
    }



    @Override
    public String deleteTransaction(Long transactionId) {
        Transaction transaction = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(ErrorMessageUtil.TRANSACTION_NOT_FOUND));
        transactionRepository.delete(transaction);
        return SuccessMessageUtil.TRANSACTION_DELETED_SUCCESSFULLY;
    }

    @Override
    public Page<TransactionResponseDTO> getTransactionsByDateRange(TransactionDateDTO transactionDateDTO, Pageable pageable) {
        LocalDate startDate = transactionDateDTO.getStartDate();
        LocalDate endDate = transactionDateDTO.getEndDate();

        Page<Transaction> transactionPage = transactionRepository.findByTransactionDateBetween(startDate, endDate, pageable);

        return transactionPage.map(transaction -> modelMapper.map(transaction, TransactionResponseDTO.class));
    }


    @Override
    public TransactionSummaryDTO getTransactionSummary(LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = transactionRepository.findByTransactionDateBetween(startDate, endDate );
        BigDecimal totalAmount = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new TransactionSummaryDTO(totalAmount, transactions.size());
    }
}
