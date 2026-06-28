package com.akhil.bank.service;

import java.math.BigDecimal;
import java.util.List;

import com.akhil.bank.model.Transaction;
import com.akhil.bank.repository.TransactionRepository;

public class TransactionService {

    private final TransactionRepository transactionRepository = new TransactionRepository();

    public void recordTransaction(int accountId,
                              String transactionType,
                              BigDecimal amount,
                              BigDecimal balanceAfter,
                              String description) {

    //System.out.println(">>> recordTransaction() called");

    Transaction transaction = new Transaction();

    transaction.setAccountId(accountId);
    transaction.setTransactionType(transactionType);
    transaction.setAmount(amount);
    transaction.setBalanceAfter(balanceAfter);
    transaction.setDescription(description);

    boolean saved = transactionRepository.saveTransaction(transaction);

    if (!saved) {
    throw new RuntimeException("Failed to save transaction.");
}
}


public List<Transaction> getTransactionHistory(int accountId) {
    return transactionRepository.getTransactionsByAccountId(accountId);
}

public List<Transaction> getMiniStatement(int accountId) {
    return transactionRepository.getMiniStatement(accountId);
}

}