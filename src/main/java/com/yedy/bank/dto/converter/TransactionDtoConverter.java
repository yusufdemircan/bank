package com.yedy.bank.dto.converter;

import com.yedy.bank.dto.TransactionDto;
import com.yedy.bank.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionDtoConverter {
    public TransactionDto convert(Transaction from) {
        return new TransactionDto(from.getId(),
                from.getTransactionType(),
                from.getAmount(),
                from.getCreatedAt());
    }
}
