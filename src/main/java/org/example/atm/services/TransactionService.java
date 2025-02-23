package org.example.atm.services;

import org.example.atm.models.Account;
import org.example.atm.models.Transaction;
import org.example.atm.repositories.AccountRepository;
import org.example.atm.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    public List<Transaction> getTransactionHistory() {
        return transactionRepository.findAll();
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

    public void withdraw(String accountNumber, double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            accountRepository.save(account);
            transactionRepository.save(new Transaction(account.getAccountNumber(), null, "WITHDRAW", BigDecimal.valueOf(amount)));
        }
    }

    public void deposit(String accountNumber, double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
        transactionRepository.save(new Transaction(account.getAccountNumber(), null, "DEPOSIT", BigDecimal.valueOf(amount)));
    }
}