package com.repopulse.issue.validator;

public class MilestoneValidator {
    private MilestoneValidator() {
    }

    public static void validateTitle(String title) {
        if(title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Milestone title cannot be empty");
        }
        if(title.length() > 255) {
            throw new IllegalArgumentException("Milestone title is too long");
        }
    }
}
