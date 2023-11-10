package com.yedy.bank.dto.converter;

import com.yedy.bank.dto.AccountOwnerDto;
import com.yedy.bank.dto.OwnerDto;
import com.yedy.bank.model.Owner;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OwnerDtoConverter {
    private final OwnerAccountDtoConverter ownerAccountDtoConverter;


    public OwnerDtoConverter(OwnerAccountDtoConverter ownerAccountDtoConverter) {
        this.ownerAccountDtoConverter = ownerAccountDtoConverter;
    }

    public AccountOwnerDto convertToAccountOwner(Optional<Owner> from){
        return from.map(f-> new AccountOwnerDto(f.getId(),f.getName(),f.getSurname())).orElse(null);
    }

    public OwnerDto convertToOwnerDto(Owner from){
        return new OwnerDto(
                from.getId(),
                from.getName(),
                from.getSurname(),
                from.getAccounts()
                        .stream()
                        .map(ownerAccountDtoConverter::convert)
                        .collect(Collectors.toSet())
                );
    }
}
