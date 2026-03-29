package com.repopulse.cli;

import com.repopulse.model.Repository;
import com.repopulse.service.RepositoryService;
import com.repopulse.service.impl.RepositoryServiceImpl;
import com.repopulse.util.Session;

import java.util.List;
import java.util.Scanner;

public class RepositoryCLI {

    private Scanner sc = new Scanner(System.in);
    // private RepositoryService repoService = new RepositoryServiceImpl();

    public void showRepoMenu() {
        while (true) {
            System.out.println("\n=== Repository Menu ===");

            System.out.println("1. Create Repository");
            System.out.println("2. View My Repositories");
            System.out.println("3. Open Repository");
            System.out.println("4. Back");

            System.out.print("Enter Choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    createRepository();
                    break;
                case 2:
                    listRepositories();
                    break;
                case 3:
                    openRepository();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid Choice...");
            }
        }
    }

    private void createRepository() {
        sc.nextLine();

        System.out.print("Enter Repository Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Description: ");
        String desc = sc.nextLine();

        System.out.print("Public? (true/false): ");
        boolean isPublic = sc.nextBoolean();

        System.out.println("Pending");
    }

    private void listRepositories() {
        System.out.println("Pending");
    }

    private void openRepository() {
        System.out.print("Enter Repository ID: ");
        long repoId = sc.nextLong();
        System.out.println("Pending");
    }
}