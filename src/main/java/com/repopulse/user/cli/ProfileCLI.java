package com.repopulse.user.cli;

import com.repopulse.common.cli.CliUtils;
import com.repopulse.infra.session.Session;
import com.repopulse.user.model.User;
import com.repopulse.user.service.UserService;

public class ProfileCLI {
    private final UserService userService = new UserService();

    private final User user;
    private final boolean isSearching;

    public ProfileCLI(String username, boolean isSearching) {
        this.user = userService.getUserByUsername(username);
        this.isSearching = isSearching;
    }

    public void start() {
        while(true) {
            System.out.println("\n=== Profile ===");
            System.out.println("Username: " + user.getUsername());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Bio: " + user.getProfileBio());
            System.out.println("Profile Photo: " + user.getProfileAvatarUrl());
            System.out.println("Followers: " + userService.getFollowersCount(user.getUserId()));
            System.out.println("Following: " + userService.getFollowingCount(user.getUserId()));
            System.out.println();

            if(isSearching) {
                System.out.println("1. View Repositories");
                System.out.println("2. Follow");
                System.out.println("3. Block");
                System.out.println("4. Report");
                System.out.println("5. Back");

                int choice = CliUtils.getIntInput("Enter Choice: ");

                if(choice == 1) {
                    System.out.println("View Repos Pending..........");
                } else if(choice == 2) {
                    userService.followUser(Session.getCurrentUser().getUserId(), user.getUserId());
                    System.out.println("Followed!!");
                } else if(choice == 3) {
                    userService.blockUser(Session.getCurrentUser().getUserId(), user.getUserId());
                    System.out.println("User: " + user.getUsername() + " Blocked Successfully!!");
                } else if(choice == 4) {
                    System.out.println("Report User Pending......");
                } else if(choice == 5) {
                    return;
                } else {
                    System.out.println("Invalid Choice!!");
                    CliUtils.waitForEnter();
                }
            } else {
                System.out.println("1. View Repositories");
                System.out.println("2. Edit Profile");
                System.out.println("3. Back");

                int choice = CliUtils.getIntInput("Enter Choice: ");

                if(choice == 1) {
                    System.out.println("View Repos Pending..........");
                } else if(choice == 2) {
                    System.out.println("Edit Profile Pending..........");
                } else if(choice == 3) {
                    return;
                } else {
                    System.out.println("Invalid Choice!!");
                    CliUtils.waitForEnter();
                }
            }
        }
    }
}
