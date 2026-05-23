package com.repopulse.repository.model;

import java.sql.Timestamp;

public class RepoCollaborator {
    private long repositoryId;
    private long userId;
    private String accessRole;
    private Timestamp joinedAt;

    public RepoCollaborator() {

    }

    public RepoCollaborator(long repositoryId, long userId, String accessRole) {
        this.repositoryId = repositoryId;
        this.userId = userId;
        this.accessRole = accessRole;
    }

    public long getRepositoryId() {
        return this.repositoryId;
    }

    public void setRepositoryId(long repositoryId) {
        this.repositoryId = repositoryId;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getAccessRole() {
        return this.accessRole;
    }

    public void setAccessRole(String accessRole) {
        this.accessRole = accessRole;
    }

    public Timestamp getJoinedAt() {
        return this.joinedAt;
    }

    public void setJoinedAt(Timestamp joinedAt) {
        this.joinedAt = joinedAt;
    }
}
