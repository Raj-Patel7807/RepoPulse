package com.repopulse.cli;

import com.repopulse.model.Repository;
import com.repopulse.service.RepositoryService;
import com.repopulse.util.Session;
import java.util.List;

public class RepositoryCLI {

    private final RepositoryService repoService = new RepositoryService();

    public void showRepoMenu() {
        while(true) {
            MenuUtils.clearScreen();

            System.out.println("\n=== Repository Menu ===");
            System.out.println("1. Create Repository");
            System.out.println("2. List My Repositories");
            System.out.println("3. Open Repository");
            System.out.println("4. Back");

            int choice = MenuUtils.getIntInput("Enter choice: ");

            if(choice == 1) {
                createRepository();
            } else if(choice == 2) {
                listRepositories();
            } else if(choice == 3) {
                openRepository();
            } else if(choice == 4) {
                return;
            } else {
                System.out.println("Invalid choice!");
                MenuUtils.waitForEnter();
            }
        }
    }

    private void createRepository() {
        String name = MenuUtils.getStringInput("Repository Name: ");
        boolean isPublic = MenuUtils.getStringInput("Public? (true/false): ").equalsIgnoreCase("true");
        long userId = Session.getCurrentUser().getUserId();

        repoService.createRepository(name, userId, isPublic);

        System.out.println("Repository Created!");
        MenuUtils.waitForEnter();
    }

    private void listRepositories() {
        long userId = Session.getCurrentUser().getUserId();

        List<Repository> repos = repoService.getRepositoriesByUser(userId);

        if(repos.isEmpty()) {
            System.out.println("No Repositories Found.");
        }
        for(Repository r : repos) {
            System.out.println(r.getRepoId() + " | " + r.getRepoName());
        }
        MenuUtils.waitForEnter();
    }

    private void openRepository() {
        long repoId = MenuUtils.getIntInput("Enter Repository ID: ");
        System.out.println(repoId);
        Repository repo = repoService.getRepositoryById(repoId);

        while(true) {
            MenuUtils.clearScreen();

            System.out.println("\n=== Repository: " + repo.getRepoName() + " ===");
            System.out.println("1. Commit Menu");
            System.out.println("2. Branch Menu");
            System.out.println("3. Files Menu");
            System.out.println("4. Back");

            int choice = MenuUtils.getIntInput("Enter choice: ");

            if(choice == 1) {
                CommitCLI commitCli = new CommitCLI();
                commitCli.showCommitMenu(repoId);
            } else if(choice == 2) {
                BranchCLI branchCli = new BranchCLI(repoId);
                branchCli.showMenu();
            } else if(choice == 3) {
                RepoFileCLI fileCli = new RepoFileCLI(repoId);
                fileCli.showMenu();
            } else if(choice == 4) {
                return;
            } else {
                System.out.println("Invalid choice!");
                MenuUtils.waitForEnter();
            }
        }
    }
}
