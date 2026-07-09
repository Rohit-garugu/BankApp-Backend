package com.Bank.bank_app.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String type;   // credit / debit
    private double amount;
    private double balance;
    private String description;
    private LocalDate date;

    public Transaction() {}

    public Transaction(String username, String type, double amount, double balance, String description, LocalDate date) {
        this.username = username;
        this.type = type;
        this.amount = amount;
        this.balance = balance;
        this.description = description;
        this.date = date;
    }

    // getters & setters

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public double getBalance() { return balance; }
    public String getDescription() { return description; }
    public LocalDate getDate() { return date; }

    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setType(String type) { this.type = type; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setBalance(double balance) { this.balance = balance; }
    public void setDescription(String description) { this.description = description; }
    public void setDate(LocalDate date) { this.date = date; }
}