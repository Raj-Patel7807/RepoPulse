package com.repopulse.commit.cli;

import com.repopulse.commit.model.Commit;
import com.repopulse.commit.service.CommitService;
import com.repopulse.common.cli.CliUtils;
import com.repopulse.infra.session.Session;

import java.util.List;

public class CommitCLI {
    private final long repoId;

    private final CommitService commitService = new CommitService();

    public CommitCLI(long repoId) {
        this.repoId = repoId;
    }

    public void start() {
        while(true) {
            System.out.println("\n=== Commits ===");
            System.out.println("1. View Commits");
            System.out.println("2. Create Commit");
            System.out.println("3. Back");

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
                String commitMsg = CliUtils.getStringInput("Enter Commit Message: ");
                long parentCommit = 1;

                commitService.createCommit(repoId, Session.getCurrentUser().getUserId(), parentCommit, commitMsg);

            } else if(choice == 3) {
                return;
            } else {
                System.out.println("Invalid Choice..!!");
            }
        }
    }
}
