package com.repopulse.user.dao;

import com.repopulse.infra.database.DBConnection;
import com.repopulse.user.model.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private final Connection conn;

    public UserDAO() {
        this.conn = DBConnection.getConnection();
    }

    public void createUser(User user) {
        String sql = "INSERT INTO users (username, email, password_hash, profile_bio, profile_avatar_url) VALUES (?, ?, ?, ?, ?)";

        try(PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getProfileBio());
            stmt.setString(5, user.getProfileAvatarUrl());

            int affectedRows = stmt.executeUpdate();
            if(affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) {
                    user.setUserId(rs.getLong(1));
                }
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ? AND is_deleted = FALSE";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public User getUserByUserId(long userId) {
        String sql = "SELECT * FROM users WHERE user_id = ? AND is_deleted = FALSE";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void updateUser(User user) {
        String sql = "UPDATE users SET username=?, email=?, password_hash=?, profile_bio=?, profile_avatar_url=?, updated_at=CURRENT_TIMESTAMP WHERE user_id=? AND is_deleted=FALSE";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getProfileBio());
            stmt.setString(5, user.getProfileAvatarUrl());
            stmt.setLong(6, user.getUserId());

            stmt.executeUpdate();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updatePassword(long userId, String newPassword) {
        String sql = "UPDATE users SET password_hash=?, updated_at=CURRENT_TIMESTAMP WHERE user_id=? AND is_deleted=FALSE";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setLong(2, userId);
            stmt.executeUpdate();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateEmail(long userId, String newEmail) {
        String sql = "UPDATE users SET email=?, updated_at=CURRENT_TIMESTAMP WHERE user_id=? AND is_deleted=FALSE";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newEmail);
            stmt.setLong(2, userId);
            stmt.executeUpdate();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateProfile(long userId, String bio, String avatarUrl) {
        String sql = "UPDATE users SET profile_bio=?, profile_avatar_url=?, updated_at=CURRENT_TIMESTAMP WHERE user_id=? AND is_deleted=FALSE";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, bio);
            stmt.setString(2, avatarUrl);
            stmt.setLong(3, userId);

            stmt.executeUpdate();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void softDeleteUser(long userId) {
        String sql = "UPDATE users SET is_deleted=TRUE, deleted_at=CURRENT_TIMESTAMP WHERE user_id=?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.executeUpdate();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getFollowersCount(long userId) {
        String sql = "SELECT COUNT(*) AS cnt FROM user_follows WHERE following_user_id=?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                return rs.getInt("cnt");
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public int getFollowingCount(long userId) {
        String sql = "SELECT COUNT(*) AS cnt FROM user_follows WHERE follower_user_id=?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                return rs.getInt("cnt");
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public int getStarredReposCount(long userId) {
        String sql = "SELECT COUNT(*) AS cnt FROM repo_stars WHERE user_id=?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                return rs.getInt("cnt");
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public void createReport(UserReport report) {
        String sql = """
            INSERT INTO user_reports (reported_user_id, reporter_user_id, report_reason, report_description, report_status, reviewed_by_admin_id, created_at, reviewed_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try(PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, report.getReportedUserId());
            stmt.setLong(2, report.getReporterUserId());
            stmt.setString(3, report.getReportReason().name());
            stmt.setString(4, report.getReportDescription());
            stmt.setString(5, report.getReportStatus().name());
            if(report.getReviewedByAdminId() != null) stmt.setLong(6, report.getReviewedByAdminId());
            else stmt.setNull(6, Types.BIGINT);
            stmt.setTimestamp(7, report.getCreatedAt());
            if(report.getReviewedAt() != null) stmt.setTimestamp(8, report.getReviewedAt());
            else stmt.setNull(8, Types.TIMESTAMP);

            int affectedRows = stmt.executeUpdate();
            if(affectedRows == 0) throw new SQLException("Creating report failed.");

            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) report.setReportId(rs.getLong(1));
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error creating report", e);
        }
    }

    public List<UserReport> getReportsByStatus(UserReport.ReportStatus status) {
        List<UserReport> reports = new ArrayList<>();

        String sql = "SELECT * FROM user_reports WHERE report_status = ?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.name());

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    UserReport report = new UserReport();
                    report.setReportId(rs.getLong("report_id"));
                    report.setReportedUserId(rs.getLong("reported_user_id"));
                    report.setReporterUserId(rs.getLong("reporter_user_id"));
                    report.setReportReason(UserReport.ReportReason.valueOf(rs.getString("report_reason")));
                    report.setReportDescription(rs.getString("report_description"));
                    report.setReportStatus(UserReport.ReportStatus.valueOf(rs.getString("report_status")));
                    long adminId = rs.getLong("reviewed_by_admin_id");
                    report.setReviewedByAdminId(rs.wasNull() ? null : adminId);
                    report.setCreatedAt(rs.getTimestamp("created_at"));
                    report.setReviewedAt(rs.getTimestamp("reviewed_at"));
                    reports.add(report);
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error fetching reports", e);
        }
        return reports;
    }

    public void pinRepo(UserPinnedRepo pinned) {
        String sql = "INSERT INTO user_pinned_repos (user_id, repository_id, pinned_at) VALUES (?, ?, ?)";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, pinned.getUserId());
            stmt.setLong(2, pinned.getRepositoryId());
            stmt.setTimestamp(3, pinned.getPinnedAt());
            stmt.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Error pinning repository", e);
        }
    }

    public List<Long> getPinnedRepos(long userId) {
        List<Long> repos = new ArrayList<>();

        String sql = "SELECT repository_id FROM user_pinned_repos WHERE user_id = ?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) repos.add(rs.getLong("repository_id"));
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error fetching pinned repos", e);
        }
        return repos;
    }

    public void followUser(UserFollow follow) {
        String sql = "INSERT INTO user_follows (follower_user_id, following_user_id, followed_at) VALUES (?, ?, ?)";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, follow.getFollowerUserId());
            stmt.setLong(2, follow.getFollowingUserId());
            stmt.setTimestamp(3, follow.getFollowedAt());
            stmt.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Error following user", e);
        }
    }

    public void unfollowUser(long followerId, long followingId) {
        String sql = "DELETE FROM user_follows WHERE follower_user_id = ? AND following_user_id = ?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, followerId);
            stmt.setLong(2, followingId);
            stmt.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Error unfollowing user", e);
        }
    }

    public List<UserFollow> getFollowers(long userId) {
        List<UserFollow> followers = new ArrayList<>();

        String sql = "SELECT * FROM user_follows WHERE following_user_id = ?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    UserFollow uf = new UserFollow();
                    uf.setFollowerUserId(rs.getLong("follower_user_id"));
                    uf.setFollowingUserId(rs.getLong("following_user_id"));
                    uf.setFollowedAt(rs.getTimestamp("followed_at"));
                    followers.add(uf);
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error fetching followers", e);
        }
        return followers;
    }

    public void blockUser(UserBlock block) {
        String sql = "INSERT INTO user_blocks (blocker_user_id, blocked_user_id, blocked_at) VALUES (?, ?, ?)";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, block.getBlockerUserId());
            stmt.setLong(2, block.getBlockedUserId());
            stmt.setTimestamp(3, block.getBlockedAt());
            stmt.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Error blocking user", e);
        }
    }

    public List<Long> getBlockedUsers(long blockerId) {
        List<Long> blocked = new ArrayList<>();

        String sql = "SELECT blocked_user_id FROM user_blocks WHERE blocker_user_id = ?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, blockerId);
            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) blocked.add(rs.getLong("blocked_user_id"));
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error fetching blocked users", e);
        }
        return blocked;
    }

    public void createActivityLog(UserActivityLog log) {
        String sql = "INSERT INTO user_activity_logs (user_id, activity_type, reference_id, activity_metadata, created_at) VALUES (?, ?, ?, ?, ?)";

        try(PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, log.getUserId());
            stmt.setString(2, log.getActivityType().name());
            stmt.setLong(3, log.getReferenceId());
            stmt.setString(4, log.getActivityMetadata());
            stmt.setTimestamp(5, log.getCreatedAt());

            int affectedRows = stmt.executeUpdate();
            if(affectedRows == 0) throw new SQLException("Creating activity log failed.");

            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) log.setActivityId(rs.getLong(1));
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error creating activity log", e);
        }
    }

    public List<UserActivityLog> getActivityLogsByUser(long userId) {
        List<UserActivityLog> logs = new ArrayList<>();

        String sql = "SELECT * FROM user_activity_logs WHERE user_id = ? ORDER BY created_at DESC";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    UserActivityLog log = new UserActivityLog();
                    log.setActivityId(rs.getLong("activity_id"));
                    log.setUserId(rs.getLong("user_id"));
                    log.setActivityType(UserActivityLog.ActivityType.valueOf(rs.getString("activity_type")));
                    log.setReferenceId(rs.getLong("reference_id"));
                    log.setActivityMetadata(rs.getString("activity_metadata"));
                    log.setCreatedAt(rs.getTimestamp("created_at"));
                    logs.add(log);
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error fetching activity logs", e);
        }
        return logs;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User(
                rs.getLong("user_id"),
                rs.getString("username"),
                rs.getString("password_hash"),
                rs.getString("email")
        );
        user.setProfileBio(rs.getString("profile_bio"));
        user.setProfileAvatarUrl(rs.getString("profile_avatar_url"));

        return user;
    }
}
