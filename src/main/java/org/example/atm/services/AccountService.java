package org.example.atm.services;

import org.example.atm.models.Account;
import org.example.atm.models.Transaction;
import org.example.atm.repositories.AccountRepository;
import org.example.atm.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Account getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public void deposit(String accountNumber, double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        account.setBalance(account.getBalance() + amount);
        transactionRepository.save(new Transaction(account.getAccountNumber(), null, "DEPOSIT", BigDecimal.valueOf(amount)));
        accountRepository.save(account);
    }

    public void withdraw(String accountNumber, double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            transactionRepository.save(new Transaction(account.getAccountNumber(), null, "WITHDRAW", BigDecimal.valueOf(amount)));
            accountRepository.save(account);
        }
    }

    public void transfer(String fromAccountNumber, String toAccountNumber, double amount) {
        Account sender = accountRepository.findByAccountNumber(fromAccountNumber);
        Account receiver = accountRepository.findByAccountNumber(toAccountNumber);
        if (sender.getBalance() >= amount) {
            sender.setBalance(sender.getBalance() - amount);
            receiver.setBalance(receiver.getBalance() + amount);
            accountRepository.save(sender);
            accountRepository.save(receiver);
            transactionRepository.save(new Transaction(sender.getAccountNumber(), receiver.getAccountNumber(), "TRANSFER", BigDecimal.valueOf(amount)));
        }
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public void createAccount(Account account) {
        account.setAccountNumber(generateUniqueAccountNumber());
        accountRepository.save(account);
    }

    private String generateUniqueAccountNumber() {
        Random random = new Random();
        String accountNumber;
        do {
            accountNumber = String.format("%010d", random.nextInt(1_000_000_000));
        } while (accountRepository.findByAccountNumber(accountNumber) != null);
        return accountNumber;
    }

    public List<Transaction> getAllTransaction() {
        return transactionRepository.findAll();
    }
}