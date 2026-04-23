package com.repopulse.auth.cli;

import com.repopulse.auth.service.AuthService;
import com.repopulse.infra.util.CliUtils;
import com.repopulse.infra.session.Session;
import com.repopulse.user.cli.GuestCLI;
import com.repopulse.user.model.User;

public class AuthCLI {

    private final GuestCLI guestCLI = new GuestCLI();
    private final AuthService authService = new AuthService();

    public void start() {
        while(true) {
            System.out.println("\n=== RepoPulse ===");
            System.out.println("1. Login");
            System.out.println("2. Signup");
            System.out.println("3. Continue as Guest");
            System.out.println("4. Admin Login");
            System.out.println("5. Exit");

            int choice = CliUtils.getIntInput("Enter Choice: ");

            if(choice == 1) {
                boolean success = login();
                if(success) {
                    return;
                }
            } else if(choice == 2) {
                signup();
            } else if(choice == 3) {
                guestCLI.start();
            } else if(choice == 4) {
                boolean success = adminLogin();
                if(success) {
                    return;
                }
            } else if(choice == 5) {
                System.out.println("Goodbye!");
                System.exit(0);
            } else {
                System.out.println("Invalid choice!");
                CliUtils.waitForEnter();
            }
        }
    }

    private boolean login() {
        System.out.println("\n\n=== Login ===");
        String username = CliUtils.getStringInput("Username: ");
        String password = CliUtils.getStringInput("Password: ");

        User user = authService.login(username, password);

        if(user == null) {
            System.out.println("Invalid credentials!");
            return false;
        } else {
            Session.setCurrentUser(user);
            Session.setAdminSession(false);
            System.out.println("Login Successful!!");
            return true;
        }
    }

    private boolean adminLogin() {
        System.out.println("\n=== Admin Login ===");
        String password = CliUtils.getStringInput("Admin Password: ");
        User user = authService.loginAdmin(password);
        if(user == null) {
            System.out.println("Invalid admin credentials!");
            CliUtils.waitForEnter();
            return false;
        }
        Session.setCurrentUser(user);
        Session.setAdminSession(true);
        System.out.println("Admin login successful.");
        return true;
    }

    private void signup() {
        System.out.println("=== Signup ===");
        String email = CliUtils.getStringInput("Email: ");
        String username = CliUtils.getStringInput("Username: ");
        String password = CliUtils.getStringInput("Password: ");

        User newUser = new User(username, password, email);

        authService.signup(newUser);

        System.out.println("Signup Successful!!");

        CliUtils.waitForEnter();
    }
}
