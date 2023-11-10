package com.yedy.bank.repository;

import com.yedy.bank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    public Account findByAccountNumber(String accountNumber);
}
