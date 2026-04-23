package com.repopulse.file.validator;

public final class RepoFileValidator {
    private RepoFileValidator() {
    }

    public static void validateDiffFormat(String diffFormat) {
        if(!diffFormat.equalsIgnoreCase("UNIFIED") && !diffFormat.equalsIgnoreCase("CONTEXT")) {
            throw new IllegalArgumentException("Invalid diff format: " + diffFormat);
        }
    }

    public static void validateChangeType(String changeType) {
        switch(changeType.toUpperCase()) {
            case "ADDED", "MODIFIED", "DELETED", "RENAMED" -> {
            }
            default -> throw new IllegalArgumentException("Invalid change type: " + changeType);
        }
    }
}
