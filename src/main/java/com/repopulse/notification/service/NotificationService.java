package com.repopulse.notification.service;

import com.repopulse.notification.dao.NotificationDAO;
import com.repopulse.notification.model.Notification;
import com.repopulse.notification.validator.NotificationValidator;

import java.sql.Timestamp;
import java.util.List;

public class NotificationService {
    private final NotificationDAO notificationDAO;

    public NotificationService() {
        this.notificationDAO = new NotificationDAO();
    }

    public void sendNotification(long userId, String typeStr, long referenceId) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setNotificationType(NotificationValidator.validateType(typeStr));
        notification.setReferenceId(referenceId);
        notification.setIsRead(false);
        notification.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        this.notificationDAO.createNotification(notification);
    }

    public List<Notification> getUserNotifications(long userId) {
        return this.notificationDAO.getNotificationsByUser(userId);
    }

    public void markNotificationAsRead(long notificationId) {
        this.notificationDAO.markAsRead(notificationId);
    }

    public void markAllNotificationsAsRead(long userId) {
        this.notificationDAO.markAllAsRead(userId);
    }

    public int getUnreadNotificationCount(long userId) {
        return this.notificationDAO.getUnreadCount(userId);
    }

}
