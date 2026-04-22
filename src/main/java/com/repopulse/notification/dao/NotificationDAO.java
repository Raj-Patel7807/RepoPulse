package com.repopulse.notification.dao;

import com.repopulse.infra.database.DBConnection;
import com.repopulse.notification.model.Notification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {
    private final Connection conn;

    public NotificationDAO() {
        this.conn = DBConnection.getConnection();
    }

    public void createNotification(Notification notification) {
        String sql = "INSERT INTO notifications (user_id, notification_type, reference_id, is_read, created_at) VALUES (?, ?, ?, ?, ?)";

        try(PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, notification.getUserId());
            stmt.setString(2, notification.getNotificationType().name());
            stmt.setLong(3, notification.getReferenceId());
            stmt.setBoolean(4, notification.getIsRead());
            stmt.setTimestamp(5, notification.getCreatedAt());

            int affectedRows = stmt.executeUpdate();
            if(affectedRows == 0) {
                throw new SQLException("Creating notification failed, no rows affected.");
            }

            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) {
                    notification.setNotificationId(rs.getLong(1));
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error creating notification", e);
        }
    }

    public List<Notification> getNotificationsByUser(long userId) {
        List<Notification> notifications = new ArrayList<>();

        String sql = "SELECT * FROM notifications WHERE user_id = ? ORDER BY created_at DESC";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    Notification n = new Notification();
                    n.setNotificationId(rs.getLong("notification_id"));
                    n.setUserId(rs.getLong("user_id"));
                    n.setNotificationType(Notification.NotificationType.valueOf(rs.getString("notification_type")));
                    n.setReferenceId(rs.getLong("reference_id"));
                    n.setIsRead(rs.getBoolean("is_read"));
                    n.setCreatedAt(rs.getTimestamp("created_at"));
                    notifications.add(n);
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error fetching notifications", e);
        }
        return notifications;
    }

    public void markAsRead(long notificationId) {
        String sql = "UPDATE notifications SET is_read = TRUE WHERE notification_id = ?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, notificationId);
            stmt.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Error marking notification as read", e);
        }
    }

    public void markAllAsRead(long userId) {
        String sql = "UPDATE notifications SET is_read = TRUE WHERE user_id = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Error marking all notifications as read", e);
        }
    }

    public int getUnreadCount(long userId) {
        String sql = "SELECT COUNT(*) AS cnt FROM notifications WHERE user_id = ? AND is_read = FALSE";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    return rs.getInt("cnt");
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error fetching unread notification count", e);
        }
        return 0;
    }
}
