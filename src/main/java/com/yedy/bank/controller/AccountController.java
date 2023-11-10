package com.yedy.bank.controller;

import com.yedy.bank.dto.AccountDto;
import com.yedy.bank.dto.CreateAccountRequest;
import com.yedy.bank.dto.MoneyTransferRequest;
import com.yedy.bank.service.AccountService;
import com.yedy.bank.service.DepositTransaction;
import com.yedy.bank.service.WithDrawTransaction;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("account/v1")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@Valid @RequestBody CreateAccountRequest request){
        return ResponseEntity.ok(accountService.createAccount(request));
    }

    @PostMapping("credit/{accountNumber}")
    public ResponseEntity<UUID> credit(@PathVariable String accountNumber, @RequestBody Double amount){
        return ResponseEntity.ok(accountService.transaction(new DepositTransaction(amount),accountService.findByAccountNumber(accountNumber)));
    }

    @PostMapping("debit/{accountNumber}")
    public ResponseEntity<UUID> debit(@PathVariable String accountNumber, @RequestBody Double amount){
        return ResponseEntity.ok(accountService.transaction(new WithDrawTransaction(amount),accountService.findByAccountNumber(accountNumber)));
    }

    @GetMapping("/{accountNumber}")
    public AccountDto getAccountInformation(@PathVariable String accountNumber){
        return accountService.getAccountInformation(accountNumber);
    }

    @PutMapping("/transfer")
    public ResponseEntity<String> transferMoney(@RequestBody MoneyTransferRequest transferRequest) {
        accountService.transferMoney(transferRequest);
        return ResponseEntity.ok("İşleminiz başarıyla alınmıştır!");
    }
}
