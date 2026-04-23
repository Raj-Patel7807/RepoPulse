package com.repopulse.pullrequest.service;

import com.repopulse.pullrequest.dao.PullRequestDAO;
import com.repopulse.pullrequest.model.PullRequest;
import com.repopulse.pullrequest.model.PullRequestIssueLink;
import com.repopulse.pullrequest.model.PullRequestReview;
import com.repopulse.pullrequest.validator.PullRequestValidator;

import java.util.List;

public class PullRequestService {

    private final PullRequestDAO pullRequestDAO;

    public PullRequestService() {
        this.pullRequestDAO = new PullRequestDAO();
    }

    public void createPullRequest(long repoId, long sourceBranchId, long targetBranchId, long createdByUserId, String title, String description) {
        PullRequest pr = new PullRequest();

        pr.setRepositoryId(repoId);
        pr.setSourceBranchId(sourceBranchId);
        pr.setTargetBranchId(targetBranchId);
        pr.setCreatedByUserId(createdByUserId);
        pr.setTitle(title);
        pr.setDescription(description);
        pr.setStatus("OPEN");

        pullRequestDAO.createPullRequest(pr);
    }

    public void updateStatus(long prId, String status) {
        PullRequestValidator.validateStatus(status);
        pullRequestDAO.updatePullRequestStatus(prId, status);
    }

    public PullRequest getPullRequest(long prId) {
        return pullRequestDAO.getPullRequestById(prId);
    }

    public List<PullRequest> getPullRequestsForRepo(long repoId) {
        return pullRequestDAO.getPullRequestsByRepo(repoId);
    }

    public List<PullRequest> getPullRequestsByUser(long userId) {
        return pullRequestDAO.getPullRequestsByUser(userId);
    }

    public void linkIssue(long pullRequestId, long issueId, String linkTypeStr) {
        PullRequestIssueLink link = new PullRequestIssueLink();
        link.setPullRequestId(pullRequestId);
        link.setIssueId(issueId);
        link.setLinkType(PullRequestValidator.validateLinkType(linkTypeStr));
        pullRequestDAO.createLink(link);
    }

    public List<PullRequestIssueLink> getLinksByPullRequest(long pullRequestId) {
        return pullRequestDAO.getLinksByPullRequest(pullRequestId);
    }

    public void addReview(long prId, long reviewerUserId, String reviewComment, String reviewStatus) {
        PullRequestValidator.validateReviewStatus(reviewStatus);

        PullRequestReview review = new PullRequestReview();
        review.setPullRequestId(prId);
        review.setReviewerUserId(reviewerUserId);
        review.setReviewComment(reviewComment);
        review.setReviewStatus(reviewStatus);

        pullRequestDAO.createReview(review);
    }

    public void updateReview(long reviewId, String reviewComment, String reviewStatus) {
        PullRequestValidator.validateReviewStatus(reviewStatus);
        pullRequestDAO.updateReview(reviewId, reviewStatus, reviewComment);
    }

    public List<PullRequestReview> getReviewsForPullRequest(long prId) {
        return pullRequestDAO.getReviewsByPullRequest(prId);
    }

    public List<PullRequestReview> getReviewsByUser(long userId) {
        return pullRequestDAO.getReviewsByUser(userId);
    }

}
