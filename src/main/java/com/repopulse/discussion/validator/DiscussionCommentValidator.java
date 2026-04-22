package com.repopulse.discussion.validator;

public class DiscussionCommentValidator {
    private DiscussionCommentValidator() {}

    public static void validateBody(String commentBody) {
        if(commentBody == null || commentBody.trim().isEmpty()) {
            throw new IllegalArgumentException("Comment body cannot be empty");
        }
        if(commentBody.length() > 5000) {
            throw new IllegalArgumentException("Comment body is too long");
        }
    }
}
