package com.repopulse.notification.model;

import java.sql.Timestamp;

public class Notification {
    private long notificationId;
    private long userId;
    private NotificationType notificationType;
    private long referenceId;
    private boolean isRead;
    private Timestamp createdAt;

    public Notification() {

    }

    public long getNotificationId() {
        return this.notificationId;
    }

    public void setNotificationId(long notificationId) {
        this.notificationId = notificationId;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public NotificationType getNotificationType() {
        return this.notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public long getReferenceId() {
        return this.referenceId;
    }

    public void setReferenceId(long referenceId) {
        this.referenceId = referenceId;
    }

    public boolean getIsRead() {
        return this.isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public Timestamp getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public enum NotificationType {
        FOLLOW, PR, ISSUE, STAR, COMMENT
    }
}
