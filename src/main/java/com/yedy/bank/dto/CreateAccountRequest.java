package com.yedy.bank.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountRequest {
    private UUID ownerId;
    @Min(value = 0,message = "initialCredit must not be negative value ")
    private Double initialCredit;
    private String accountNumber;
}
