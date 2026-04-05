package com.repopulse.pullrequest.model;

import java.sql.Timestamp;

public class PullRequest {
    private long pullRequestId;
    private long repositoryId;
    private long sourceBranchId;
    private long targetBranchId;
    private long createdByUserId;
    private String title;
    private String description;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public PullRequest() {

    }

    public long getPullRequestId() {
        return this.pullRequestId;
    }
    public void setPullRequestId(long pullRequestId) {
        this.pullRequestId = pullRequestId;
    }

    public long getRepositoryId() {
        return this.repositoryId;
    }
    public void setRepositoryId(long repositoryId) {
        this.repositoryId = repositoryId;
    }

    public long getSourceBranchId() {
        return this.sourceBranchId;
    }
    public void setSourceBranchId(long sourceBranchId) {
        this.sourceBranchId = sourceBranchId;
    }

    public long getTargetBranchId() {
        return this.targetBranchId;
    }
    public void setTargetBranchId(long targetBranchId) {
        this.targetBranchId = targetBranchId;
    }

    public long getCreatedByUserId() {
        return this.createdByUserId;
    }
    public void setCreatedByUserId(long createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return this.createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return this.updatedAt;
    }
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
