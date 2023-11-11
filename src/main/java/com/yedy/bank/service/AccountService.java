package com.yedy.bank.service;

import com.yedy.bank.dto.AccountDto;
import com.yedy.bank.dto.CreateAccountRequest;
import com.yedy.bank.dto.MoneyTransferRequest;
import com.yedy.bank.dto.converter.AccountDtoConverter;
import com.yedy.bank.enums.TransactionType;
import com.yedy.bank.model.Account;
import com.yedy.bank.model.Owner;
import com.yedy.bank.model.Transaction;
import com.yedy.bank.repository.AccountRepository;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final OwnerService ownerService;
    private final AccountDtoConverter accountDtoConverter;
    private final DirectExchange exchange;

    private final AmqpTemplate rabbitTemplate;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${sample.rabbitmq.routingKey}")
    String routingKey;

    @Value("${sample.rabbitmq.queue}")
    String queueName;

    public AccountService(AccountRepository accountRepository, OwnerService ownerService, AccountDtoConverter accountDtoConverter, DirectExchange exchange, AmqpTemplate rabbitTemplate, KafkaTemplate<String, String> kafkaTemplate) {
        this.accountRepository = accountRepository;
        this.ownerService = ownerService;
        this.accountDtoConverter = accountDtoConverter;
        this.exchange = exchange;
        this.rabbitTemplate = rabbitTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    public AccountDto createAccount(CreateAccountRequest createAccountRequest) {
        Owner owner = ownerService.findOwnerById(createAccountRequest.getOwnerId());

        Account account = new Account();
        account.setOwner(owner);
        account.setAccountNumber(createAccountRequest.getAccountNumber());
        account.setBalance(createAccountRequest.getInitialCredit());

        if (createAccountRequest.getInitialCredit() > 0) {
            Transaction transaction = new Transaction();
            transaction.setAmount(createAccountRequest.getInitialCredit());
            transaction.setAccount(account);

            account.getTransaction().add(transaction);
        }
        return accountDtoConverter.convert(accountRepository.save(account));
    }

    public UUID transaction(ITransaction transaction, Account account) {
        Account updateAccount = transaction.process(account);
        return accountRepository.save(updateAccount).getId();
    }

    public Account findByAccountNumber(String accountNumber) {
        Assert.notNull(accountNumber, "Hesap numarası boş olamaz");
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public AccountDto getAccountInformation(String accountNumber) {
        return accountDtoConverter.convert(findByAccountNumber(accountNumber));
    }

    public void transferMoney(MoneyTransferRequest transferRequest) {
        rabbitTemplate.convertAndSend(exchange.getName(), routingKey, transferRequest);
    }

    @RabbitListener(queues = "${sample.rabbitmq.queue}")
    public void transferMoneyMessage(MoneyTransferRequest transferRequest) {
        Optional<Account> accountOptional = accountRepository.findById(transferRequest.getFromId());
        accountOptional.ifPresentOrElse(account -> {
            if (account.getBalance() > transferRequest.getAmount()) {
                account.setBalance(account.getBalance() - transferRequest.getAmount());
                Transaction transaction = new Transaction();
                transaction.setTransactionType(TransactionType.TransferOutGoing);
                transaction.setAccount(account);
                transaction.setAmount(transferRequest.getAmount());
                account.getTransaction().add(transaction);
                accountRepository.save(account);
                rabbitTemplate.convertAndSend(exchange.getName(), "secondRoute", transferRequest);
            } else {
                System.out.println("Yetersiz Bakiye -> hesap: " + transferRequest.getFromId() + " bakiye: " + account.getBalance() + " miktar: " + transferRequest.getAmount());
            }
        }, () -> System.out.println("Hesap bulunamadı"));
    }

    @RabbitListener(queues = "secondStepQueue")
    public void updateReceiverAccount(MoneyTransferRequest transferRequest) {
        Optional<Account> accountOptional = accountRepository.findById(transferRequest.getToId());
        accountOptional.ifPresentOrElse(account -> {
            account.setBalance(account.getBalance() + transferRequest.getAmount());
            Transaction transaction = new Transaction();
            transaction.setTransactionType(TransactionType.TransferInComing);
            transaction.setAccount(account);
            transaction.setAmount(transferRequest.getAmount());
            account.getTransaction().add(transaction);
            accountRepository.save(account);
            rabbitTemplate.convertAndSend(exchange.getName(), "thirdRoute", transferRequest);
        }, () -> {
            System.out.println("Alıcı bulunamadı");
            Optional<Account> senderAccount = accountRepository.findById(transferRequest.getFromId());
            senderAccount.ifPresent(sender -> {
                System.out.println("Para geri gönderildi");
                sender.setBalance(sender.getBalance() + transferRequest.getAmount());
                accountRepository.save(sender);
            });

        });
    }

    @RabbitListener(queues = "thirdStepQueue")
    public void finalizeTransfer(MoneyTransferRequest transferRequest) {
        Optional<Account> accountOptional = accountRepository.findById(transferRequest.getFromId());
        accountOptional.ifPresentOrElse(account -> {
            String notificationMessage = "Değerli müşterimiz %s \n" +
                    "Para transferi talebiniz başarılı oldu. Yeni bakiyeniz: %s";
            System.out.println("Gönderen(" + account.getId() + ") Hesap Bakiyeniz: " + account.getBalance());
            String senderMessage = String.format(notificationMessage, account.getId(), account.getBalance());
            kafkaTemplate.send("transfer-notification", senderMessage);
        }, () -> System.out.println("Hesap Bulunamadı"));

        Optional<Account> accountToOptional = accountRepository.findById(transferRequest.getToId());
        accountToOptional.ifPresentOrElse(account -> {
            String notificationMessage = "Değerli müşterimiz %s \n para transferi aldınız. Yeni bakiyeniz:%s";
            System.out.println("Alıcı(" + account.getId() + ") Hesap Bakiyesi: " + account.getBalance());
            String receiverMessage = String.format(notificationMessage, account.getId(),account.getBalance());
            kafkaTemplate.send("transfer-notification", receiverMessage);
        }, () -> System.out.println("Hesap Bulunamadı"));


    }
}
