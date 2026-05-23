package com.repopulse.user.cli;

import com.repopulse.infra.exception.AppException;
import com.repopulse.infra.session.Authz;
import com.repopulse.infra.session.Session;
import com.repopulse.infra.util.CliUtils;
import com.repopulse.repository.model.Repository;
import com.repopulse.repository.service.RepositoryService;
import com.repopulse.user.model.User;
import com.repopulse.user.service.UserService;

import java.util.List;

public class ProfileCLI {
    private final UserService userService = new UserService();
    private final RepositoryService repositoryService = new RepositoryService();

    private final User user;

    public ProfileCLI(String username, boolean isSearching) {
        this.user = userService.getUserByUsername(username);
    }

    public void start() {
        if(user == null) {
            System.out.println("User not found.");
            CliUtils.waitForEnter();
            return;
        }
        if(Session.getCurrentUser() != null && Session.getCurrentUser().getUserId() != user.getUserId() && !Session.isAdminSession() && userService.hasBlockRelationship(Session.getCurrentUser().getUserId(), user.getUserId())) {
            throw new AppException("This profile is not accessible due to block settings.");
        }

        while(true) {
            System.out.println("\n=== Profile ===");
            System.out.println("Username: " + user.getUsername());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Bio: " + user.getProfileBio());
            System.out.println("Profile Photo: " + user.getProfileAvatarUrl());
            System.out.println("Followers: " + userService.getFollowersCount(user.getUserId()));
            System.out.println("Following: " + userService.getFollowingCount(user.getUserId()));
            System.out.println();

            boolean loggedIn = Authz.isLoggedIn();
            boolean ownProfile = loggedIn && Session.getCurrentUser().getUserId() == user.getUserId();

            if(ownProfile) {
                System.out.println("1. View Repositories");
                System.out.println("2. Edit Profile");
                System.out.println("3. Back");

                int choice = CliUtils.getIntInput("Enter Choice: ");

                if(choice == 1) {
                    listUserRepositories();
                } else if(choice == 2) {
                    editProfileMenu();
                } else if(choice == 3) {
                    return;
                } else {
                    System.out.println("Invalid Choice!!");
                    CliUtils.waitForEnter();
                }
            } else if(loggedIn) {
                System.out.println("1. View Repositories");
                System.out.println("2. Follow");
                System.out.println("3. Block");
                System.out.println("4. Report");
                System.out.println("5. Back");

                int choice = CliUtils.getIntInput("Enter Choice: ");

                if(choice == 1) {
                    listUserRepositories();
                } else if(choice == 2) {
                    userService.followUser(Session.getCurrentUser().getUserId(), user.getUserId());
                    System.out.println("Followed!!");
                    CliUtils.waitForEnter();
                } else if(choice == 3) {
                    userService.blockUser(Session.getCurrentUser().getUserId(), user.getUserId());
                    System.out.println("User: " + user.getUsername() + " Blocked Successfully!!");
                    CliUtils.waitForEnter();
                } else if(choice == 4) {
                    String reason = CliUtils.getStringInput("Reason (SPAM/ABUSE/FAKE_ACCOUNT): ");
                    String desc = CliUtils.getStringInput("Description: ");
                    userService.reportUser(user.getUserId(), Session.getCurrentUser().getUserId(), reason, desc);
                    System.out.println("Report submitted.");
                    CliUtils.waitForEnter();
                } else if(choice == 5) {
                    return;
                } else {
                    System.out.println("Invalid Choice!!");
                    CliUtils.waitForEnter();
                }
            } else {
                System.out.println("1. View Repositories");
                System.out.println("2. Back");

                int choice = CliUtils.getIntInput("Enter Choice: ");

                if(choice == 1) {
                    listUserRepositories();
                } else if(choice == 2) {
                    return;
                } else {
                    System.out.println("Invalid Choice!!");
                    CliUtils.waitForEnter();
                }
            }
        }
    }

    private void listUserRepositories() {
        List<Repository> repos = repositoryService.getRepositoriesByUser(user.getUserId());
        if(repos.isEmpty()) {
            System.out.println("No repositories found.");
        } else {
            for(Repository r : repos) {
                System.out.println(r.getRepoId() + " | " + r.getRepoName() + " | " + r.getRepoVisibilityType());
            }
        }
        CliUtils.waitForEnter();
    }

    private void editProfileMenu() {
        while(true) {
            System.out.println("\n=== Edit Profile ===");
            System.out.println("1. Update Bio + Avatar URL");
            System.out.println("2. Change Email");
            System.out.println("3. Change Password");
            System.out.println("4. Back");

            int choice = CliUtils.getIntInput("Enter Choice: ");
            long userId = Session.getCurrentUser().getUserId();

            if(choice == 1) {
                String bio = CliUtils.getStringInput("Bio: ");
                String avatar = CliUtils.getStringInput("Avatar URL: ");
                userService.updateProfileBioAndAvatar(userId, bio, avatar);
                System.out.println("Profile updated.");
                CliUtils.waitForEnter();
            } else if(choice == 2) {
                String email = CliUtils.getStringInput("New Email: ");
                userService.changeEmail(userId, email);
                System.out.println("Email updated.");
                CliUtils.waitForEnter();
            } else if(choice == 3) {
                String pw = CliUtils.getStringInput("New Password: ");
                userService.changePassword(userId, pw);
                System.out.println("Password updated.");
                CliUtils.waitForEnter();
            } else if(choice == 4) {
                return;
            } else {
                System.out.println("Invalid choice!");
                CliUtils.waitForEnter();
            }
        }
    }
}
