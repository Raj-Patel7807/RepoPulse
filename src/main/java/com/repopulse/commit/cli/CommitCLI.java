package com.repopulse.commit.cli;

import com.repopulse.commit.model.Commit;
import com.repopulse.commit.service.CommitService;
import com.repopulse.infra.exception.AppException;
import com.repopulse.infra.session.Authz;
import com.repopulse.infra.session.Session;
import com.repopulse.infra.util.CliUtils;
import com.repopulse.repository.service.RepositoryService;

import java.util.List;

public class CommitCLI {
    private final long repoId;

    private final CommitService commitService = new CommitService();
    private final RepositoryService repositoryService = new RepositoryService();

    public CommitCLI(long repoId) {
        this.repoId = repoId;
    }

    public void start() {
        while(true) {
            boolean loggedIn = Authz.isLoggedIn();
            System.out.println("\n=== Commits ===");
            System.out.println("1. View Commits");
            System.out.println("2. View Latest Commit");
            System.out.println("3. View Commit by ID");
            if(loggedIn) {
                System.out.println("4. Create Commit");
                System.out.println("5. Delete Commit");
                System.out.println("6. Back");
            } else {
                System.out.println("4. Back");
            }

            int choice = CliUtils.getIntInput("Enter Choice: ");

            if(choice == 1) {
                List<Commit> commits = commitService.getCommitsByRepo(repoId);

                if(commits.isEmpty()) {
                    System.out.println("No Commits Found!!");
                } else {
                    System.out.println("Commits: ");
                    for(Commit commit : commits) {
                        System.out.println(commit.getCommitId() + " | " + commit.getCommitMessage());
                    }
                }
            } else if(choice == 2) {
                Commit commit = commitService.getLatestCommit(repoId);
                if(commit == null) {
                    System.out.println("No commits found.");
                } else {
                    System.out.println(commit.getCommitId() + " | " + commit.getCommitHash() + " | " + commit.getCommitMessage());
                }
                CliUtils.waitForEnter();
            } else if(choice == 3) {
                long commitId = CliUtils.getLongInput("Enter Commit ID: ");
                Commit commit = commitService.getCommitById(commitId);
                if(commit == null || commit.getRepoId() != repoId) {
                    System.out.println("Commit not found for this repository.");
                } else {
                    System.out.println("ID: " + commit.getCommitId());
                    System.out.println("Author: " + commit.getAuthorUserId());
                    System.out.println("Hash: " + commit.getCommitHash());
                    System.out.println("Message: " + commit.getCommitMessage());
                    System.out.println("Committed At: " + commit.getCommittedAt());
                }
                CliUtils.waitForEnter();
            } else if(loggedIn && choice == 4) {
                Authz.requireLogin("create commit");
                if(!repositoryService.canWriteRepository(repoId, Session.getCurrentUser().getUserId())) {
                    throw new AppException("You do not have write access to this repository.");
                }
                String commitMsg = CliUtils.getStringInput("Enter Commit Message: ");
                Long parentCommit = null;

                commitService.createCommit(repoId, Session.getCurrentUser().getUserId(), parentCommit, commitMsg);

            } else if(loggedIn && choice == 5) {
                Authz.requireLogin("delete commit");
                if(!repositoryService.canWriteRepository(repoId, Session.getCurrentUser().getUserId())) {
                    throw new AppException("You do not have write access to this repository.");
                }
                long commitId = CliUtils.getLongInput("Enter Commit ID to delete: ");
                commitService.deleteCommit(commitId);
                System.out.println("Commit deleted.");
                CliUtils.waitForEnter();
            } else if((loggedIn && choice == 6) || (!loggedIn && choice == 4)) {
                return;
            } else {
                System.out.println("Invalid Choice..!!");
            }
        }
    }
}
