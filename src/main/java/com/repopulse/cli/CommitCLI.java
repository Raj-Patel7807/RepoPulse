package com.repopulse.cli;

import com.repopulse.model.Commit;
import com.repopulse.service.CommitService;
import com.repopulse.service.RepositoryService;
import com.repopulse.util.Session;

import java.util.List;

public class CommitCLI {

    private final CommitService commitService = new CommitService();
    private final RepositoryService repoService = new RepositoryService();

    public void showCommitMenu(long repoId) {
        while(true) {
            MenuUtils.clearScreen();

            System.out.println("\n=== Repository: " + repoService.getRepositoryById(repoId).getRepoName() + " ===");
            System.out.println("1. View Commits");
            System.out.println("2. Add Commit");
            System.out.println("3. Back");

            int choice = MenuUtils.getIntInput("Enter choice: ");

            if(choice == 1) {
                viewCommits(repoId);
            } else if(choice == 2) {
                addCommit(repoId);
            } else if(choice == 3) {
                return;
            } else {
                System.out.println("Invalid choice!");
                MenuUtils.waitForEnter();
            }
        }
    }

    private void viewCommits(long repoId) {
        List<Commit> commits = commitService.getCommitsByRepo(repoId);

        if(commits.isEmpty()) {
            System.out.println("No commits found...");
        }

        for(Commit c : commits) {
            System.out.println(c.getCommitId() + " | " + c.getCommitMessage());
        }
        MenuUtils.waitForEnter();
    }

    private void addCommit(long repoId) {
        String message = MenuUtils.getStringInput("Commit message: ");

        long authorId = Session.getCurrentUser().getUserId();

        List<Commit> commits = commitService.getCommitsByRepo(repoId);
        Long parentCommitId = null;

        if(!commits.isEmpty()) {
            parentCommitId = commits.get(commits.size() - 1).getCommitId();
        }

        commitService.createCommit(repoId, authorId, parentCommitId, message);

        System.out.println("Commit Added..!");
        MenuUtils.waitForEnter();
    }
}
