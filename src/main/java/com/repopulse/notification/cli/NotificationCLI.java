package com.repopulse.notification.cli;

import com.repopulse.infra.session.Authz;
import com.repopulse.infra.util.CliUtils;
import com.repopulse.infra.session.Session;
import com.repopulse.notification.model.Notification;
import com.repopulse.notification.service.NotificationService;

import java.util.List;

public class NotificationCLI {
    private final NotificationService notificationService = new NotificationService();

    public void start() {
        Authz.requireLogin("view notifications");
        while(true) {
            long userId = Session.getCurrentUser().getUserId();
            int unread = notificationService.getUnreadNotificationCount(userId);
            System.out.println("\n=== Notifications ===");
            System.out.println("Unread: " + unread);
            System.out.println("1. List Notifications");
            System.out.println("2. Mark as Read");
            System.out.println("3. Mark All as Read");
            System.out.println("4. Back");

            int choice = CliUtils.getIntInput("Enter Choice: ");

            if(choice == 1) {
                listNotifications();
            } else if(choice == 2) {
                long id = CliUtils.getLongInput("Enter Notification ID: ");
                notificationService.markNotificationAsRead(id);
                System.out.println("Marked as read.");
                CliUtils.waitForEnter();
            } else if(choice == 3) {
                notificationService.markAllNotificationsAsRead(userId);
                System.out.println("All notifications marked as read.");
                CliUtils.waitForEnter();
            } else if(choice == 4) {
                return;
            } else {
                System.out.println("Invalid Choice!");
                CliUtils.waitForEnter();
            }
        }
    }

    private void listNotifications() {
        long userId = Session.getCurrentUser().getUserId();
        List<Notification> notifications = notificationService.getUserNotifications(userId);

        if(notifications.isEmpty()) {
            System.out.println("No notifications.");
            CliUtils.waitForEnter();
            return;
        }

        System.out.println("ID | TYPE | REF | READ | DATE");
        for(Notification n : notifications) {
            System.out.println(
                    n.getNotificationId() + " | " +
                    n.getNotificationType() + " | " +
                    n.getReferenceId() + " | " +
                    (n.getIsRead() ? "YES" : "NO") + " | " +
                    n.getCreatedAt()
            );
        }
        CliUtils.waitForEnter();
    }
}
