package com.yedy.bank.dto;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MoneyTransferRequest {

    private UUID fromId;
    private UUID toId;
    private Double amount;
}