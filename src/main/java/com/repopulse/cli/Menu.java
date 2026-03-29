package com.repopulse.cli;

import com.repopulse.util.Session;

public class Menu {

    private final AuthCLI authCLI = new AuthCLI();
    private final RepositoryCLI repoCLI = new RepositoryCLI();
    private final ProfileCLI profileCLI = new ProfileCLI();

    public void start() {
        while(true) {
            if(Session.getCurrentUser() == null) {
                showAuthMenu();
            } else {
                showDashboard();
            }
        }
    }

    private void showAuthMenu() {
        MenuUtils.clearScreen();

        System.out.println("\n=== RepoPulse ===");
        System.out.println("1. Login");
        System.out.println("2. Signup");
        System.out.println("3. Exit");

        int choice = MenuUtils.getIntInput("Enter choice: ");

        if(choice == 1) {
            authCLI.login();
        } else if(choice == 2) {
            authCLI.signup();
        } else if(choice == 3) {
            System.out.println("Goodbye!");
            System.exit(0);
        } else {
            System.out.println("Invalid choice!");
            MenuUtils.waitForEnter();
        }
    }

    private void showDashboard() {
        MenuUtils.clearScreen();

        System.out.println("\n=== Dashboard ===");
        System.out.println("Welcome, " + Session.getCurrentUser().getUsername());
        System.out.println("1. Repositories");
        System.out.println("2. Profile");
        System.out.println("3. Logout");

        int choice = MenuUtils.getIntInput("Enter choice: ");

        if(choice == 1) {
            repoCLI.showRepoMenu();
        } else if(choice == 2) {
            profileCLI.showProfileMenu();
        } else if(choice == 3) {
            Session.logout();
            System.out.println("Logged out successfully!");
            MenuUtils.waitForEnter();
        } else {
            System.out.println("Invalid choice!");
            MenuUtils.waitForEnter();
        }
    }
}
