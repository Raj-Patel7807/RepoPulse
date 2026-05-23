package com.repopulse.branch.model;

import java.sql.Timestamp;

public class BranchMerge {
    private long mergeId;
    private long repositoryId;
    private long sourceBranchId;
    private long targetBranchId;
    private long mergeCommitId;
    private long mergedByUserId;
    private MergeStrategy mergeStrategy;
    private Timestamp mergedAt;

    public BranchMerge() {

    }

    public long getMergeId() {
        return this.mergeId;
    }

    public void setMergeId(long mergeId) {
        this.mergeId = mergeId;
    }

    public long getRepositoryId() {
        return this.repositoryId;
    }

    public void setRepositoryId(long repositoryId) {
        this.repositoryId = repositoryId;
    }

    public long getSourceBranchId() {
        return this.sourceBranchId;
    }

    public void setSourceBranchId(long sourceBranchId) {
        this.sourceBranchId = sourceBranchId;
    }

    public long getTargetBranchId() {
        return this.targetBranchId;
    }

    public void setTargetBranchId(long targetBranchId) {
        this.targetBranchId = targetBranchId;
    }

    public long getMergeCommitId() {
        return this.mergeCommitId;
    }

    public void setMergeCommitId(long mergeCommitId) {
        this.mergeCommitId = mergeCommitId;
    }

    public long getMergedByUserId() {
        return this.mergedByUserId;
    }

    public void setMergedByUserId(long mergedByUserId) {
        this.mergedByUserId = mergedByUserId;
    }

    public MergeStrategy getMergeStrategy() {
        return this.mergeStrategy;
    }

    public void setMergeStrategy(MergeStrategy mergeStrategy) {
        this.mergeStrategy = mergeStrategy;
    }

    public Timestamp getMergedAt() {
        return this.mergedAt;
    }

    public void setMergedAt(Timestamp mergedAt) {
        this.mergedAt = mergedAt;
    }

    public enum MergeStrategy {
        MERGE, SQUASH, REBASE
    }
}
