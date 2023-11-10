package com.yedy.bank.dto.converter;

import com.yedy.bank.dto.OwnerAccountDto;
import com.yedy.bank.model.Account;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class OwnerAccountDtoConverter {
    private final TransactionDtoConverter transactionDtoConverter;

    public OwnerAccountDtoConverter(TransactionDtoConverter transactionDtoConverter) {
        this.transactionDtoConverter = transactionDtoConverter;
    }

    public OwnerAccountDto convert(Account from){
        return new OwnerAccountDto(
                Objects.requireNonNull(from.getId()),
                from.getAccountNumber(),
                from.getBalance(),
                from.getTransaction()
                        .stream()
                        .map(transactionDtoConverter::convert)
                        .collect(Collectors.toSet()),
                from.getCreatedAt());

    }
}
