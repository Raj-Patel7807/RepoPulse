package com.repopulse.repository.cli;

import com.repopulse.infra.util.CliUtils;
import com.repopulse.infra.session.Session;
import com.repopulse.repository.model.Repository;
import com.repopulse.repository.service.RepositoryService;

import java.util.List;

public class RepositoryCLI {

    private final RepositoryService repositoryService = new RepositoryService();

    public void start() {
        while(true) {
            System.out.println("\n=== My Repositories ===");
            System.out.println("1. List All Repositories");
            System.out.println("2. Open Repository");
            System.out.println("3. Create Repository");
            System.out.println("4. View Starred Repositories");
            System.out.println("5. Back");

            int choice = CliUtils.getIntInput("Enter Choice: ");

            if(choice == 1) {
                List<Repository> repos = repositoryService.getRepositoriesByUser(Session.getCurrentUser().getUserId());

                if(repos.isEmpty()) {
                    System.out.println("No Repositories Found!!");
                } else {
                    System.out.println("Repositories: ");
                    for(Repository repo : repos) {
                        System.out.println(repo.getRepoId() + " | " + repo.getRepoName());
                    }

                    CliUtils.waitForEnter();
                }
            } else if(choice == 2) {
                int repoId = CliUtils.getIntInput("Enter RepoID: ");

                RepoDetailCLI repoDetailCLI = new RepoDetailCLI(repoId);
                repoDetailCLI.start();
                
            } else if(choice == 3) {
                String name = CliUtils.getStringInput("Enter Repo Name: ");
                // I have to take Input for giving user choice to make repo Private Or Public;
                // Currently All Repo Public;
                Repository repo = repositoryService.createRepository(name, Session.getCurrentUser().getUserId(), true);
                if(repo == null) {
                    System.out.println("Error While Making Repository!!");
                } else {
                    System.out.println("Repository Created Successfully!!");
                }

            } else if(choice == 4) {
                List<Repository> repos = repositoryService.getStarredRepos(Session.getCurrentUser().getUserId());

                if(repos.isEmpty()) {
                    System.out.println("No Repositories Found!!");
                } else {
                    System.out.println("Starred Repositories: ");
                    for(Repository repo : repos) {
                        System.out.println(repo.getRepoId() + " | " + repo.getRepoName());
                    }

                    CliUtils.waitForEnter();
                }
            } else if(choice == 5) {
                return;
            } else {
                System.out.println("Invalid Choice!!");
                CliUtils.waitForEnter();
            }
        }
    }
}
