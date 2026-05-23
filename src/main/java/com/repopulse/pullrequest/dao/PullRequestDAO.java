package com.repopulse.pullrequest.dao;

import com.repopulse.infra.database.DBConnection;
import com.repopulse.pullrequest.model.PullRequest;
import com.repopulse.pullrequest.model.PullRequestIssueLink;
import com.repopulse.pullrequest.model.PullRequestReview;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PullRequestDAO {
    private final Connection conn;

    public PullRequestDAO() {
        this.conn = DBConnection.getConnection();
    }

    public void createPullRequest(PullRequest pr) {
        String sql = """
                INSERT INTO pull_requests (repository_id, source_branch_id, target_branch_id, created_by_user_id, title, description, status)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try(PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, pr.getRepositoryId());
            stmt.setLong(2, pr.getSourceBranchId());
            stmt.setLong(3, pr.getTargetBranchId());
            stmt.setLong(4, pr.getCreatedByUserId());
            stmt.setString(5, pr.getTitle());
            stmt.setString(6, pr.getDescription());
            stmt.setString(7, pr.getStatus());

            int affectedRows = stmt.executeUpdate();
            if(affectedRows == 0) {
                throw new SQLException("Creating PullRequest failed, no rows affected.");
            }

            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) {
                    pr.setPullRequestId(rs.getLong(1));
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error creating PullRequest", e);
        }
    }

    public void updatePullRequestStatus(long prId, String status) {
        String sql = """
                UPDATE pull_requests SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE pull_request_id = ?
                """;

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setLong(2, prId);

            int affectedRows = stmt.executeUpdate();
            if(affectedRows == 0) {
                throw new SQLException("Updating PullRequest failed, no rows affected.");
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error updating PullRequest status", e);
        }
    }

    public PullRequest getPullRequestById(long prId) {
        String sql = """
                SELECT * FROM pull_requests WHERE pull_request_id = ?
                """;

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, prId);

            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    return mapResultSetToPullRequest(rs);
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error fetching PullRequest", e);
        }
        return null;
    }

    public List<PullRequest> getPullRequestsByRepo(long repoId) {
        List<PullRequest> prs = new ArrayList<>();

        String sql = """
                SELECT * FROM pull_requests WHERE repository_id = ? ORDER BY created_at DESC
                """;

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, repoId);

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    prs.add(mapResultSetToPullRequest(rs));
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error fetching PullRequests", e);
        }

        return prs;
    }

    public List<PullRequest> getPullRequestsByUser(long userId) {
        List<PullRequest> prs = new ArrayList<>();

        String sql = """
                SELECT * FROM pull_requests WHERE created_by_user_id = ? ORDER BY created_at DESC
                """;

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    prs.add(mapResultSetToPullRequest(rs));
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error fetching PullRequests by user", e);
        }

        return prs;
    }

    public void createLink(PullRequestIssueLink link) {
        String sql = "INSERT INTO pull_request_issue_links (pull_request_id, issue_id, link_type) VALUES (?, ?, ?)";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, link.getPullRequestId());
            stmt.setLong(2, link.getIssueId());
            stmt.setString(3, link.getLinkType().name());
            stmt.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Error creating pull request issue link", e);
        }
    }

    public List<PullRequestIssueLink> getLinksByPullRequest(long pullRequestId) {
        List<PullRequestIssueLink> links = new ArrayList<>();

        String sql = "SELECT * FROM pull_request_issue_links WHERE pull_request_id = ?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, pullRequestId);

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    PullRequestIssueLink l = new PullRequestIssueLink();
                    l.setPullRequestId(rs.getLong("pull_request_id"));
                    l.setIssueId(rs.getLong("issue_id"));
                    l.setLinkType(PullRequestIssueLink.LinkType.valueOf(rs.getString("link_type")));
                    links.add(l);
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error fetching pull request issue links", e);
        }
        return links;
    }

    public void createReview(PullRequestReview review) {
        String sql = """
                INSERT INTO pull_request_reviews (pull_request_id, reviewer_user_id, review_comment, review_status)
                VALUES (?, ?, ?, ?)
                """;

        try(PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, review.getPullRequestId());
            stmt.setLong(2, review.getReviewerUserId());
            stmt.setString(3, review.getReviewComment());
            stmt.setString(4, review.getReviewStatus());

            int affectedRows = stmt.executeUpdate();
            if(affectedRows == 0) {
                throw new SQLException("Creating PullRequestReview failed, no rows affected.");
            }

            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) {
                    review.setReviewId(rs.getLong(1));
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error creating PullRequestReview", e);
        }
    }

    public void updateReview(long reviewId, String reviewStatus, String reviewComment) {
        String sql = """
                UPDATE pull_request_reviews
                SET review_status = ?, review_comment = ?, reviewed_at = CURRENT_TIMESTAMP
                WHERE review_id = ?
                """;

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, reviewStatus);
            stmt.setString(2, reviewComment);
            stmt.setLong(3, reviewId);

            int affectedRows = stmt.executeUpdate();
            if(affectedRows == 0) {
                throw new SQLException("Updating PullRequestReview failed, no rows affected.");
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error updating PullRequestReview", e);
        }
    }

    public List<PullRequestReview> getReviewsByPullRequest(long prId) {
        List<PullRequestReview> reviews = new ArrayList<>();

        String sql = "SELECT * FROM pull_request_reviews WHERE pull_request_id = ? ORDER BY reviewed_at DESC";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, prId);

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    reviews.add(mapResultSetToReview(rs));
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error fetching PullRequestReviews", e);
        }

        return reviews;
    }

    public List<PullRequestReview> getReviewsByUser(long userId) {
        List<PullRequestReview> reviews = new ArrayList<>();

        String sql = "SELECT * FROM pull_request_reviews WHERE reviewer_user_id = ? ORDER BY reviewed_at DESC";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    reviews.add(mapResultSetToReview(rs));
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error fetching PullRequestReviews by user", e);
        }

        return reviews;
    }

    private PullRequestReview mapResultSetToReview(ResultSet rs) {
        try {
            PullRequestReview review = new PullRequestReview();

            review.setReviewId(rs.getLong("review_id"));
            review.setPullRequestId(rs.getLong("pull_request_id"));
            review.setReviewerUserId(rs.getLong("reviewer_user_id"));
            review.setReviewComment(rs.getString("review_comment"));
            review.setReviewStatus(rs.getString("review_status"));

            return review;

        } catch(SQLException e) {
            throw new RuntimeException("Error mapping PullRequestReview", e);
        }
    }

    private PullRequest mapResultSetToPullRequest(ResultSet rs) {
        try {
            PullRequest pr = new PullRequest();

            pr.setPullRequestId(rs.getLong("pull_request_id"));
            pr.setRepositoryId(rs.getLong("repository_id"));
            pr.setSourceBranchId(rs.getLong("source_branch_id"));
            pr.setTargetBranchId(rs.getLong("target_branch_id"));
            pr.setCreatedByUserId(rs.getLong("created_by_user_id"));
            pr.setTitle(rs.getString("title"));
            pr.setDescription(rs.getString("description"));
            pr.setStatus(rs.getString("status"));

            return pr;

        } catch(SQLException e) {
            throw new RuntimeException("Error mapping PullRequest", e);
        }
    }
}
