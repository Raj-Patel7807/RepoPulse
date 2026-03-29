package com.repopulse.dao;

import com.repopulse.database.DBConnection;
import com.repopulse.model.User;

import java.sql.*;

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

    public boolean authenticate(String username, String password) {
        String sql = "SELECT password_hash FROM users WHERE username=? AND is_deleted=FALSE";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                String storedHash = rs.getString("password_hash");
                return storedHash.equals(password);
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public boolean existsByUsername(String username) {
        String sql = "SELECT 1 FROM users WHERE username=? AND is_deleted=FALSE";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            return rs.next();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT 1 FROM users WHERE email=? AND is_deleted=FALSE";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            return rs.next();
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
