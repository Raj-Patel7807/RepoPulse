package com.repopulse.model;

public class Commit {
    private long commitId;
    private long repoId;
    private long authorUserId;
    private Long parentCommitId;
    private String commitMessage;

    public Commit() {}

    public long getCommitId() {
        return this.commitId;
    }
    public void setCommitId(long commitId) {
        this.commitId = commitId;
    }

    public long getRepoId() {
        return this.repoId;
    }
    public void setRepoId(long repoId) {
        this.repoId = repoId;
    }

    public long getAuthorUserId() {
        return this.authorUserId;
    }
    public void setAuthorUserId(long authorUserId) {
        this.authorUserId = authorUserId;
    }

    public Long getParentCommitId() {
        return this.parentCommitId;
    }
    public void setParentCommitId(Long parentCommitId) {
        this.parentCommitId = parentCommitId;
    }

    public String getCommitMessage() {
        return this.commitMessage;
    }
    public void setCommitMessage(String commitMessage) {
        this.commitMessage = commitMessage;
    }
}
