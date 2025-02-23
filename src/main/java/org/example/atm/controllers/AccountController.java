package org.example.atm.controllers;

import org.example.atm.models.Account;
import org.example.atm.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/")
    public String home() {
        return "dashboard";
    }

    @GetMapping("/balance")
    public String viewBalance(Model model, @RequestParam String accountNumber) {
        Account account = accountService.getAccountByAccountNumber(accountNumber);
        model.addAttribute("balance", account.getBalance());
        return "balance";
    }

    @GetMapping("/deposit")
    public String depositForm() {
        return "deposit";
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam String accountNumber, @RequestParam double amount) {
        accountService.deposit(accountNumber, amount);
        return "redirect:/transactions";
    }

    @GetMapping("/withdraw")
    public String withdrawForm() {
        return "withdraw";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam String accountNumber, @RequestParam double amount) {
        accountService.withdraw(accountNumber, amount);
        return "redirect:/transactions";
    }

    @GetMapping("/transfer")
    public String transferForm() {
        return "transfer";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam String fromAccount, @RequestParam String toAccount, @RequestParam double amount) {
        accountService.transfer(fromAccount, toAccount, amount);
        return "redirect:/transactions";
    }

    @GetMapping("/transactions")
    public String viewTransactions(Model model) {
        model.addAttribute("transactions", accountService.getAllTransaction());
        return "transactions";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String fullName) {
        Account account = new Account();
        account.setFullName(fullName);
        accountService.createAccount(account);
        return "redirect:/accounts";
    }

    @GetMapping("/accounts")
    public String listAccounts(Model model) {
        List<Account> accounts = accountService.getAllAccounts();
        model.addAttribute("accounts", accounts);
        return "accounts";
    }
}