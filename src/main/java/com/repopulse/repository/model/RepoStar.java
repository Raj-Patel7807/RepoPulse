package com.repopulse.repository.model;

import java.sql.Timestamp;

public class RepoStar {
    private long userId;
    private long repositoryId;
    private Timestamp starredAt;

    public RepoStar() {

    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getRepositoryId() {
        return this.repositoryId;
    }

    public void setRepositoryId(long repositoryId) {
        this.repositoryId = repositoryId;
    }

    public Timestamp getStarredAt() {
        return this.starredAt;
    }

    public void setStarredAt(Timestamp starredAt) {
        this.starredAt = starredAt;
    }
}
