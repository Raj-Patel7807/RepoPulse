package com.repopulse.issue.model;

import java.sql.Timestamp;

public class RepoIssue {
    private long issueId;
    private long repositoryId;
    private long createdByUserId;
    private Long assignedToUserId;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private Timestamp createdAt;
    private Timestamp closedAt;
    private Long milestoneId;

    public enum Status {
        OPEN,
        CLOSED
    }

    public enum Priority {
        LOW,
        MEDIUM,
        HIGH
    }

    public RepoIssue() {

    }

    public long getIssueId() {
        return this.issueId;
    }
    public void setIssueId(long issueId) {
        this.issueId = issueId;
    }

    public long getRepositoryId() {
        return this.repositoryId;
    }
    public void setRepositoryId(long repositoryId) {
        this.repositoryId = repositoryId;
    }

    public long getCreatedByUserId() {
        return this.createdByUserId;
    }
    public void setCreatedByUserId(long createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public Long getAssignedToUserId() {
        return this.assignedToUserId;
    }
    public void setAssignedToUserId(Long assignedToUserId) {
        this.assignedToUserId = assignedToUserId;
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

    public Status getStatus() {
        return this.status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return this.priority;
    }
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Timestamp getCreatedAt() {
        return this.createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getClosedAt() {
        return this.closedAt;
    }
    public void setClosedAt(Timestamp closedAt) {
        this.closedAt = closedAt;
    }

    public Long getMilestoneId() {
        return this.milestoneId;
    }
    public void setMilestoneId(Long milestoneId) {
        this.milestoneId = milestoneId;
    }
}
