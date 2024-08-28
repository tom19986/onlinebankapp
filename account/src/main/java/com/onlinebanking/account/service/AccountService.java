package com.onlinebanking.account.service;

import com.onlinebanking.account.dto.AccountRequestDTO;
import com.onlinebanking.account.dto.AccountResponseDTO;
import com.onlinebanking.account.model.AccountStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    public AccountResponseDTO getAccountByCriteria(Long id,String accountNumber,String accountHolderName,Long userId);
    public AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO);
    public Page<AccountResponseDTO> getAllAccounts(Pageable pageable);
    public Page<AccountResponseDTO> getAccountsByCriteria(String accountHolderName,String accountNumber,List<AccountStatus> status, Pageable pageable);
    public AccountResponseDTO changeAccountType(String accountNumber, String newType);
    public AccountResponseDTO changeAccountStatus(String accountNumber, String status);
    public BigDecimal getBalance(AccountRequestDTO accountRequestDTO);
    public AccountResponseDTO setBalance(String accountNumber, BigDecimal amount);
    public AccountResponseDTO activateBankAccount(String accountNumber);
    public AccountResponseDTO AddAmount(String accountNumber, BigDecimal amount);
}
