package com.repopulse.repository.model;

import java.sql.Timestamp;

public class RepoWatcher {
    private long userId;
    private long repositoryId;
    private WatchLevel watchLevel;
    private Timestamp watchedAt;

    public enum WatchLevel {
        ALL,
        PARTICIPATING,
        NONE
    }

    public RepoWatcher() {

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

    public WatchLevel getWatchLevel() {
        return this.watchLevel;
    }
    public void setWatchLevel(WatchLevel watchLevel) {
        this.watchLevel = watchLevel;
    }

    public Timestamp getWatchedAt() {
        return this.watchedAt;
    }
    public void setWatchedAt(Timestamp watchedAt) {
        this.watchedAt = watchedAt;
    }
}
