package com.repopulse.user.validator;

import com.repopulse.user.model.UserActivityLog;
import com.repopulse.user.model.UserReport;

import java.util.regex.Pattern;

public class UserValidator {

    private UserValidator() {

    }

    public static void validateUsername(String username) {
        if(username == null || username.length() < 3 || username.length() > 20) {
            throw new IllegalArgumentException("Username must be 3-20 characters long");
        }
    }

    public static void validateEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if(email == null || !Pattern.matches(emailRegex, email)) {
            throw new IllegalArgumentException("Invalid email address");
        }
    }

    public static void validatePassword(String password) {
        if(password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
    }

    public static UserReport.ReportReason validateReportReason(String reason) {
        switch(reason.toUpperCase()) {
            case "SPAM", "ABUSE", "FAKE_ACCOUNT" -> {
            }
            default -> throw new IllegalArgumentException("Invalid report reason: " + reason);
        }
        return UserReport.ReportReason.valueOf(reason.toUpperCase());
    }

    public static UserReport.ReportStatus validateReportStatus(String status) {
        switch(status.toUpperCase()) {
            case "OPEN", "UNDER_REVIEW", "RESOLVED", "REJECTED" -> {
            }
            default -> throw new IllegalArgumentException("Invalid report status: " + status);
        }
        return UserReport.ReportStatus.valueOf(status.toUpperCase());
    }

    public static UserActivityLog.ActivityType validateActivityType(String type) {
        switch(type.toUpperCase()) {
            case "CREATE_REPOSITORY", "COMMIT", "MERGE", "FOLLOW" -> {
            }
            default -> throw new IllegalArgumentException("Invalid activity type: " + type);
        }
        return UserActivityLog.ActivityType.valueOf(type.toUpperCase());
    }
}
