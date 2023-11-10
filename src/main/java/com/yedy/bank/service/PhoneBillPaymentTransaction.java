package com.yedy.bank.service;

import com.yedy.bank.enums.TransactionType;
import com.yedy.bank.model.Account;
import com.yedy.bank.model.Transaction;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.UUID;

public class PhoneBillPaymentTransaction implements ITransaction {

    private String provider;
    private String phoneNumber;
    private Double amount;

    public PhoneBillPaymentTransaction(String provider, String phoneNumber, Double amount) {
        this.provider = provider;
        this.phoneNumber = phoneNumber;
        this.amount = amount;
    }

    @Override
    public Account process(Account account) {
        Assert.notNull(account,"Account boş olamaz.!");
        Assert.notNull(provider,"Sağlayıcı boş bırakılamaz");
        Assert.notNull(phoneNumber,"Telefon numarası boş bırakılamaz");

        if(amount<=0){
            throw new RuntimeException("Fatura işlemi miktarı 0 dan büyük olmalıdır!");
        }
        if(account.getBalance()<amount){
            throw new RuntimeException("Fatura işlemi için bakiyeniz yeterli değildir!");
        }
        account.setBalance(account.getBalance()-amount);
        Transaction transaction= new Transaction();
        transaction.setTransactionType(TransactionType.BillPaymentTransaction);
        transaction.setAmount(amount);
        transaction.setPhoneNumber(phoneNumber);
        transaction.setProvider(provider);
        transaction.setAccount(account);
        account.getTransaction().add(transaction);
        return account;
    }
}
