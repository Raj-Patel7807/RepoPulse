package com.repopulse.user.cli;

import com.repopulse.infra.util.CliUtils;
import com.repopulse.repository.cli.ExploreRepositoryCLI;

public class GuestCLI {
    private final ExploreRepositoryCLI exploreRepositoryCLI = new ExploreRepositoryCLI();

    public void start() {

        while(true) {
            System.out.println("\n=== Guest Menu ===");
            System.out.println("1. Explore Repositories");
            System.out.println("2. Search Users");
            System.out.println("3. Back");

            int choice = CliUtils.getIntInput("Enter Choice: ");

            if(choice == 1) {
                exploreRepositoryCLI.start();
            } else if(choice == 2) {
                String username = CliUtils.getStringInput("Enter Username: ");

                ProfileCLI profileCLI = new ProfileCLI(username, false);
                profileCLI.start();
            } else if(choice == 3) {
                return;
            } else {
                System.out.println("Invalid Choice!!");
            }
        }
    }
}
