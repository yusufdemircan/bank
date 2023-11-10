package com.yedy.bank.dto;

import com.yedy.bank.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private UUID approvalCode;
    private TransactionType transactionType;
    private Double amount;
    private Date transactionDate;
}
