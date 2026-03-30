package com.repopulse.model;

public class PullRequestReview {
    private long reviewId;
    private long pullRequestId;
    private long reviewerUserId;
    private String reviewComment;
    private String reviewStatus;

    public PullRequestReview() {

    }

    public long getReviewId() {
        return this.reviewId;
    }
    public void setReviewId(long reviewId) {
        this.reviewId = reviewId;
    }

    public long getPullRequestId() {
        return this.pullRequestId;
    }
    public void setPullRequestId(long pullRequestId) {
        this.pullRequestId = pullRequestId;
    }

    public long getReviewerUserId() {
        return this.reviewerUserId;
    }
    public void setReviewerUserId(long reviewerUserId) {
        this.reviewerUserId = reviewerUserId;
    }

    public String getReviewComment() {
        return this.reviewComment;
    }
    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }

    public String getReviewStatus() {
        return this.reviewStatus;
    }
    public void setReviewStatus(String reviewStatus) {
        this.reviewStatus = reviewStatus;
    }
}
