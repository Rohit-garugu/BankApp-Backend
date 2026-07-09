package com.Bank.bank_app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String account_name;
    private String account_type;
    private double balance;
    private String account_holder_name;
    private String username;
    private String password;

    // ✅ NEW FIELDS
    @Column(unique = true)
    private String accountNumber;

    private String ifscCode;

    // ✅ DEFAULT CONSTRUCTOR
    public Account() {}

    // ✅ FULL CONSTRUCTOR
    public Account(Long id, String account_name, String account_type,
                   double balance, String account_holder_name,
                   String username, String password,
                   String accountNumber, String ifscCode) {

        this.id = id;
        this.account_name = account_name;
        this.account_type = account_type;
        this.balance = balance;
        this.account_holder_name = account_holder_name;
        this.username = username;
        this.password = password;
        this.accountNumber = accountNumber;
        this.ifscCode = ifscCode;
    }

    // ✅ GETTERS

    public Long getId() { return id; }

    public String getAccount_name() { return account_name; }

    public String getAccount_type() { return account_type; }

    public double getBalance() { return balance; }

    public String getAccount_holder_name() { return account_holder_name; }

    public String getUsername() { return username; }

    public String getPassword() { return password; }

    public String getAccountNumber() { return accountNumber; }

    public String getIfscCode() { return ifscCode; }


    // ✅ SETTERS

    public void setId(Long id) { this.id = id; }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setAccount_holder_name(String account_holder_name) {
        this.account_holder_name = account_holder_name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }
}
