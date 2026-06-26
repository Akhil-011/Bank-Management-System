package com.akhil.bank.util;

public class AccountNumberGenerator {

    private static long counter = 1000000000L;

    public static synchronized String generateAccountNumber() {
        return String.valueOf(counter++);
    }
}
