package com.repopulse.infra.session;

import com.repopulse.user.model.User;

public class Session {

    private static User currentUser;
    private static boolean adminSession = false;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setAdminSession(boolean admin) {
        adminSession = admin;
    }

    public static boolean isAdminSession() {
        return adminSession;
    }

    public static void logout() {
        currentUser = null;
        adminSession = false;
    }
}