package com.akhil.bank.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Account {

    private int id;
    private String accountNumber;
    private int userId;
    private BigDecimal balance;
    private String status;

    private String accountType;
    private String currency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Account() {
    }

    public Account(String accountNumber, int userId, BigDecimal balance, String status) {
        this.accountNumber = accountNumber;
        this.userId = userId;
        this.balance = balance;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public int getUserId() {
        return userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAccountId() {
        return id;
    }

    public void setAccountId(int accountId) {
        this.id = accountId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
