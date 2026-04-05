package com.repopulse.user.cli;

import com.repopulse.common.cli.CliUtils;
import com.repopulse.infra.session.Session;
import com.repopulse.user.model.User;
import com.repopulse.user.service.UserService;

public class DashboardCLI {
    private final UserService userService = new UserService();

    public void start() {
        while(true) {
            System.out.println("\n=== Dashboard ===");
            System.out.println("Welcome, " + Session.getCurrentUser().getUsername());
            System.out.println("1. Search User");
            System.out.println("2. View Profile");
            System.out.println("3. Logout");

            int choice = CliUtils.getIntInput("Enter Choice: ");

            if(choice == 1) {
                searchUser();
            } else if(choice == 2) {
                ProfileCLI profileCLI = new ProfileCLI(Session.getCurrentUser().getUsername(), false);
                profileCLI.start();
            } else if(choice == 3) {
                Session.setCurrentUser(null);
                return;
            } else {
                System.out.println("Invalid choice!");
                CliUtils.waitForEnter();
            }
        }
    }

    private void searchUser() {
        String username = CliUtils.getStringInput("Enter Username to Search: ");

        User user = userService.getUserByUsername(username);

        if(user != null) {
            ProfileCLI profileCLI = new ProfileCLI(username, true);
            profileCLI.start();
        } else {
            System.out.println("User not found!!");
            CliUtils.waitForEnter();
        }
    }
}
