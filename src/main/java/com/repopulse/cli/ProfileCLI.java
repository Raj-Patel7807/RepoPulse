package com.repopulse.cli;

import com.repopulse.model.User;
import com.repopulse.service.UserService;
import com.repopulse.util.Session;

public class ProfileCLI {

    private final UserService userService = new UserService();

    public void showProfileMenu() {
        while(true) {
            MenuUtils.clearScreen();

            User user = Session.getCurrentUser();

            System.out.println("\n=== Profile ===");
            System.out.println("Username: " + user.getUsername());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Bio: " + user.getProfileBio());
            System.out.println();
            System.out.println("1. Edit Profile");
            System.out.println("2. Back");

            int choice = MenuUtils.getIntInput("Enter choice: ");

            if(choice == 1) {
                editProfile();
            } else if(choice == 2) {
                return;
            } else {
                System.out.println("Invalid choice!");
                MenuUtils.waitForEnter();
            }
        }
    }

    private void editProfile() {
        String bio = MenuUtils.getStringInput("New Bio: ");
        String avatar = MenuUtils.getStringInput("New Avatar URL: ");
        long userId = Session.getCurrentUser().getUserId();

        userService.updateProfileBioAndAvatar(userId, bio, avatar);

        System.out.println("Profile Updated!");
        MenuUtils.waitForEnter();
    }
}
