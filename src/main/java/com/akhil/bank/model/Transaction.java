package com.akhil.bank.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Transaction {

    private int id;
    private String senderAccount;
    private String receiverAccount;
    private BigDecimal amount;
    private String transactionType;
    private Timestamp createdAt;

    private int accountId;
    private String description;
    private String status;
    private LocalDateTime transactionDate;

    public Transaction() {
    }

    public Transaction(String senderAccount, String receiverAccount,
                       BigDecimal amount, String transactionType) {
        this.senderAccount = senderAccount;
        this.receiverAccount = receiverAccount;
        this.amount = amount;
        this.transactionType = transactionType;
    }

    public int getId() {
        return id;
    }

    public String getSenderAccount() {
        return senderAccount;
    }

    public String getReceiverAccount() {
        return receiverAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSenderAccount(String senderAccount) {
        this.senderAccount = senderAccount;
    }

    public void setReceiverAccount(String receiverAccount) {
        this.receiverAccount = receiverAccount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public int getTransactionId() {
        return id;
    }

    public void setTransactionId(int transactionId) {
        this.id = transactionId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
}
