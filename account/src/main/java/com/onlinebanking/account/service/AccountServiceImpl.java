package com.onlinebanking.account.service;

import com.onlinebanking.account.dto.AccountRequestDTO;
import com.onlinebanking.account.dto.AccountResponseDTO;
import com.onlinebanking.account.exception.*;
import com.onlinebanking.account.model.*;
import com.onlinebanking.account.repository.AccountRepository;
import com.onlinebanking.account.util.ErrorMessageConstants;
import com.onlinebanking.account.util.SuccessMessageConstants;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {


    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    private static final String COMMON_PREFIX = "MOB3605";
    private static final int NUMBER_LENGTH = 4; // Length of the random part

    private static final SecureRandom secureRandom = new SecureRandom();

    private String generateNextNumber() {
        String accNumber;
        Account account;
        do {
            int randomNumber = secureRandom.nextInt((int) Math.pow(10, NUMBER_LENGTH));
            String randomPart = String.format("%0" + NUMBER_LENGTH + "d", randomNumber);
            accNumber = COMMON_PREFIX + randomPart;
            account = accountRepository.findByAccountNumber(accNumber);
        } while (account != null);

        return accNumber;
    }

    //----privates------
    private Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ErrorMessageConstants.ACCOUNT_NOT_FOUND));
    }
    private Account getAccountByAccountNumber(String accountNumber) {
        Account account= accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
           throw  new AccountNotFoundException(ErrorMessageConstants.ACCOUNT_NOT_FOUND);
        }
    return account;}
    private Account getAccountByAccountHolderName(String accountHolderName) {
        Account account= accountRepository.findByAccountHolderName(accountHolderName);
        if (account == null) {
            throw  new AccountNotFoundException(ErrorMessageConstants.ACCOUNT_NOT_FOUND);
        }
        return account;}
    private Account getAccountByUserId(Long userId) {
        Account account= accountRepository.findByUserId(userId);
        if (account == null) {
            throw  new AccountNotFoundException(ErrorMessageConstants.ACCOUNT_NOT_FOUND);
        }
        return account;}
    //----done-------
    public AccountResponseDTO getAccountByCriteria(Long id,String accountNumber,String accountHolderName,Long userId) {
        Account account = null;
        if (id != null){account=getAccountById(id);}
        else if (accountHolderName!=null) {
            account=getAccountByAccountHolderName(accountHolderName);
        } else if (accountNumber != null) {account = getAccountByAccountNumber(accountNumber);}
        else if (userId != null) {account = getAccountByUserId(userId);}

        return modelMapper.map(account, AccountResponseDTO.class);}
    //----done---------

    public AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO) {
        Account account = modelMapper.map(accountRequestDTO, Account.class);

        account.setAccountNumber(generateNextNumber());
        account.setBalance(BigDecimal.ZERO);
        account.setCreatedDate(LocalDateTime.now());
        account.setStatus(AccountStatus.PENDING);
        Account savedAccount=accountRepository.save(account);
        AccountResponseDTO accountResponseDTO=modelMapper.map(savedAccount, AccountResponseDTO.class);
        accountResponseDTO.setMessage(SuccessMessageConstants.ACCOUNT_CREATED_SUCCESS);
        return accountResponseDTO;}
        //-done---------


        //---getall------

        public Page<AccountResponseDTO> getAllAccounts(Pageable pageable) {
            // Fetch paginated and sorted users from the repository
            Page<Account> accountsPage = accountRepository.findAll(pageable);

            // Convert each User entity to a UserResponseDTO using ModelMapper
            return accountsPage.map(account -> modelMapper.map(account, AccountResponseDTO.class));
        }
//----done------
// Partial search with pagination and sorting

