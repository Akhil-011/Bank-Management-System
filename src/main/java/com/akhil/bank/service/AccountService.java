package com.akhil.bank.service;

import com.akhil.bank.model.Account;
import com.akhil.bank.repository.AccountRepository;
import com.akhil.bank.util.AccountNumberGenerator;
import com.akhil.bank.exception.BankException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Service layer for Account business logic
 */
public class AccountService {
    
    private final AccountRepository accountRepository;
    
    public AccountService() {
        this.accountRepository = new AccountRepository();
    }
    
    /**
     * Create a new account
     */
    public boolean createAccount(int userId, String accountType, BigDecimal initialBalance) throws BankException {
        if (userId <= 0) {
            throw new BankException("Invalid user ID");
        }
        
        if (accountType == null || accountType.trim().isEmpty()) {
            throw new BankException("Account type is required");
        }
        
        if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new BankException("Initial balance cannot be negative");
        }
        
        Account account = new Account();
        account.setUserId(userId);
        account.setAccountNumber(AccountNumberGenerator.generateAccountNumber());
        account.setAccountType(accountType);
        account.setBalance(initialBalance);
        account.setCurrency("USD");
        account.setStatus("Active");
        
        return accountRepository.createAccount(account);
    }
    
    /**
     * Get account by ID
     */
    public Account getAccountById(int accountId) throws BankException {
        Account account = accountRepository.getAccountById(accountId);
        if (account == null) {
            throw new BankException("Account not found");
        }
        return account;
    }
    
    /**
     * Get accounts by user ID
     */
    public List<Account> getAccountsByUserId(int userId) throws BankException {
        if (userId <= 0) {
            throw new BankException("Invalid user ID");
        }
        return accountRepository.getAccountsByUserId(userId);
    }
    
    /**
     * Deposit money to account
     */
    public boolean deposit(int accountId, BigDecimal amount) throws BankException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BankException("Deposit amount must be greater than zero");
        }
        
        Account account = getAccountById(accountId);
        account.setBalance(account.getBalance().add(amount));
        
        return accountRepository.updateAccount(account);
    }
    
    /**
     * Withdraw money from account
     */
    public boolean withdraw(int accountId, BigDecimal amount) throws BankException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BankException("Withdrawal amount must be greater than zero");
        }
        
        Account account = getAccountById(accountId);
        
        if (account.getBalance().compareTo(amount) < 0) {
            throw new BankException("Insufficient funds");
        }
        
        account.setBalance(account.getBalance().subtract(amount));
        
        return accountRepository.updateAccount(account);
    }
    
    /**
     * Transfer money between accounts
     */
    public boolean transfer(int fromAccountId, int toAccountId, BigDecimal amount) throws BankException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BankException("Transfer amount must be greater than zero");
        }
        
        Account fromAccount = getAccountById(fromAccountId);
        Account toAccount = getAccountById(toAccountId);
        
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new BankException("Insufficient funds in source account");
        }
        
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));
        
        // Update both accounts
        accountRepository.updateAccount(fromAccount);
        accountRepository.updateAccount(toAccount);
        
        return true;
    }
    
    /**
     * Close account
     */
    public boolean closeAccount(int accountId) throws BankException {
        Account account = getAccountById(accountId);
        
        if (account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new BankException("Cannot close account with remaining balance");
        }
        
        account.setStatus("Closed");
        return accountRepository.updateAccount(account);
    }
    
    /**
     * Get all accounts
     */
    public List<Account> getAllAccounts() {
        return accountRepository.getAllAccounts();
    }
}
