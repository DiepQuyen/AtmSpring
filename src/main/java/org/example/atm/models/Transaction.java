package org.example.atm.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "type")
    private String type;
    @Column(name = "from_account_number")
    private String fromAccountNumber;
    @Column(name = "to_account_number")
    private String toAccountNumber;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "transaction_date")
    private LocalDateTime transaction_date = LocalDateTime.now();

    public Transaction(String fromAccountNumber, String toAccountNumber, String type, BigDecimal amount) {
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.type = type;
        this.amount = amount;
    }
}