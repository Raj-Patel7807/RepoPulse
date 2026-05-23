package com.repopulse.pullrequest.model;

import java.sql.Timestamp;

public class PullRequestReview {
    private long reviewId;
    private long pullRequestId;
    private long reviewerUserId;
    private String reviewComment;
    private String reviewStatus;
    private Timestamp reviewedAt;

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

    public Timestamp getReviewedAt() {
        return this.reviewedAt;
    }

    public void setReviewedAt(Timestamp reviewedAt) {
        this.reviewedAt = reviewedAt;
    }
}
