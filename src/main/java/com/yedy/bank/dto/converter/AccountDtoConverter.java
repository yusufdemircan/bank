package com.yedy.bank.dto.converter;

import com.yedy.bank.dto.AccountDto;
import com.yedy.bank.model.Account;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AccountDtoConverter {
    private final OwnerDtoConverter ownerDtoConverter;
    private final TransactionDtoConverter transactionDtoConverter;

    public AccountDtoConverter(OwnerDtoConverter ownerDtoConverter, TransactionDtoConverter transactionDtoConverter) {
        this.ownerDtoConverter = ownerDtoConverter;
        this.transactionDtoConverter = transactionDtoConverter;
    }

    public AccountDto convert(Account from) {
        return new AccountDto(from.getId(),
                from.getAccountNumber(),
                ownerDtoConverter.convertToAccountOwner(Optional.ofNullable(from.getOwner())),
                from.getBalance(),
                Objects.requireNonNull(from.getTransaction())
                        .stream()
                        .map(transactionDtoConverter::convert)
                        .collect(Collectors.toSet())
                );
    }
}
