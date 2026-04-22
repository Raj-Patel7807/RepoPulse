package com.repopulse.repository.cli;

import com.repopulse.infra.util.CliUtils;
import com.repopulse.repository.model.Repository;
import com.repopulse.repository.service.RepositoryService;

import java.util.List;

public class ExploreRepositoryCLI {
    private final RepositoryService repositoryService = new RepositoryService();

    public void start() {
        while(true) {
            System.out.println("\n=== Explore Repositories ===");
            System.out.println("1. List Public Repositories");
            System.out.println("2. Open Repository");
            System.out.println("3. Back");

            int choice = CliUtils.getIntInput("Enter Choice: ");

            if(choice == 1) {
                List<Repository> repos = repositoryService.getAllPublicRepositories();
                if(repos.isEmpty()) {
                    System.out.println("No repositories found.");
                } else {
                    for(Repository r : repos) {
                        System.out.println(r.getRepoId() + " | " + r.getRepoName() + " | owner=" + r.getRepoOwnerUserId());
                    }
                }
                CliUtils.waitForEnter();
            } else if(choice == 2) {
                long repoId = CliUtils.getLongInput("Enter Repo ID: ");
                RepoDetailCLI repoDetailCLI = new RepoDetailCLI(repoId);
                repoDetailCLI.start();
            } else if(choice == 3) {
                return;
            } else {
                System.out.println("Invalid choice!");
                CliUtils.waitForEnter();
            }
        }
    }
}
