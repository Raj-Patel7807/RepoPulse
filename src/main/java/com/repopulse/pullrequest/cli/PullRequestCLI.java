package com.repopulse.pullrequest.cli;

import com.repopulse.common.cli.CliUtils;
import com.repopulse.infra.session.Session;
import com.repopulse.pullrequest.model.PullRequest;
import com.repopulse.pullrequest.service.PullRequestService;

import java.util.List;

public class PullRequestCLI {
    private final long repoId;

    private final PullRequestService pullRequestService = new PullRequestService();

    public PullRequestCLI(long repoId) {
        this.repoId = repoId;
    }

    public void start() {
        while(true) {
            System.out.println("\n=== Pull Requests ===");
            System.out.println("1. List PRs");
            System.out.println("2. Create PR");
            System.out.println("3. Review PR");
            System.out.println("4. Merge PR");
            System.out.println("5. Back");

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
            } else if(choice == 2) {
                long sourceBranch = CliUtils.getLongInput("Enter Source Branch Id: ");
                long targetBranch = CliUtils.getLongInput("Enter Target Branch Id: ");
                String title = CliUtils.getStringInput("Enter Title: ");
                String description = CliUtils.getStringInput("Enter Description: ");

                pullRequestService.createPullRequest(repoId, sourceBranch, targetBranch, Session.getCurrentUser().getUserId(), title, description);

            } else if(choice == 3) {
                long PrId = CliUtils.getLongInput("Enter PR Id: ");
                String comment = CliUtils.getStringInput("Enter Review Comment: ");
                String status = CliUtils.getStringInput("Enter Review Status (APPROVED, CHANGES_REQUESTED, COMMENTED): ");

                pullRequestService.addReview(PrId, Session.getCurrentUser().getUserId(), comment, status);

            } else if(choice == 4) {
                System.out.println("Pending Merge PR...........");
            } else if(choice == 5) {
                return;
            } else {
                System.out.println("Invalid Choice..!!");
            }
        }
    }
}
