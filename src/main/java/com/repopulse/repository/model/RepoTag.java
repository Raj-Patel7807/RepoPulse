package com.repopulse.repository.model;

import java.sql.Timestamp;

public class RepoTag {
    private long tagId;
    private long repositoryId;
    private long commitId;
    private String tagName;
    private String tagDescription;
    private Timestamp createdAt;

    public long getTagId() {
        return tagId;
    }
    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

    public long getRepositoryId() {
        return repositoryId;
    }
    public void setRepositoryId(long repositoryId) {
        this.repositoryId = repositoryId;
    }

    public long getCommitId() {
        return commitId;
    }
    public void setCommitId(long commitId) {
        this.commitId = commitId;
    }

    public String getTagName() {
        return tagName;
    }
    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagDescription() {
        return tagDescription;
    }
    public void setTagDescription(String tagDescription) {
        this.tagDescription = tagDescription;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
