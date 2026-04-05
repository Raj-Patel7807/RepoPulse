package com.repopulse.issue.dao;

import com.repopulse.infra.database.DBConnection;
import com.repopulse.issue.model.IssueLabel;
import com.repopulse.issue.model.RepoIssue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class RepoIssueDAO {
    private final Connection conn;

    public RepoIssueDAO() {
        this.conn = DBConnection.getConnection();
    }

    public void createRepoIssue(RepoIssue issue) {
        String sql = """
                    INSERT INTO repo_issues
                    (repository_id, created_by_user_id, assigned_to_user_id, title, description, status, priority, created_at, closed_at, milestone_id)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """;

        try(PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, issue.getRepositoryId());
            stmt.setLong(2, issue.getCreatedByUserId());

            if(issue.getAssignedToUserId() != null) {
                stmt.setLong(3, issue.getAssignedToUserId());
            } else {
                stmt.setNull(3, Types.BIGINT);
            }

            stmt.setString(4, issue.getTitle());
            stmt.setString(5, issue.getDescription());
            stmt.setString(6, issue.getStatus().name());
            stmt.setString(7, issue.getPriority().name());
            stmt.setTimestamp(8, issue.getCreatedAt());

            if(issue.getClosedAt() != null) {
                stmt.setTimestamp(9, issue.getClosedAt());
            } else {
                stmt.setNull(9, Types.TIMESTAMP);
            }

            if(issue.getMilestoneId() != null) {
                stmt.setLong(10, issue.getMilestoneId());
            } else {
                stmt.setNull(10, Types.BIGINT);
            }

            int affectedRows = stmt.executeUpdate();
            if(affectedRows == 0) {
                throw new SQLException("Creating RepoIssue failed, no rows affected.");
            }

            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) {
                    issue.setIssueId(rs.getLong(1));
                }
            }

        } catch(SQLException e) {
            throw new RuntimeException("Error creating RepoIssue", e);
        }
    }

    public RepoIssue getRepoIssueById(long issueId) {
        String sql = "SELECT * FROM repo_issues WHERE issue_id = ?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, issueId);

            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    return mapResultSetToRepoIssue(rs);
                }
            }

        } catch(SQLException e) {
            throw new RuntimeException("Error fetching RepoIssue", e);
        }

        return null;
    }

    public List<RepoIssue> getIssuesByRepositoryId(long repositoryId) {
        List<RepoIssue> issues = new ArrayList<>();

        String sql = "SELECT * FROM repo_issues WHERE repository_id = ? ORDER BY created_at ASC";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, repositoryId);

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    issues.add(mapResultSetToRepoIssue(rs));
                }
            }

        } catch(SQLException e) {
            throw new RuntimeException("Error fetching RepoIssues", e);
        }

        return issues;
    }

    public void createLabel(IssueLabel label) {
        String sql = "INSERT INTO issue_labels (repository_id, label_name, label_color, created_at) VALUES (?, ?, ?, ?)";

        try(PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, label.getRepositoryId());
            stmt.setString(2, label.getLabelName());
            stmt.setString(3, label.getLabelColor());
            stmt.setTimestamp(4, label.getCreatedAt());

            int affectedRows = stmt.executeUpdate();
            if(affectedRows == 0) throw new SQLException("Creating label failed.");

            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) label.setLabelId(rs.getLong(1));
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error creating label", e);
        }
    }

    public List<IssueLabel> getLabelsByRepo(long repositoryId) {
        List<IssueLabel> labels = new ArrayList<>();

        String sql = "SELECT * FROM issue_labels WHERE repository_id = ?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, repositoryId);

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    IssueLabel label = new IssueLabel();
                    label.setLabelId(rs.getLong("label_id"));
                    label.setRepositoryId(rs.getLong("repository_id"));
                    label.setLabelName(rs.getString("label_name"));
                    label.setLabelColor(rs.getString("label_color"));
                    label.setCreatedAt(rs.getTimestamp("created_at"));
                    labels.add(label);
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error fetching labels", e);
        }
        return labels;
    }

    public void assignLabel(long issueId, long labelId) {
        String sql = "INSERT INTO issue_label_mappings (issue_id, label_id) VALUES (?, ?)";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, issueId);
            stmt.setLong(2, labelId);
            stmt.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Error assigning label", e);
        }
    }

    public List<Long> getLabelIdsForIssue(long issueId) {
        List<Long> labelIds = new ArrayList<>();

        String sql = "SELECT label_id FROM issue_label_mappings WHERE issue_id = ?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, issueId);

            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) labelIds.add(rs.getLong("label_id"));
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error fetching labels for issue", e);
        }
        return labelIds;
    }

    private RepoIssue mapResultSetToRepoIssue(ResultSet rs) throws SQLException {
        RepoIssue issue = new RepoIssue();

        issue.setIssueId(rs.getLong("issue_id"));
        issue.setRepositoryId(rs.getLong("repository_id"));
        issue.setCreatedByUserId(rs.getLong("created_by_user_id"));

        long assignedTo = rs.getLong("assigned_to_user_id");
        if(!rs.wasNull()) {
            issue.setAssignedToUserId(assignedTo);
        }

        issue.setTitle(rs.getString("title"));
        issue.setDescription(rs.getString("description"));
        issue.setStatus(RepoIssue.Status.valueOf(rs.getString("status")));
        issue.setPriority(RepoIssue.Priority.valueOf(rs.getString("priority")));
        issue.setCreatedAt(rs.getTimestamp("created_at"));
        issue.setClosedAt(rs.getTimestamp("closed_at"));

        long milestone = rs.getLong("milestone_id");
        if(!rs.wasNull()) {
            issue.setMilestoneId(milestone);
        }

        return issue;
    }
}
