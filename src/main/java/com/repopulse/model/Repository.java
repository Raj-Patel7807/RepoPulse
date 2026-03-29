package com.repopulse.model;

public class Repository {
    private long repoId;
    private String repoName;
    private String repoDesc;
    private long repoOwnerUserId;
    private String repoVisibilityType;

    public Repository() {}

    public long getRepoId() {
        return this.repoId;
    }
    public void setRepoId(long repoId) {
        this.repoId = repoId;
    }

    public String getRepoName() {
        return this.repoName;
    }
    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getRepoDesc() {
        return this.repoDesc;
    }
    public void setRepoDesc(String repoDesc) {
        this.repoDesc = repoDesc;
    }

    public long getRepoOwnerUserId() {
        return this.repoOwnerUserId;
    }
    public void setRepoOwnerUserId(long repoOwnerUserId) {
        this.repoOwnerUserId = repoOwnerUserId;
    }

    public String getRepoVisibilityType() {
        return this.repoVisibilityType;
    }
    public void setRepoVisibilityType(String repoVisibilityType) {
        this.repoVisibilityType = repoVisibilityType;
    }
}
