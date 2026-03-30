package com.repopulse.model;

public class Branch {
    private long branchId;
    private long repoId;
    private String branchName;
    private long headCommitId;

    public Branch() {

    }

    public Branch(long branchId, String branchName, long repoId, long headCommitId) {
        this.branchId = branchId;
        this.branchName = branchName;
        this.repoId = repoId;
        this.headCommitId = headCommitId;
    }

    public Branch(String branchName, long repoId) {
        this.branchName = branchName;
        this.repoId = repoId;
    }

    public long getBranchId() {
        return this.branchId;
    }
    public void setBranchId(long branchId) {
        this.branchId = branchId;
    }

    public long getRepoId() {
        return this.repoId;
    }
    public void setRepoId(long repoId) {
        this.repoId = repoId;
    }

    public String getBranchName() {
        return this.branchName;
    }
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public long getHeadCommitId() {
        return this.headCommitId;
    }
    public void setHeadCommitId(long headCommitId) {
        this.headCommitId = headCommitId;
    }
}
