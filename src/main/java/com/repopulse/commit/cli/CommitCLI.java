package com.repopulse.commit.cli;

import com.repopulse.commit.model.Commit;
import com.repopulse.commit.service.CommitService;
import com.repopulse.infra.session.Authz;
import com.repopulse.infra.util.CliUtils;
import com.repopulse.infra.exception.AppException;
import com.repopulse.infra.session.Session;
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
            if(loggedIn) {
                System.out.println("2. Create Commit");
                System.out.println("3. Back");
            } else {
                System.out.println("2. Back");
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
            } else if(loggedIn && choice == 2) {
                Authz.requireLogin("create commit");
                if(!repositoryService.canWriteRepository(repoId, Session.getCurrentUser().getUserId())) {
                    throw new AppException("You do not have write access to this repository.");
                }
                String commitMsg = CliUtils.getStringInput("Enter Commit Message: ");
                Long parentCommit = null;

                commitService.createCommit(repoId, Session.getCurrentUser().getUserId(), parentCommit, commitMsg);

            } else if((loggedIn && choice == 3) || (!loggedIn && choice == 2)) {
                return;
            } else {
                System.out.println("Invalid Choice..!!");
            }
        }
    }
}
