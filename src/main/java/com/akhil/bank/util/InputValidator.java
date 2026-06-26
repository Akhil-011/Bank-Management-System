package com.akhil.bank.util;

public class InputValidator {

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("\\d{10}");
    }

    public static boolean isValidAmount(double amount) {
        return amount > 0;
    }

    public static boolean isValidUsername(String username) {
        return username != null && username.matches("^[a-zA-Z0-9_]{3,20}$");
    }

    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUppercase = password.matches(".*[A-Z].*");
        boolean hasLowercase = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*[0-9].*");

        return hasUppercase && hasLowercase && hasDigit;
    }

    public static boolean isValidPhoneNumber(String phone) {
        return isValidPhone(phone);
    }

    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static boolean isPositive(double number) {
        return number > 0;
    }
}
