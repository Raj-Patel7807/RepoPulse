package com.repopulse.issue.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Milestone {
    private long milestoneId;
    private long repositoryId;
    private String title;
    private String description;
    private Date dueDate;
    private Status status;
    private Timestamp createdAt;
    private Timestamp closedAt;

    public Milestone() {

    }

    public long getMilestoneId() {
        return this.milestoneId;
    }

    public void setMilestoneId(long milestoneId) {
        this.milestoneId = milestoneId;
    }

    public long getRepositoryId() {
        return this.repositoryId;
    }

    public void setRepositoryId(long repositoryId) {
        this.repositoryId = repositoryId;
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

    public Date getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public enum Status {
        OPEN, CLOSED
    }
}
