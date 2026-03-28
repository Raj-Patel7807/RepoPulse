package com.repopulse.cli;

import com.repopulse.model.User;
import com.repopulse.service.AuthService;
import com.repopulse.service.impl.AuthServiceImpl;
import com.repopulse.util.Session;

import java.util.Scanner;

public class AuthCLI {
    private Scanner sc = new Scanner(System.in);
    private AuthService authService = new AuthServiceImpl();

    public void login() {
        System.out.print("Username: ");
        String username = sc.next();

        System.out.print("Password: ");
        String password = sc.next();

        User user = authService.login(username, password);

        if(user != null) {
            Session.setCurrentUser(user);
            System.out.println("Login Successful...");
        } else {
            System.out.println("Invalid Credentials...");
        }
    }

    public void signup() {
        System.out.print("Enter Your Email: ");
        String email = sc.next();

        System.out.print("Choose username: ");
        String username = sc.next();

        System.out.print("Choose password: ");
        String password = sc.next();

        User newUser = new User(username, password, email);
        authService.signup(newUser);
        System.out.println("Signup Successful...");
    }
}
