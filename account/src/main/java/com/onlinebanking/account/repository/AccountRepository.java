package com.onlinebanking.account.repository;

import com.onlinebanking.account.model.Account;
import com.onlinebanking.account.model.AccountStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
 Account findByAccountNumber(String accountNumber);
 Account findByAccountHolderName(String accountHolderName);
 Page<Account> findByAccountHolderNameContainingIgnoreCase(String accountHolderName, Pageable pageable);
 Page<Account> findByAccountNumberContainingIgnoreCase(String accountNumber, Pageable pageable);
 Page<Account> findByStatusIn(List<AccountStatus> status, Pageable pageable);

 Account findByUserId(Long userId);


}
