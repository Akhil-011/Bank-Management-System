package com.akhil.bank.service;

import java.math.BigDecimal;

import com.akhil.bank.model.Account;
import com.akhil.bank.model.User;
import com.akhil.bank.repository.AccountRepository;
import com.akhil.bank.repository.UserRepository;
import com.akhil.bank.util.AccountNumberGenerator;
import com.akhil.bank.util.InputValidator;
import com.akhil.bank.util.PasswordUtil;

public class UserService {

    private final UserRepository userRepository = new UserRepository();
    private final AccountRepository accountRepository = new AccountRepository();

    public void register(User user) {

        if (!InputValidator.isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Invalid email.");
        }

        if (!InputValidator.isValidPhone(user.getPhone())) {
            throw new IllegalArgumentException("Invalid phone number.");
        }

        if (userRepository.emailExists(user.getEmail())) {
            throw new IllegalArgumentException("Email already registered.");
        }

        user.setPassword(
                PasswordUtil.hashPassword(user.getPassword())
        );

        int userId = userRepository.saveUser(user);

        Account account = new Account(
                AccountNumberGenerator.generateAccountNumber(),
                userId,
                BigDecimal.ZERO,
                "ACTIVE"
        );

        accountRepository.createAccount(account);

        System.out.println("\n================================");
        System.out.println(" Registration Successful");
        System.out.println("================================");
        System.out.println("Account Number : " + account.getAccountNumber());
        System.out.println("Opening Balance : ₹0.00");
    }
}
