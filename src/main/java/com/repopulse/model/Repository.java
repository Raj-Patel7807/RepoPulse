package com.repopulse.model;

public class Repository {
    private long repoId;
    private String repoName;
    private String repoDesc;
    private long repoOwnerUserId;
    private String repoVisibilityType;
    private Long defaultBranchId;
    private Long parentRepositoryId;
    private Long forkedFromCommitId;

    public Repository() {}

    public long getRepoId() {
        return repoId;
    }
    public void setRepoId(long repoId) {
        this.repoId = repoId;
    }

    public String getRepoName() {
        return repoName;
    }
    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getRepoDesc() {
        return repoDesc;
    }
    public void setRepoDesc(String repoDesc) {
        this.repoDesc = repoDesc;
    }

    public long getRepoOwnerUserId() {
        return repoOwnerUserId;
    }
    public void setRepoOwnerUserId(long repoOwnerUserId) {
        this.repoOwnerUserId = repoOwnerUserId;
    }

    public String getRepoVisibilityType() {
        return repoVisibilityType;
    }
    public void setRepoVisibilityType(String repoVisibilityType) {
        this.repoVisibilityType = repoVisibilityType;
    }

    public Long getDefaultBranchId() {
        return defaultBranchId;
    }
    public void setDefaultBranchId(Long defaultBranchId) {
        this.defaultBranchId = defaultBranchId;
    }

    public Long getParentRepositoryId() {
        return parentRepositoryId;
    }
    public void setParentRepositoryId(Long parentRepositoryId) {
        this.parentRepositoryId = parentRepositoryId;
    }

    public Long getForkedFromCommitId() {
        return forkedFromCommitId;
    }
    public void setForkedFromCommitId(Long forkedFromCommitId) {
        this.forkedFromCommitId = forkedFromCommitId;
    }
}
