package com.repopulse.discussion.model;

import java.sql.Timestamp;

public class DiscussionComment {
    private long commentId;
    private long userId;
    private Long commitId;
    private Long pullRequestId;
    private Long issueId;
    private Long parentCommentId;
    private String commentBody;
    private Long fileId;
    private Integer lineNumber;
    private Timestamp createdAt;

    public DiscussionComment() {

    }

    public long getCommentId() {
        return this.commentId;
    }
    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public long getUserId() {
        return this.userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Long getCommitId() {
        return this.commitId;
    }
    public void setCommitId(Long commitId) {
        this.commitId = commitId;
    }

    public Long getPullRequestId() {
        return this.pullRequestId;
    }
    public void setPullRequestId(Long pullRequestId) {
        this.pullRequestId = pullRequestId;
    }

    public Long getIssueId() {
        return this.issueId;
    }
    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public Long getParentCommentId() {
        return this.parentCommentId;
    }
    public void setParentCommentId(Long parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public String getCommentBody() {
        return this.commentBody;
    }
    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public Long getFileId() {
        return this.fileId;
    }
    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Integer getLineNumber() {
        return this.lineNumber;
    }
    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public Timestamp getCreatedAt() {
        return this.createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
