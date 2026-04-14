package com.repopulse.repository.cli;

import com.repopulse.branch.cli.BranchCLI;
import com.repopulse.commit.cli.CommitCLI;
import com.repopulse.common.cli.CliUtils;
import com.repopulse.infra.session.Session;
import com.repopulse.issue.cli.IssueCLI;
import com.repopulse.pullrequest.cli.PullRequestCLI;
import com.repopulse.release.cli.ReleaseCLI;
import com.repopulse.repository.model.Repository;
import com.repopulse.repository.service.RepositoryService;

public class RepoDetailCLI {
    private final RepositoryService repositoryService = new RepositoryService();

    private final Repository repo;

    public RepoDetailCLI(int repoId) {
        this.repo = repositoryService.getRepositoryById(repoId);
    }

    public void start() {
        while(true) {
            System.out.println("\n=== Repository " + repo.getRepoName() + " ===");
            System.out.println("Repo Id: " + repo.getRepoId());
            System.out.println("Total Starts: " + repositoryService.getRepositoryStars(repo.getRepoId()));
            System.out.println("Total Commits: " + "Pending..........");
            System.out.println();
            System.out.println("1. Code");
            System.out.println("2. Commits");
            System.out.println("3. Branches");
            System.out.println("4. Pull Requests");
            System.out.println("5. Issue");
            System.out.println("6. Releases");
            System.out.println("7. Collaborators");
            System.out.println("8. Star Repo");
            System.out.println("9. Unstar Repo");
            System.out.println("10. Back");

            int choice = CliUtils.getIntInput("Enter Choice: ");

            if(choice == 1) {
                System.out.println("Code Pending......");
            } else if(choice == 2) {
                CommitCLI commitCLI = new CommitCLI(repo.getRepoId());
                commitCLI.start();

            } else if(choice == 3) {
                BranchCLI branchCLI = new BranchCLI(repo.getRepoId());
                branchCLI.start();

            } else if(choice == 4) {
                PullRequestCLI pullRequestCLI = new PullRequestCLI(repo.getRepoId());
                pullRequestCLI.start();

            } else if(choice == 5) {
                IssueCLI issueCLI = new IssueCLI(repo.getRepoId());
                issueCLI.start();

            } else if(choice == 6) {
                ReleaseCLI releaseCLI = new ReleaseCLI(repo.getRepoId());
                releaseCLI.start();

            } else if(choice == 7) {
                System.out.println("Collaborators Pending..........");
            } else if(choice == 8) {
                repositoryService.starRepository(Session.getCurrentUser().getUserId(), repo.getRepoId());
                System.out.println("Repository Starred!!");

            } else if(choice == 9) {
                repositoryService.unstarRepository(Session.getCurrentUser().getUserId(), repo.getRepoId());
                System.out.println("Repository Unstarred!!");

            } else if(choice == 10) {
                return;
            } else {
                System.out.println("Invalid Choice!!");
                CliUtils.waitForEnter();
            }
        }
    }
}
