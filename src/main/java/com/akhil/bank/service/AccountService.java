package com.akhil.bank.service;

import java.math.BigDecimal;

import com.akhil.bank.model.Account;
import com.akhil.bank.repository.AccountRepository;
import com.akhil.bank.service.TransactionService;
import com.akhil.bank.exception.AccountNotFoundException;
import com.akhil.bank.exception.InsufficientBalanceException;
import com.akhil.bank.exception.InvalidAmountException;

import java.sql.Connection;
import java.sql.SQLException;

public class AccountService {

    private final AccountRepository accountRepository = new AccountRepository();

    private final TransactionService transactionService = new TransactionService();

    /**
     * Deposit money into a user's account.
     */
    public void deposit(int userId, BigDecimal amount) {

    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
        throw new IllegalArgumentException("Deposit amount must be greater than zero.");
    }

    Account account = accountRepository.getAccountByUserId(userId);

    if (account == null) {
        throw new AccountNotFoundException("Account not found.");
    }

    BigDecimal newBalance = account.getBalance().add(amount);

    boolean updated = accountRepository.updateBalance(
            account.getAccountId(),
            newBalance
    );

    if (!updated) {
        throw new RuntimeException("Failed to deposit money.");
    }

    // NEW CODE - Record transaction
    transactionService.recordTransaction(
            account.getAccountId(),
            "DEPOSIT",
            amount,
            newBalance,
            "Cash Deposit"
    );

    System.out.println();
    System.out.println("======================================");
    System.out.println(" Deposit Successful");
    System.out.println("======================================");
    System.out.println("Account Number : " + account.getAccountNumber());
    System.out.println("Deposited      : INR " + amount);
    System.out.println("New Balance    : INR " + newBalance);
    System.out.println("======================================");
}


    /**
 * Display account balance.
 */
public void checkBalance(int userId) {

    Account account = accountRepository.getAccountByUserId(userId);

    if (account == null) {
        throw new IllegalArgumentException("Account not found.");
    }

    System.out.println();
    System.out.println("======================================");
    System.out.println(" Account Details");
    System.out.println("======================================");
    System.out.println("Account Number : " + account.getAccountNumber());
    System.out.println("Account Type   : " + account.getAccountType());
    System.out.println("Balance        : INR " + account.getBalance());
    System.out.println("Status         : " + account.getStatus());
    System.out.println("======================================");
}

/**
 * Withdraw money from account.
 */
public void withdraw(int userId, BigDecimal amount) {

    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
        throw new IllegalArgumentException("Withdrawal amount must be greater than zero.");
    }

    Account account = accountRepository.getAccountByUserId(userId);

    if (account == null) {
        throw new IllegalArgumentException("Account not found.");
    }

    if (account.getBalance().compareTo(amount) < 0) {
        throw new IllegalArgumentException("Insufficient balance.");
    }

    BigDecimal newBalance = account.getBalance().subtract(amount);

    boolean updated = accountRepository.updateBalance(
            account.getAccountId(),
            newBalance
    );

    if (!updated) {
        throw new RuntimeException("Failed to withdraw money.");
    }

    System.out.println();
    System.out.println("======================================");
    System.out.println(" Withdrawal Successful");
    System.out.println("======================================");
    System.out.println("Account Number : " + account.getAccountNumber());
    System.out.println("Withdrawn      : INR " + amount);
    System.out.println("New Balance    : INR " + newBalance);
    System.out.println("======================================");
}

/**
 * Transfer money between two accounts.
 */
public void transfer(int senderUserId,
                     String receiverAccountNumber,
                     BigDecimal amount) {

    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
        throw new IllegalArgumentException("Transfer amount must be greater than zero.");
    }

    Connection conn = accountRepository.getConnection();

    try {

        conn.setAutoCommit(false);

        Account sender = accountRepository.getAccountByUserId(senderUserId);

        if (sender == null) {
            throw new IllegalArgumentException("Sender account not found.");
        }

        Account receiver = accountRepository.getAccountByAccountNumber(receiverAccountNumber);

        if (receiver == null) {
            throw new IllegalArgumentException("Receiver account not found.");
        }

        if (sender.getAccountNumber().equals(receiver.getAccountNumber())) {
            throw new IllegalArgumentException("Cannot transfer to the same account.");
        }

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance.");
        }

        BigDecimal senderBalance = sender.getBalance().subtract(amount);
        BigDecimal receiverBalance = receiver.getBalance().add(amount);

        accountRepository.updateBalance(
                conn,
                sender.getAccountId(),
                senderBalance);

        accountRepository.updateBalance(
                conn,
                receiver.getAccountId(),
                receiverBalance);

        conn.commit();


    transactionService.recordTransaction(
        sender.getAccountId(),
        "TRANSFER_OUT",
        amount,
        senderBalance,
        "Transfer to " + receiver.getAccountNumber()
);

transactionService.recordTransaction(
        receiver.getAccountId(),
        "TRANSFER_IN",
        amount,
        receiverBalance,
        "Transfer from " + sender.getAccountNumber()
);

        System.out.println();
        System.out.println("======================================");
        System.out.println(" Transfer Successful");
        System.out.println("======================================");
        System.out.println("From Account : " + sender.getAccountNumber());
        System.out.println("To Account   : " + receiver.getAccountNumber());
        System.out.println("Amount       : INR " + amount);
        System.out.println("======================================");

    } catch (Exception e) {

        try {
            conn.rollback();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        throw new RuntimeException(e);

    } finally {

        try {
            conn.setAutoCommit(true);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

public Account getAccountByUserId(int userId) {
    return accountRepository.getAccountByUserId(userId);
}

}