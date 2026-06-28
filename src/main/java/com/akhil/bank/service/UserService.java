package com.akhil.bank.service;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.SQLException;
import com.akhil.bank.config.DatabaseConnection;
import com.akhil.bank.exception.UserAlreadyExistsException;


import com.akhil.bank.model.Account;
import com.akhil.bank.model.User;
import com.akhil.bank.repository.AccountRepository;
import com.akhil.bank.repository.UserRepository;
import com.akhil.bank.util.AccountNumberGenerator;
import com.akhil.bank.util.InputValidator;
import com.akhil.bank.util.PasswordUtil;

public class UserService {

    private final UserRepository userRepository = new UserRepository();
    private final AccountRepository accountRepository= new AccountRepository();

    public void register(User user) {

        if (!InputValidator.isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Invalid email.");
        }

        if (!InputValidator.isValidPhone(user.getPhone())) {
            throw new IllegalArgumentException("Invalid phone number.");
        }

        if (userRepository.emailExists(user.getEmail())) {
    throw new UserAlreadyExistsException(
            "Email already registered. Please login instead."
    );
}

        // Hash password
        user.setPassword(
                PasswordUtil.hashPassword(user.getPassword())
        );

        
        Connection connection = DatabaseConnection.getConnection();

try {

    connection.setAutoCommit(false);

    // Save user
    int userId = userRepository.saveUser(connection, user);

    // Create account
    Account account = new Account();

    account.setUserId(userId);

    String accountNumber;

    do {
        accountNumber = AccountNumberGenerator.generateAccountNumber();
    } while (accountRepository.accountNumberExists(accountNumber));

    account.setAccountNumber(accountNumber);
    account.setAccountType("SAVINGS");
    account.setBalance(BigDecimal.ZERO);
    account.setCurrency("INR");
    account.setStatus("ACTIVE");

    boolean created = accountRepository.createAccount(connection, account);

    if (!created) {
        throw new RuntimeException("Failed to create account.");
    }

    connection.commit();

    System.out.println();
    System.out.println("======================================");
    System.out.println(" Registration Successful");
    System.out.println("======================================");
    System.out.println("User ID          : " + userId);
    System.out.println("Account Number   : " + account.getAccountNumber());
    System.out.println("Account Type     : " + account.getAccountType());
    System.out.println("Currency         : " + account.getCurrency());
    System.out.println("Opening Balance  : INR " + account.getBalance());
    System.out.println("Status           : " + account.getStatus());
    System.out.println("======================================");

} catch (Exception e) {

    try {
        connection.rollback();
    } catch (SQLException ex) {
        ex.printStackTrace();
    }

    throw new RuntimeException("Registration failed.", e);

} finally {

    try {
        connection.setAutoCommit(true);
        connection.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    }

    public User login(String email, String rawPassword) {

    User user = userRepository.findByEmail(email);

    if (user == null) {
        throw new IllegalArgumentException("User not found.");
    }

    // verify password
    if (!PasswordUtil.verifyPassword(rawPassword, user.getPassword())) {
        throw new IllegalArgumentException("Invalid password.");
    }

    System.out.println("======================================");
    System.out.println(" Login Successful");
    System.out.println("======================================");
    System.out.println("User ID   : " + user.getId());
    System.out.println("Email     : " + user.getEmail());
    System.out.println("======================================");

    return user;
}
}