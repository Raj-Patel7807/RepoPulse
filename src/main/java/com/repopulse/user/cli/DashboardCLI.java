package com.repopulse.user.cli;

import com.repopulse.infra.util.CliUtils;
import com.repopulse.infra.session.Session;
import com.repopulse.notification.cli.NotificationCLI;
import com.repopulse.repository.cli.RepositoryCLI;
import com.repopulse.user.model.User;
import com.repopulse.user.service.UserService;

public class DashboardCLI {
    private final UserService userService = new UserService();

    private final RepositoryCLI repositoryCLI = new RepositoryCLI();
    private final NotificationCLI notificationCLI = new NotificationCLI();

    public void start() {
        while(true) {
            System.out.println("\n=== Dashboard ===");
            System.out.println("Welcome, " + Session.getCurrentUser().getUsername());
            System.out.println("1. My Profile");
            System.out.println("2. My Repositories");
            System.out.println("3. Search User");
            System.out.println("4. Notifications");
            System.out.println("5. Logout");

            int choice = CliUtils.getIntInput("Enter Choice: ");

            if(choice == 1) {
                ProfileCLI profileCLI = new ProfileCLI(Session.getCurrentUser().getUsername(), false);
                profileCLI.start();
            } else if(choice == 2) {
                repositoryCLI.start();
            } else if(choice == 3) {
                searchUser();
            } else if(choice == 4) {
                notificationCLI.start();
            } else if(choice == 5) {
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
