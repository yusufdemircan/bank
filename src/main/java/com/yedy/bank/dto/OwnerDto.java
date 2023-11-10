package com.yedy.bank.dto;

import com.yedy.bank.model.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerDto {
    private UUID id;
    private String name;
    private String surname;
    private Set<OwnerAccountDto> accounts;

}
