package com.repopulse.repository.validator;

import com.repopulse.repository.model.RepoWatcher;

public final class RepositoryValidator {
    private RepositoryValidator() {
    }

    public static RepoWatcher.WatchLevel validateWatchLevel(String level) {
        switch(level.toUpperCase()) {
            case "ALL", "PARTICIPATING", "NONE" -> {
            }
            default -> throw new IllegalArgumentException("Invalid watch level: " + level);
        }
        return RepoWatcher.WatchLevel.valueOf(level.toUpperCase());
    }

    public static void validateRole(String role) {
        switch(role.toUpperCase()) {
            case "OWNER", "MAINTAINER", "WRITE", "READ" -> {
            }
            default -> throw new IllegalArgumentException("Invalid access role: " + role);
        }
    }

    public static void validateRepoName(String name) {
        if(name == null || name.trim().isEmpty() || name.length() > 100) {
            throw new IllegalArgumentException("Repository name must be 1-100 characters");
        }
    }

    public static void validateVisibilityType(String visibilityType) {
        if(!"PUBLIC".equalsIgnoreCase(visibilityType) && !"PRIVATE".equalsIgnoreCase(visibilityType)) {
            throw new IllegalArgumentException("Visibility type must be PUBLIC or PRIVATE");
        }
    }
}
