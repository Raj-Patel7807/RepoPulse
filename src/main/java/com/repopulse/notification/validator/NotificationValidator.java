package com.repopulse.notification.validator;

import com.repopulse.notification.model.Notification;

public final class NotificationValidator {
    private NotificationValidator() {
    }

    public static Notification.NotificationType validateType(String type) {
        switch(type.toUpperCase()) {
            case "FOLLOW", "PR", "ISSUE", "STAR", "COMMENT" -> {
            }
            default -> throw new IllegalArgumentException("Invalid notification type: " + type);
        }
        return Notification.NotificationType.valueOf(type.toUpperCase());
    }
}
