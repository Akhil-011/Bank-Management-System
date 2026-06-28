package com.akhil.bank;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

import com.akhil.bank.model.Account;
import com.akhil.bank.model.Transaction;
import com.akhil.bank.model.User;
import com.akhil.bank.service.AccountService;
import com.akhil.bank.service.TransactionService;
import com.akhil.bank.service.UserService;

public class ConsoleMenu {

    private final Scanner scanner = new Scanner(System.in);

    private final UserService userService = new UserService();
    private final AccountService accountService = new AccountService();
    private final TransactionService transactionService = new TransactionService();

    public void start() {

        while (true) {

            System.out.println();
            System.out.println("========================================");
            System.out.println("      BANK MANAGEMENT SYSTEM");
            System.out.println("========================================");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            int choice;

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid choice.");
                continue;
            }

            switch (choice) {

                case 1:
                    register();
                    break;

                case 2:
                    login();
                    break;

                case 3:
                    System.out.println("Thank you for using Bank Management System.");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void register() {

    try {

        System.out.println();
        System.out.println("========== USER REGISTRATION ==========");

        System.out.print("Full Name : ");
        String name = scanner.nextLine();

        System.out.print("Email     : ");
        String email = scanner.nextLine();

        System.out.print("Phone     : ");
        String phone = scanner.nextLine();

        System.out.print("Password  : ");
        String password = scanner.nextLine();

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(password);

        userService.register(user);

    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
    }
}

    private void login() {

    try {

        System.out.println();
        System.out.println("============== LOGIN ==============");

        System.out.print("Email    : ");
        String email = scanner.nextLine();

        System.out.print("Password : ");
        String password = scanner.nextLine();

        User user = userService.login(email, password);

        accountMenu(user);

    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
    }
}
private void accountMenu(User user) {

    while (true) {

        System.out.println();
        System.out.println("========================================");
        System.out.println(" Welcome, " + user.getName());
        System.out.println("========================================");
        System.out.println("1. Deposit");
        System.out.println("2. Withdraw");
        System.out.println("3. Transfer");
        System.out.println("4. View Balance");
        System.out.println("5. Transaction History");
        System.out.println("6. Mini Statement");
        System.out.println("7. Logout");
        System.out.print("Enter choice: ");

        int choice;

        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid choice.");
            continue;
        }

        switch (choice) {

            case 1:
                deposit(user);
                break;

            case 2:
                withdraw(user);
                break;

            case 3:
                transfer(user);
                break;

            case 4:
                accountService.checkBalance(user.getId());
                break;

            case 5:
                showTransactionHistory(user);
                break;

            case 6:
                showMiniStatement(user);
                break;

            case 7:
                System.out.println("Logged out successfully.");
                return;

            default:
                System.out.println("Invalid choice.");
        }
    }
}

private void deposit (User user) {

    try {

        System.out.print("Enter amount: ");
        BigDecimal amount = new BigDecimal(scanner.nextLine());

        accountService.deposit(user.getId(), amount);

    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
    }
}

private void withdraw(User user) {

    try {

        System.out.print("Enter amount: ");
        BigDecimal amount = new BigDecimal(scanner.nextLine());

        accountService.withdraw(user.getId(), amount);

    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
    }
}

private void transfer(User user) {

    try {

        System.out.print("Receiver Account Number: ");
        String accountNumber = scanner.nextLine().trim();

        System.out.print("Amount: ");
        BigDecimal amount = new BigDecimal(scanner.nextLine());

        accountService.transfer(
                user.getId(),
                accountNumber,
                amount
        );

    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
    }
}

private void showTransactionHistory(User user) {

    try {

        Account account = accountService.getAccountByUserId(user.getId());

        List<Transaction> transactions =
                transactionService.getTransactionHistory(account.getAccountId());

        System.out.println();
        System.out.println("==========================================");
        System.out.println("        TRANSACTION HISTORY");
        System.out.println("==========================================");

        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        for (Transaction t : transactions) {

            System.out.println("Transaction ID : " + t.getTransactionId());
            System.out.println("Type           : " + t.getTransactionType());
            System.out.println("Amount         : INR " + t.getAmount());
            System.out.println("Balance After  : INR " + t.getBalanceAfter());
            System.out.println("Description    : " + t.getDescription());
            System.out.println("Date           : " + t.getCreatedAt());
            System.out.println("------------------------------------------");
        }

    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
    }
}



private void showMiniStatement(User user) {

    try {

        Account account = accountService.getAccountByUserId(user.getId());

        List<Transaction> transactions =
                transactionService.getMiniStatement(account.getAccountId());

        System.out.println();
        System.out.println("==========================================");
        System.out.println("         MINI STATEMENT");
        System.out.println("==========================================");

        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        for (Transaction t : transactions) {

            System.out.printf(
                    "%-15s INR %-10s %s%n",
                    t.getTransactionType(),
                    t.getAmount(),
                    t.getCreatedAt()
            );
        }

    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
    }
}

}