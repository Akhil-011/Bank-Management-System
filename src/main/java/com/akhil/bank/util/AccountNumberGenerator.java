package com.akhil.bank.util;

import java.util.Random;

public class AccountNumberGenerator {

    private static final Random RANDOM = new Random();

    public static String generateAccountNumber() {

        long number = 1_000_000_000L + RANDOM.nextInt(900_000_000);

        return String.valueOf(number);
    }
}