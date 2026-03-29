package com.repopulse.cli;

import com.repopulse.cli.AuthCLI;
import com.repopulse.util.Session;
import com.repopulse.cli.RepositoryCLI;

import java.util.Scanner;

public class Menu {
    private Scanner sc = new Scanner(System.in);
    private AuthCLI authCLI = new AuthCLI();
    private RepositoryCLI repositoryCLI = new RepositoryCLI();

    public void showAuthMenu() {
        System.out.println("\n=== RepoPulse ===");
        System.out.println("1. Login");
        System.out.println("2. SignUp");
        System.out.println("3. Exit");

        System.out.print("Enter Choice: ");
        int choice = sc.nextInt();

        if(choice == 1) {
            authCLI.login();
        } else if(choice == 2) {
            authCLI.signup();
        } else if(choice == 3) {
            System.out.println("Exiting....");
            System.exit(0);
        } else {
            System.out.println("Invalid Choice...");
        }
    }

    public void showUserMenu() {
        System.out.println("\n=== Dashboard ===");
        System.out.println("Welcome, " + Session.getCurrentUser().getUsername());

        System.out.println("1. Repository Menu");
        System.out.println("2. View Profile");
        System.out.println("3. Logout");

        int choice = sc.nextInt();

        if(choice == 1) {
            repositoryCLI.showRepoMenu();
        } else if(choice == 2) {
            System.out.println("View Profile - Not Implemented...");
        } else if(choice == 3) {
            Session.logout();
        } else {
            System.out.println("Invalid Choice...");
        }
    }

    private void logout() {
        Session.logout();
        System.out.println("Logged out successfully!");
    }

    public void start() {
        while(true) {
            if(Session.getCurrentUser() == null) {
                showAuthMenu();
            } else {
                showUserMenu();
            }
        }
    }
}