public Page<AccountResponseDTO> getAccountsByCriteria(String accountHolderName,String accountNumber,List<AccountStatus> status, Pageable pageable) {
    Page<Account> accountPage;

    if (accountHolderName != null && !accountHolderName.isEmpty()) {
        accountPage = accountRepository.findByAccountHolderNameContainingIgnoreCase(accountHolderName, pageable);
    } else if (accountNumber != null && !accountNumber.isEmpty()) {
        accountPage = accountRepository.findByAccountNumberContainingIgnoreCase(accountNumber, pageable);
    }
    else if (status != null && !status.isEmpty()) {
        accountPage = accountRepository.findByStatusIn(status, pageable);
    } else {
        accountPage = Page.empty(pageable); // Return an empty page if no criteria are provided
    }

    return accountPage.map(account -> modelMapper.map(account, AccountResponseDTO.class));
}
//-done--



    public AccountResponseDTO changeAccountType(String accountNumber, String newType) {
        AccountType accountType;
        try {
            accountType = AccountType.valueOf(newType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidAccountTypeException(ErrorMessageConstants.INVALID_ACCOUNT_TYPE + newType);
        }

        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException(ErrorMessageConstants.ACCOUNT_NOT_FOUND);
        }

        account.setAccountType(accountType);
        account.setLastUpdatedDate(LocalDateTime.now());
        Account updatedAccount=accountRepository.save(account);
        AccountResponseDTO accountResponseDTO=modelMapper.map(updatedAccount, AccountResponseDTO.class);
        accountResponseDTO.setMessage(SuccessMessageConstants.TYPE_UPDATE_MSG(updatedAccount.getAccountType()));
        return accountResponseDTO;}
    //---done----

    public AccountResponseDTO changeAccountStatus(String accountNumber, String status) {
        AccountStatus accountStatus;
        try {
            accountStatus = AccountStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidAccountStatusException(ErrorMessageConstants.INVALID_ACCOUNT_STATUS + status);
        }

        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException(ErrorMessageConstants.ACCOUNT_NOT_FOUND);
        }

        account.setStatus(accountStatus);
        account.setLastUpdatedDate(LocalDateTime.now());
        Account savedAccount=accountRepository.save(account);
        AccountResponseDTO accountResponseDTO=modelMapper.map(savedAccount, AccountResponseDTO.class);
        accountResponseDTO.setMessage(SuccessMessageConstants.STATUS_UPDATE_MSG(savedAccount.getStatus()));
        return accountResponseDTO;}
    //----done----

    public String deleteAccountByAccountNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException(ErrorMessageConstants.ACCOUNT_NOT_FOUND);
        }
        accountRepository.delete(account);
        return SuccessMessageConstants.ACCOUNT_DELETED_SUCCESS;
    }
    //-done----

    public BigDecimal getBalance(AccountRequestDTO accountRequestDTO) {
        Account account = getAccountByAccountNumber(accountRequestDTO.getAccountNumber());
        return account.getBalance();
    }

    public AccountResponseDTO setBalance(String accountNumber, BigDecimal amount) {
        Account account = getAccountByAccountNumber(accountNumber);

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException(ErrorMessageConstants.INVALID_AMOUNT);
        }

        account.setBalance(account.getBalance().add(amount));
        account.setLastUpdatedDate(LocalDateTime.now());
        Account savedAccount=accountRepository.save(account);
        AccountResponseDTO accountResponseDTO=modelMapper.map(savedAccount, AccountResponseDTO.class);
        accountResponseDTO.setMessage(SuccessMessageConstants.BALANCE_UPDATED);
        return accountResponseDTO;}

    public AccountResponseDTO activateBankAccount(String accountNumber) {

        Account account = getAccountByAccountNumber(accountNumber);
        if (account.getStatus() == AccountStatus.ACTIVE) {
            throw new InvalidOperationException(ErrorMessageConstants.ACCOUNT_ALREADY_ACTIVE);
        }
        if (account.getStatus() == AccountStatus.PENDING && meetsActivationCriteria(account)) {
            account.setStatus(AccountStatus.ACTIVE);
            Account savedAccount=accountRepository.save(account);
            AccountResponseDTO accountResponseDTO=modelMapper.map(savedAccount, AccountResponseDTO.class);
            accountResponseDTO.setMessage(SuccessMessageConstants.ACCOUNT_ACTIVATED);
            return accountResponseDTO;}
        else {
            throw new InvalidOperationException(ErrorMessageConstants.CANNOT_ACTIVATE_ACCOUNT);
        }

    }

    private boolean meetsActivationCriteria(Account account) {
        return account.getBalance().compareTo(BigDecimal.valueOf(100)) >= 0;
    }

    //----done--------
    public AccountResponseDTO AddAmount(String accountNumber, BigDecimal amount) {
        Account account = getAccountByAccountNumber(accountNumber);


        // Check if the account status is ACTIVE
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new InvalidAccountStatusException(ErrorMessageConstants.INVALID_ACCOUNT_STATUS);
        }

        // Update the balance
        account.setBalance(amount.add(account.getBalance()));
        Account savedAccount=accountRepository.save(account);
        AccountResponseDTO accountResponseDTO=modelMapper.map(savedAccount, AccountResponseDTO.class);
        accountResponseDTO.setMessage(SuccessMessageConstants.BALANCE_UPDATED);
        return accountResponseDTO;}
}
