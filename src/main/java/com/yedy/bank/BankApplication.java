package com.yedy.bank;

import com.yedy.bank.model.Account;
import com.yedy.bank.model.Owner;
import com.yedy.bank.repository.AccountRepository;
import com.yedy.bank.repository.OwnerRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.math.BigDecimal;

@SpringBootApplication
public class BankApplication implements CommandLineRunner {

	private final AccountRepository accountRepository;

	public BankApplication(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Owner owner = new Owner();
		owner.setName("yusuf");
		owner.setSurname("demircan");
		Owner owner2 = new Owner();
		owner2.setName("emine");
		owner2.setSurname("demircan");

		Account account = new Account();
		account.setOwner(owner);
		account.setAccountNumber("144-1414");
		account.setBalance(0.0);

		Account account2 = new Account();
		account2.setOwner(owner2);
		account2.setAccountNumber("144-1415");
		account2.setBalance(0.0);
		accountRepository.save(account);
		accountRepository.save(account2);

		System.out.println(account);
		System.out.println(account2);
	}
}
