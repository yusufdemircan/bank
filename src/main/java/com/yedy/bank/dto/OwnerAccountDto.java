package com.yedy.bank.dto;

import com.yedy.bank.dto.TransactionDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerAccountDto {
    private UUID id;
    private String accountNumber;
    private Double balance;
    private Set<TransactionDto> transactions;
    private Date creationDate;
}
