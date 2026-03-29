package com.repopulse.cli;

import com.repopulse.model.User;
import com.repopulse.service.AuthService;
import com.repopulse.util.Session;

public class AuthCLI {

    private final AuthService authService = new AuthService();

    public void login() {
        MenuUtils.clearScreen();

        System.out.println("\n\n=== Login ===");
        String username = MenuUtils.getStringInput("Username: ");
        String password = MenuUtils.getStringInput("Password: ");

        User user = authService.login(username, password);

        if(user != null) {
            Session.setCurrentUser(user);
            System.out.println("Login Successful!");
        } else {
            System.out.println("Invalid credentials!");
            MenuUtils.waitForEnter();
        }
    }

    public void signup() {
        MenuUtils.clearScreen();

        System.out.println("=== Signup ===");
        String email = MenuUtils.getStringInput("Email: ");
        String username = MenuUtils.getStringInput("Username: ");
        String password = MenuUtils.getStringInput("Password: ");

        User newUser = new User(username, password, email);
        authService.signup(newUser);
        System.out.println("Signup Successful!");
        MenuUtils.waitForEnter();
    }
}
