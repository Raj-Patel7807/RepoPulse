package com.repopulse.user.model;

import java.sql.Timestamp;

public class UserPinnedRepo {
    private long userId;
    private long repositoryId;
    private Timestamp pinnedAt;

    public UserPinnedRepo() {

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

    public Timestamp getPinnedAt() {
        return this.pinnedAt;
    }

    public void setPinnedAt(Timestamp pinnedAt) {
        this.pinnedAt = pinnedAt;
    }
}
