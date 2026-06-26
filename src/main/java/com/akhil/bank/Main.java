package com.akhil.bank;

import com.akhil.bank.model.User;
import com.akhil.bank.service.UserService;

public class Main {

    public static void main(String[] args) {

        UserService service = new UserService();

        User user = new User(
                "Akhil",
                "akhil@gmail.com",
                "9876543210",
                "password123"
        );

        service.register(user);

    }
}
