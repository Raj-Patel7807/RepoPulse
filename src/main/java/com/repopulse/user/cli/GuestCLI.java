package com.repopulse.user.cli;

import com.repopulse.common.cli.CliUtils;

public class GuestCLI {

    public void start() {

        while(true) {
            System.out.println("\n=== Guest Menu ===");
            System.out.println("1. Explore Repositories");
            System.out.println("2. Search Users");
            System.out.println("3. Back");

            int choice = CliUtils.getIntInput("Enter Choice: ");

            if(choice == 1) {
                System.out.println("Explore Repo Pending.......");
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
