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
        // chuyển hướng người dùng đến trang dashboard
        return "dashboard";
    }

    @GetMapping("/balance")
    public String viewBalance(Model model, @RequestParam String accountNumber) {
        // lấy thông tin số dư của tài khoản
        Account account = accountService.getAccountByAccountNumber(accountNumber);
        // truyền thông tin số dư vào model để hiển thị lên view
        model.addAttribute("balance", account.getBalance());
        return "balance";
    }

    @GetMapping("/deposit")
    public String depositForm() {
        // trả về view deposit.html
        return "deposit";
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam String accountNumber, @RequestParam double amount) {
        // gọi phương thức deposit từ accountService để thực hiện nạp tiền
        accountService.deposit(accountNumber, amount);
        // sau khi nạp tiền thành công, chuyển hướng người dùng đến trang transactions
        return "redirect:/transactions";
    }

    @GetMapping("/withdraw")
    public String withdrawForm() {
        // trả về view withdraw.html
        return "withdraw";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam String accountNumber, @RequestParam double amount) {
        // gọi phương thức withdraw từ accountService để thực hiện rút tiền
        accountService.withdraw(accountNumber, amount);
        // sau khi rút tiền thành công, chuyển hướng người dùng đến trang transactions
        return "redirect:/transactions";
    }

    @GetMapping("/transfer")
    public String transferForm() {
        // trả về view transfer.html
        return "transfer";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam String fromAccount, @RequestParam String toAccount, @RequestParam double amount) {
        // gọi phương thức transfer từ accountService để thực hiện chuyển tiền
        accountService.transfer(fromAccount, toAccount, amount);
        // sau khi chuyển tiền thành công, chuyển hướng người dùng đến trang transactions
        return "redirect:/transactions";
    }

    @GetMapping("/transactions")
    public String viewTransactions(Model model) {
        // lấy danh sách giao dịch từ accountService
        model.addAttribute("transactions", accountService.getAllTransaction());
        // trả về view transactions.html
        return "transactions";
    }

    @GetMapping("/register")
    public String registerForm() {
        // trả về view register.html
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String fullName) {
        Account account = new Account();
        account.setFullName(fullName);
        // gọi phương thức createAccount từ accountService để tạo tài khoản mới
        accountService.createAccount(account);
        // sau khi tạo tài khoản thành công, chuyển hướng người dùng đến trang accounts
        return "redirect:/accounts";
    }

    @GetMapping("/accounts")
    public String listAccounts(Model model) {
        // lấy danh sách tài khoản từ accountService
        List<Account> accounts = accountService.getAllAccounts();
        // truyền danh sách tài khoản vào model để hiển thị lên view
        model.addAttribute("accounts", accounts);
        // trả về view accounts.html
        return "accounts";
    }
}