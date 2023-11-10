package com.yedy.bank.service;

import com.yedy.bank.enums.TransactionType;
import com.yedy.bank.model.Account;
import com.yedy.bank.model.Transaction;
import org.springframework.util.Assert;

public class DepositTransaction implements ITransaction{
    private Double amount;

    public DepositTransaction(Double amount){
        this.amount=amount;
    }
    @Override
    public Account process(Account account) {
        Assert.notNull(account,"Account boş olamaz!");
        if(amount<=0){
            throw new RuntimeException("Para yatırma işlemi miktarı 0 dan büyük olmalıdır!");
        }
        account.setBalance(account.getBalance()+amount);
        Transaction transaction= new Transaction();
        transaction.setTransactionType(TransactionType.DepositTransaction);
        transaction.setAmount(amount);
        transaction.setAccount(account);
        account.getTransaction().add(transaction);
        return account;
    }
}
