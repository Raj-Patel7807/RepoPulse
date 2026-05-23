package com.repopulse.repository.model;

import java.sql.Timestamp;

public class RepoClone {
    private long cloneId;
    private long repositoryId;
    private long clonedByUserId;
    private String cloneType;
    private Timestamp clonedAt;

    public RepoClone() {

    }

    public long getCloneId() {
        return this.cloneId;
    }

    public void setCloneId(long cloneId) {
        this.cloneId = cloneId;
    }

    public long getRepositoryId() {
        return this.repositoryId;
    }

    public void setRepositoryId(long repositoryId) {
        this.repositoryId = repositoryId;
    }

    public long getClonedByUserId() {
        return this.clonedByUserId;
    }

    public void setClonedByUserId(long clonedByUserId) {
        this.clonedByUserId = clonedByUserId;
    }

    public String getCloneType() {
        return this.cloneType;
    }

    public void setCloneType(String cloneType) {
        this.cloneType = cloneType;
    }

    public Timestamp getClonedAt() {
        return this.clonedAt;
    }

    public void setClonedAt(Timestamp clonedAt) {
        this.clonedAt = clonedAt;
    }
}
