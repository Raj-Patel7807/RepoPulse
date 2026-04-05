package com.repopulse.issue.model;

import java.sql.Timestamp;

public class IssueLabel {
    private long labelId;
    private long repositoryId;
    private String labelName;
    private String labelColor;
    private Timestamp createdAt;

    public IssueLabel() {}

    public long getLabelId() {
        return this.labelId;
    }
    public void setLabelId(long labelId) {
        this.labelId = labelId;
    }

    public long getRepositoryId() {
        return this.repositoryId;
    }
    public void setRepositoryId(long repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getLabelName() {
        return this.labelName;
    }
    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getLabelColor() {
        return this.labelColor;
    }
    public void setLabelColor(String labelColor) {
        this.labelColor = labelColor;
    }

    public Timestamp getCreatedAt() {
        return this.createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
