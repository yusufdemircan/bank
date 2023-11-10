package com.yedy.bank.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private UUID id;
    private String accountNumber;
    private AccountOwnerDto owner;
    private Double balance;
    private Set<TransactionDto> transactions;
}
