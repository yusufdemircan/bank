package com.yedy.bank.service;

import com.yedy.bank.enums.TransactionType;
import com.yedy.bank.model.Account;
import com.yedy.bank.model.Transaction;
import org.springframework.util.Assert;

import java.util.UUID;

public class WithDrawTransaction implements ITransaction {
    private Double amount;

    public WithDrawTransaction(Double amount){
        this.amount=amount;
    }
    @Override
    public Account process(Account account){
        Assert.notNull(account,"Account boş olamaz.!");
        if(amount<=0){
            throw new RuntimeException("Para çekme işlemi miktarı 0 dan büyük olmalıdır!");
        }
        if(account.getBalance()<amount){
            throw new RuntimeException("Para çekme işlemi için bakiyeniz yeterli değildir!");
        }
        account.setBalance(account.getBalance()-amount);
        Transaction transaction= new Transaction();
        transaction.setTransactionType(TransactionType.WithdrawalTransaction);
        transaction.setAmount(amount);
        transaction.setAccount(account);
        account.getTransaction().add(transaction);
        return account;
    }
}
