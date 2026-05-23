package com.repopulse.discussion.cli;

import com.repopulse.discussion.model.DiscussionComment;
import com.repopulse.discussion.service.DiscussionCommentService;
import com.repopulse.infra.exception.AppException;
import com.repopulse.infra.session.Authz;
import com.repopulse.infra.session.Session;
import com.repopulse.infra.util.CliUtils;
import com.repopulse.repository.service.RepositoryService;

import java.sql.Timestamp;
import java.util.List;

public class DiscussionCommentCLI {
    private final long repoId;
    private final DiscussionCommentService discussionCommentService = new DiscussionCommentService();
    private final RepositoryService repositoryService = new RepositoryService();

    public DiscussionCommentCLI(long repoId) {
        this.repoId = repoId;
    }

    public void start() {
        while(true) {
            boolean loggedIn = Authz.isLoggedIn();
            System.out.println("\n=== Discussion Comments ===");
            System.out.println("1. List Comments by Target");
            System.out.println("2. List Replies of Comment");
            if(loggedIn) {
                System.out.println("3. Add Comment");
                System.out.println("4. Reply to Comment");
                System.out.println("5. Back");
            } else {
                System.out.println("3. Back");
            }

            int choice = CliUtils.getIntInput("Enter Choice: ");
            if(choice == 1) {
                listByTarget();
            } else if(choice == 2) {
                listReplies();
            } else if(loggedIn && choice == 3) {
                addComment();
            } else if(loggedIn && choice == 4) {
                replyComment();
            } else if((loggedIn && choice == 5) || (!loggedIn && choice == 3)) {
                return;
            } else {
                System.out.println("Invalid choice!");
                CliUtils.waitForEnter();
            }
        }
    }

    private void listByTarget() {
        TargetInput target = readTarget();
        List<DiscussionComment> comments = discussionCommentService.getCommentsByTarget(target.commitId, target.pullRequestId, target.issueId);
        if(comments.isEmpty()) {
            System.out.println("No comments found.");
        } else {
            System.out.println("id | user | parent | body");
            for(DiscussionComment c : comments) {
                System.out.println(c.getCommentId() + " | " + c.getUserId() + " | " + c.getParentCommentId() + " | " + c.getCommentBody());
            }
        }
        CliUtils.waitForEnter();
    }

    private void listReplies() {
        long parentId = CliUtils.getLongInput("Parent Comment ID: ");
        List<DiscussionComment> replies = discussionCommentService.getChildComments(parentId);
        if(replies.isEmpty()) {
            System.out.println("No replies found.");
        } else {
            for(DiscussionComment c : replies) {
                System.out.println(c.getCommentId() + " | " + c.getUserId() + " | " + c.getCommentBody());
            }
        }
        CliUtils.waitForEnter();
    }

    private void addComment() {
        Authz.requireLogin("add discussion comment");
        ensureWriteAccess();
        TargetInput target = readTarget();
        String body = CliUtils.getStringInput("Comment: ");
        long userId = Session.getCurrentUser().getUserId();

        discussionCommentService.createComment(userId, target.commitId, target.pullRequestId, target.issueId, null, body, null, null, new Timestamp(System.currentTimeMillis()));
        System.out.println("Comment added.");
        CliUtils.waitForEnter();
    }

    private void replyComment() {
        Authz.requireLogin("reply discussion comment");
        ensureWriteAccess();
        long parentCommentId = CliUtils.getLongInput("Parent Comment ID: ");
        DiscussionComment parent = discussionCommentService.getComment(parentCommentId);
        if(parent == null) {
            System.out.println("Parent comment not found.");
            CliUtils.waitForEnter();
            return;
        }

        String body = CliUtils.getStringInput("Reply: ");
        long userId = Session.getCurrentUser().getUserId();
        discussionCommentService.createComment(userId, parent.getCommitId(), parent.getPullRequestId(), parent.getIssueId(), parentCommentId, body, null, null, new Timestamp(System.currentTimeMillis()));
        System.out.println("Reply added.");
        CliUtils.waitForEnter();
    }

    private TargetInput readTarget() {
        System.out.println("Target Type:");
        System.out.println("1. Commit");
        System.out.println("2. Pull Request");
        System.out.println("3. Issue");
        int type = CliUtils.getIntInput("Choose: ");

        TargetInput input = new TargetInput();
        if(type == 1) {
            input.commitId = CliUtils.getLongInput("Commit ID: ");
        } else if(type == 2) {
            input.pullRequestId = CliUtils.getLongInput("Pull Request ID: ");
        } else if(type == 3) {
            input.issueId = CliUtils.getLongInput("Issue ID: ");
        } else {
            throw new IllegalArgumentException("Invalid target type.");
        }
        return input;
    }

    private void ensureWriteAccess() {
        if(Session.getCurrentUser() == null) {
            throw new AppException("Please login first.");
        }
        if(!repositoryService.canWriteRepository(repoId, Session.getCurrentUser().getUserId())) {
            throw new AppException("You do not have write access to this repository.");
        }
    }

    private static class TargetInput {
        Long commitId;
        Long pullRequestId;
        Long issueId;
    }
}
