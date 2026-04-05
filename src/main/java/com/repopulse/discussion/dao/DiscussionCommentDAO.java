package com.repopulse.discussion.dao;

import com.repopulse.infra.database.DBConnection;
import com.repopulse.discussion.model.DiscussionComment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class DiscussionCommentDAO {
    private final Connection conn;

    public DiscussionCommentDAO() {
        this.conn = DBConnection.getConnection();
    }

    public void createComment(DiscussionComment comment) {
        String sql = """
            INSERT INTO discussion_comments
            (user_id, commit_id, pull_request_id, issue_id, parent_comment_id, comment_body, file_id, line_number, created_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try(PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, comment.getUserId());

            if(comment.getCommitId() != null) stmt.setLong(2, comment.getCommitId());
            else stmt.setNull(2, Types.BIGINT);

            if(comment.getPullRequestId() != null) stmt.setLong(3, comment.getPullRequestId());
            else stmt.setNull(3, Types.BIGINT);

            if(comment.getIssueId() != null) stmt.setLong(4, comment.getIssueId());
            else stmt.setNull(4, Types.BIGINT);

            if(comment.getParentCommentId() != null) stmt.setLong(5, comment.getParentCommentId());
            else stmt.setNull(5, Types.BIGINT);

            stmt.setString(6, comment.getCommentBody());

            if(comment.getFileId() != null) stmt.setLong(7, comment.getFileId());
            else stmt.setNull(7, Types.BIGINT);

            if(comment.getLineNumber() != null) stmt.setInt(8, comment.getLineNumber());
            else stmt.setNull(8, Types.INTEGER);

            stmt.setTimestamp(9, comment.getCreatedAt());

            int affectedRows = stmt.executeUpdate();
            if(affectedRows == 0) {
                throw new SQLException("Creating DiscussionComment failed, no rows affected.");
            }

            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) {
                    comment.setCommentId(rs.getLong(1));
                }
            }

        } catch(SQLException e) {
            throw new RuntimeException("Error creating DiscussionComment", e);
        }
    }

    public DiscussionComment getCommentById(long commentId) {
        String sql = "SELECT * FROM discussion_comments WHERE comment_id = ?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, commentId);

            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) return mapResultSetToComment(rs);
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error fetching DiscussionComment", e);
        }
        return null;
    }

    public List<DiscussionComment> getCommentsByParent(long parentCommentId) {
        List<DiscussionComment> comments = new ArrayList<>();

        String sql = "SELECT * FROM discussion_comments WHERE parent_comment_id = ? ORDER BY created_at ASC";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, parentCommentId);
            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    comments.add(mapResultSetToComment(rs));
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error fetching child DiscussionComments", e);
        }
        return comments;
    }

    private DiscussionComment mapResultSetToComment(ResultSet rs) throws SQLException {
        DiscussionComment comment = new DiscussionComment();

        comment.setCommentId(rs.getLong("comment_id"));
        comment.setUserId(rs.getLong("user_id"));

        long commitId = rs.getLong("commit_id");
        if(!rs.wasNull()) comment.setCommitId(commitId);

        long pullRequestId = rs.getLong("pull_request_id");
        if(!rs.wasNull()) comment.setPullRequestId(pullRequestId);

        long issueId = rs.getLong("issue_id");
        if(!rs.wasNull()) comment.setIssueId(issueId);

        long parentId = rs.getLong("parent_comment_id");
        if(!rs.wasNull()) comment.setParentCommentId(parentId);

        comment.setCommentBody(rs.getString("comment_body"));

        long fileId = rs.getLong("file_id");
        if(!rs.wasNull()) comment.setFileId(fileId);

        int lineNumber = rs.getInt("line_number");
        if(!rs.wasNull()) comment.setLineNumber(lineNumber);

        comment.setCreatedAt(rs.getTimestamp("created_at"));
        return comment;
    }
}
