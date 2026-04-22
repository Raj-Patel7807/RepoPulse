package com.repopulse.pullrequest.cli;

import com.repopulse.infra.session.Authz;
import com.repopulse.infra.util.CliUtils;
import com.repopulse.infra.exception.AppException;
import com.repopulse.infra.session.Session;
import com.repopulse.pullrequest.model.PullRequest;
import com.repopulse.pullrequest.service.PullRequestService;
import com.repopulse.repository.service.RepositoryService;

import java.util.List;

public class PullRequestCLI {
    private final long repoId;

    private final PullRequestService pullRequestService = new PullRequestService();
    private final RepositoryService repositoryService = new RepositoryService();

    public PullRequestCLI(long repoId) {
        this.repoId = repoId;
    }

    public void start() {
        while(true) {
            boolean loggedIn = Authz.isLoggedIn();
            System.out.println("\n=== Pull Requests ===");
            System.out.println("1. List PRs");
            if(loggedIn) {
                System.out.println("2. Create PR");
                System.out.println("3. Review PR");
                System.out.println("4. Merge PR");
                System.out.println("5. Back");
            } else {
                System.out.println("2. Back");
            }

            int choice = CliUtils.getIntInput("Enter Choice: ");

            if(choice == 1) {
                List<PullRequest> pullRequests = pullRequestService.getPullRequestsForRepo(repoId);

                if(pullRequests.isEmpty()) {
                    System.out.println("No Pull Requests Found..!!");
                } else {
                    System.out.println("Pull Requests: ");
                    for(PullRequest pullRequest : pullRequests) {
                        System.out.println(pullRequest.getPullRequestId() + " | " + pullRequest.getDescription() + " | " + pullRequest.getStatus());
                    }
                }
            } else if(loggedIn && choice == 2) {
                Authz.requireLogin("create pull request");
                ensureWriteAccess();
                long sourceBranch = CliUtils.getLongInput("Enter Source Branch Id: ");
                long targetBranch = CliUtils.getLongInput("Enter Target Branch Id: ");
                String title = CliUtils.getStringInput("Enter Title: ");
                String description = CliUtils.getStringInput("Enter Description: ");

                pullRequestService.createPullRequest(repoId, sourceBranch, targetBranch, Session.getCurrentUser().getUserId(), title, description);

            } else if(loggedIn && choice == 3) {
                Authz.requireLogin("review pull request");
                ensureWriteAccess();
                long PrId = CliUtils.getLongInput("Enter PR Id: ");
                String comment = CliUtils.getStringInput("Enter Review Comment: ");
                String status = CliUtils.getStringInput("Enter Review Status (APPROVED, CHANGES_REQUESTED, COMMENTED): ");

                pullRequestService.addReview(PrId, Session.getCurrentUser().getUserId(), comment, status);

            } else if(loggedIn && choice == 4) {
                Authz.requireLogin("merge pull request");
                ensureWriteAccess();
                long prId = CliUtils.getLongInput("Enter PR Id to merge: ");
                pullRequestService.updateStatus(prId, "MERGED");
                System.out.println("Pull request merged.");
                CliUtils.waitForEnter();
            } else if((loggedIn && choice == 5) || (!loggedIn && choice == 2)) {
                return;
            } else {
                System.out.println("Invalid Choice..!!");
            }
        }
    }

    private void ensureWriteAccess() {
        if(Session.getCurrentUser() == null) {
            throw new AppException("Please login first.");
        }
        if(!repositoryService.canWriteRepository(repoId, Session.getCurrentUser().getUserId())) {
            throw new AppException("You do not have write access to this repository.");
        }
    }
}
