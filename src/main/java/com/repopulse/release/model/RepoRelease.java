package com.repopulse.release.model;

import java.sql.Timestamp;

public class RepoRelease {
    private long releaseId;
    private long repositoryId;
    private long tagId;
    private String releaseTitle;
    private String releaseNotes;
    private long createdByUserId;
    private Timestamp createdAt;

    public RepoRelease() {

    }

    public long getReleaseId() {
        return this.releaseId;
    }
    public void setReleaseId(long releaseId) {
        this.releaseId = releaseId;
    }

    public long getRepositoryId() {
        return this.repositoryId;
    }
    public void setRepositoryId(long repositoryId) {
        this.repositoryId = repositoryId;
    }

    public long getTagId() {
        return this.tagId;
    }
    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

    public String getReleaseTitle() {
        return this.releaseTitle;
    }
    public void setReleaseTitle(String releaseTitle) {
        this.releaseTitle = releaseTitle;
    }

    public String getReleaseNotes() {
        return this.releaseNotes;
    }
    public void setReleaseNotes(String releaseNotes) {
        this.releaseNotes = releaseNotes;
    }

    public long getCreatedByUserId() {
        return this.createdByUserId;
    }
    public void setCreatedByUserId(long createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public Timestamp getCreatedAt() {
        return this.createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
