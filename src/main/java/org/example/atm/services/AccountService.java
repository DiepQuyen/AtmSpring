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
    @Autowired //tiêm AccountRepository vào để sử dụng
    private AccountRepository accountRepository;

    @Autowired //tiêm TransactionRepository vào để sử dụng
    private TransactionRepository transactionRepository;

    //phương thức lấy thông tin tài khoản theo số tài khoản
    public Account getAccountByAccountNumber(String accountNumber) {
        //gọi phương thức findByAccountNumber từ accountRepository để lấy thông tin tài khoản
        return accountRepository.findByAccountNumber(accountNumber);
    }

    //phương thức nạp tiền vào tài khoản
    public void deposit(String accountNumber, double amount) {
        //lấy thông tin tài khoản theo số tài khoản
        Account account = accountRepository.findByAccountNumber(accountNumber);
        //cộng số tiền nạp vào số dư của tài khoản
        account.setBalance(account.getBalance() + amount);
        //lưu thông tin giao dịch vào transactionRepository
        transactionRepository.save(new Transaction(account.getAccountNumber(), null, "DEPOSIT", BigDecimal.valueOf(amount)));
        //lưu thông tin tài khoản vào database
        accountRepository.save(account);
    }

    //phương thức rút tiền từ tài khoản
    public void withdraw(String accountNumber, double amount) {
        //lấy thông tin tài khoản theo số tài khoản
        Account account = accountRepository.findByAccountNumber(accountNumber);
        //kiểm tra số dư tài khoản có lớn hơn số tiền rút không
        if (account.getBalance() >= amount) {
            //nếu có thì trừ số tiền rút khỏi số dư tài khoản
            account.setBalance(account.getBalance() - amount);
            //lưu thông tin giao dịch vào database
            transactionRepository.save(new Transaction(account.getAccountNumber(), null, "WITHDRAW", BigDecimal.valueOf(amount)));
            //lưu thông tin tài khoản vào database
            accountRepository.save(account);
        }
    }

    //phương thức chuyển tiền từ tài khoản người gửi sang tài khoản người nhận
    public void transfer(String fromAccountNumber, String toAccountNumber, double amount) {
        //lấy thông tin tài khoản người gửi
        Account sender = accountRepository.findByAccountNumber(fromAccountNumber);
        //lấy thông tin tài khoản người nhận
        Account receiver = accountRepository.findByAccountNumber(toAccountNumber);
        //kiểm tra số dư tài khoản người gửi có lớn hơn số tiền chuyển không
        if (sender.getBalance() >= amount) {
            //nếu có thì trừ số tiền chuyển khỏi số dư tài khoản người gửi
            sender.setBalance(sender.getBalance() - amount);
            //cộng số tiền chuyển vào số dư tài khoản người nhận
            receiver.setBalance(receiver.getBalance() + amount);
            //lưu thông tin tài khoản người gửi vào database
            accountRepository.save(sender);
            //lưu thông tin tài khoản người nhận vào database
            accountRepository.save(receiver);
            //lưu thông tin giao dịch vào database
            transactionRepository.save(new Transaction(sender.getAccountNumber(), receiver.getAccountNumber(), "TRANSFER", BigDecimal.valueOf(amount)));
        }
    }

    //phương thức lấy danh sách tất cả tài khoản
    public List<Account> getAllAccounts() {
        //lấy danh sách tất cả tài khoản từ database
        return accountRepository.findAll();
    }

    //phương thức tạo tài khoản
    public void createAccount(Account account) {
        //tạo số tài khoản mới cho tài khoản
        account.setAccountNumber(generateUniqueAccountNumber());
        //lưu thông tin tài khoản vào database
        accountRepository.save(account);
    }

    //phương thức tạo số tài khoản mới
    private String generateUniqueAccountNumber() {
        //tạo số tài khoản ngẫu nhiên
        Random random = new Random();
        String accountNumber;
        do {
            //định dạng số tài khoản có 10 chữ số
            accountNumber = String.format("%010d", random.nextInt(1_000_000_000));
            //kiểm tra xem số tài khoản đã tồn tại chưa
        } while (accountRepository.findByAccountNumber(accountNumber) != null);
        //trả về số tài khoản mới
        return accountNumber;
    }

    //phương thức lấy danh sách tất cả giao dịch
    public List<Transaction> getAllTransaction() {
        //lấy danh sách tất cả giao dịch từ database
        return transactionRepository.findAll();
    }
}