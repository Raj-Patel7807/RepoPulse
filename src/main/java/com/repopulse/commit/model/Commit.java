package com.repopulse.commit.model;

import java.sql.Timestamp;

public class Commit {
    private long commitId;
    private long repoId;
    private long authorUserId;
    private Long parentCommitId;
    private String commitHash;
    private String commitMessage;
    private Timestamp committedAt;

    public Commit() {}

    public long getCommitId() {
        return commitId;
    }
    public void setCommitId(long commitId) {
        this.commitId = commitId;
    }

    public long getRepoId() {
        return repoId;
    }
    public void setRepoId(long repoId) {
        this.repoId = repoId;
    }

    public long getAuthorUserId() {
        return authorUserId;
    }
    public void setAuthorUserId(long authorUserId) {
        this.authorUserId = authorUserId;
    }

    public Long getParentCommitId() {
        return parentCommitId;
    }
    public void setParentCommitId(Long parentCommitId) {
        this.parentCommitId = parentCommitId;
    }

    public String getCommitHash() {
        return commitHash;
    }
    public void setCommitHash(String commitHash) {
        this.commitHash = commitHash;
    }

    public String getCommitMessage() {
        return commitMessage;
    }
    public void setCommitMessage(String commitMessage) {
        this.commitMessage = commitMessage;
    }

    public Timestamp getCommittedAt() {
        return committedAt;
    }
    public void setCommittedAt(Timestamp committedAt) {
        this.committedAt = committedAt;
    }
}
