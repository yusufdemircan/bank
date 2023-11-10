package com.yedy.bank.service;

import com.yedy.bank.model.Account;

public interface ITransaction {
    public Account process(Account account);
}
