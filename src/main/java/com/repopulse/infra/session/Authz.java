package com.repopulse.infra.session;

import com.repopulse.infra.exception.AppException;

public class Authz {
    private Authz() {}

    public static boolean isLoggedIn() {
        return Session.getCurrentUser() != null;
    }

    public static void requireLogin(String action) {
        if(!isLoggedIn()) {
            throw new AppException("Please login first to " + action + ".");
        }
    }
}

