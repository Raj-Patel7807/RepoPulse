package com.repopulse.pullrequest.validator;

import com.repopulse.pullrequest.model.PullRequestIssueLink;

public final class PullRequestValidator {
    private PullRequestValidator() {
    }

    public static void validateReviewStatus(String status) {
        switch(status.toUpperCase()) {
            case "APPROVED", "CHANGES_REQUESTED", "COMMENTED" -> {
            }
            default -> throw new IllegalArgumentException("Invalid review status: " + status);
        }
    }

    public static PullRequestIssueLink.LinkType validateLinkType(String linkTypeStr) {
        switch(linkTypeStr.toUpperCase()) {
            case "CLOSES", "REFERENCES" -> {
            }
            default -> throw new IllegalArgumentException("Invalid link type: " + linkTypeStr);
        }
        return PullRequestIssueLink.LinkType.valueOf(linkTypeStr.toUpperCase());
    }

    public static void validateStatus(String status) {
        switch(status.toUpperCase()) {
            case "OPEN", "CLOSED", "MERGED" -> {
            }
            default -> throw new IllegalArgumentException("Invalid pull request status: " + status);
        }
    }
}
