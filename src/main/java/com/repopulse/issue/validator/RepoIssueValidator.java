package com.repopulse.issue.validator;

import com.repopulse.issue.model.RepoIssue;

public final class RepoIssueValidator {
    private RepoIssueValidator() {
    }

    public static RepoIssue.Status validateStatus(String status) {
        switch(status.toUpperCase()) {
            case "OPEN", "CLOSED" -> {
            }
            default -> throw new IllegalArgumentException("Invalid status: " + status);
        }
        return RepoIssue.Status.valueOf(status.toUpperCase());
    }

    public static RepoIssue.Priority validatePriority(String priority) {
        switch(priority.toUpperCase()) {
            case "LOW", "MEDIUM", "HIGH" -> {
            }
            default -> throw new IllegalArgumentException("Invalid priority: " + priority);
        }
        return RepoIssue.Priority.valueOf(priority.toUpperCase());
    }
}
