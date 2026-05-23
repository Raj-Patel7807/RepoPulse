package com.repopulse.pullrequest.cli;

import com.repopulse.infra.exception.AppException;
import com.repopulse.infra.session.Authz;
import com.repopulse.infra.session.Session;
import com.repopulse.infra.util.CliUtils;
import com.repopulse.pullrequest.model.PullRequest;
import com.repopulse.pullrequest.model.PullRequestIssueLink;
import com.repopulse.pullrequest.model.PullRequestReview;
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
            System.out.println("2. List Reviews for PR");
            System.out.println("3. List Linked Issues for PR");
            if(loggedIn) {
                System.out.println("4. Create PR");
                System.out.println("5. Add Review");
                System.out.println("6. Update Review");
                System.out.println("7. Link Issue");
                System.out.println("8. Merge PR");
                System.out.println("9. Close PR");
                System.out.println("10. Back");
            } else {
                System.out.println("4. Back");
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
            } else if(choice == 2) {
                long prId = CliUtils.getLongInput("Enter PR Id: ");
                List<PullRequestReview> reviews = pullRequestService.getReviewsForPullRequest(prId);
                if(reviews.isEmpty()) {
                    System.out.println("No reviews found.");
                } else {
                    System.out.println("review_id | reviewer | status | comment");
                    for(PullRequestReview review : reviews) {
                        System.out.println(review.getReviewId() + " | " + review.getReviewerUserId() + " | " + review.getReviewStatus() + " | " + review.getReviewComment());
                    }
                }
                CliUtils.waitForEnter();
            } else if(choice == 3) {
                long prId = CliUtils.getLongInput("Enter PR Id: ");
                List<PullRequestIssueLink> links = pullRequestService.getLinksByPullRequest(prId);
                if(links.isEmpty()) {
                    System.out.println("No linked issues found.");
                } else {
                    System.out.println("pr_id | issue_id | type");
                    for(PullRequestIssueLink link : links) {
                        System.out.println(link.getPullRequestId() + " | " + link.getIssueId() + " | " + link.getLinkType());
                    }
                }
                CliUtils.waitForEnter();
            } else if(loggedIn && choice == 4) {
                Authz.requireLogin("create pull request");
                ensureWriteAccess();
                long sourceBranch = CliUtils.getLongInput("Enter Source Branch Id: ");
                long targetBranch = CliUtils.getLongInput("Enter Target Branch Id: ");
                String title = CliUtils.getStringInput("Enter Title: ");
                String description = CliUtils.getStringInput("Enter Description: ");

                pullRequestService.createPullRequest(repoId, sourceBranch, targetBranch, Session.getCurrentUser().getUserId(), title, description);

            } else if(loggedIn && choice == 5) {
                Authz.requireLogin("review pull request");
                ensureWriteAccess();
                long PrId = CliUtils.getLongInput("Enter PR Id: ");
                String comment = CliUtils.getStringInput("Enter Review Comment: ");
                String status = CliUtils.getStringInput("Enter Review Status (APPROVED, CHANGES_REQUESTED, COMMENTED): ");

                pullRequestService.addReview(PrId, Session.getCurrentUser().getUserId(), comment, status);

            } else if(loggedIn && choice == 6) {
                Authz.requireLogin("update pull request review");
                ensureWriteAccess();
                long reviewId = CliUtils.getLongInput("Enter Review Id: ");
                String comment = CliUtils.getStringInput("Enter Review Comment: ");
                String status = CliUtils.getStringInput("Enter Review Status (APPROVED, CHANGES_REQUESTED, COMMENTED): ");
                pullRequestService.updateReview(reviewId, comment, status);
                System.out.println("Review updated.");
                CliUtils.waitForEnter();
            } else if(loggedIn && choice == 7) {
                Authz.requireLogin("link issue to pull request");
                ensureWriteAccess();
                long prId = CliUtils.getLongInput("Enter PR Id: ");
                long issueId = CliUtils.getLongInput("Enter Issue Id: ");
                String linkType = CliUtils.getStringInput("Enter Link Type (CLOSES/REFERENCES): ");
                pullRequestService.linkIssue(prId, issueId, linkType);
                System.out.println("Issue linked to PR.");
                CliUtils.waitForEnter();
            } else if(loggedIn && choice == 8) {
                Authz.requireLogin("merge pull request");
                ensureWriteAccess();
                long prId = CliUtils.getLongInput("Enter PR Id to merge: ");
                pullRequestService.updateStatus(prId, "MERGED");
                System.out.println("Pull request merged.");
                CliUtils.waitForEnter();
            } else if(loggedIn && choice == 9) {
                Authz.requireLogin("close pull request");
                ensureWriteAccess();
                long prId = CliUtils.getLongInput("Enter PR Id to close: ");
                pullRequestService.updateStatus(prId, "CLOSED");
                System.out.println("Pull request closed.");
                CliUtils.waitForEnter();
            } else if((loggedIn && choice == 10) || (!loggedIn && choice == 4)) {
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
