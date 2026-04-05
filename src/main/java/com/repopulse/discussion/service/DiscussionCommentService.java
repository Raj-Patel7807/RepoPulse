package com.repopulse.discussion.service;

import com.repopulse.discussion.dao.DiscussionCommentDAO;
import com.repopulse.discussion.model.DiscussionComment;

import java.sql.Timestamp;
import java.util.List;

public class DiscussionCommentService {

    private final DiscussionCommentDAO commentDAO;

    public DiscussionCommentService() {
        this.commentDAO = new DiscussionCommentDAO();
    }

    public void createComment(long userId, Long commitId, Long pullRequestId, Long issueId, Long parentCommentId, String commentBody, Long fileId, Integer lineNumber, Timestamp createdAt) {

        int nonNullCount = 0;
        if(commitId != null) nonNullCount++;
        if(pullRequestId != null) nonNullCount++;
        if(issueId != null) nonNullCount++;
        if(nonNullCount != 1) {
            throw new IllegalArgumentException("Exactly one of commitId, pullRequestId, or issueId must be non-null");
        }

        DiscussionComment comment = new DiscussionComment();

        comment.setUserId(userId);
        comment.setCommitId(commitId);
        comment.setPullRequestId(pullRequestId);
        comment.setIssueId(issueId);
        comment.setParentCommentId(parentCommentId);
        comment.setCommentBody(commentBody);
        comment.setFileId(fileId);
        comment.setLineNumber(lineNumber);
        comment.setCreatedAt(createdAt);

        commentDAO.createComment(comment);
    }

    public DiscussionComment getComment(long commentId) {
        return commentDAO.getCommentById(commentId);
    }

    public List<DiscussionComment> getChildComments(long parentCommentId) {
        return commentDAO.getCommentsByParent(parentCommentId);
    }
}
