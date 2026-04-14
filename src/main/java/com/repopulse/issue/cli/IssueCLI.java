package com.repopulse.issue.cli;

import com.repopulse.common.cli.CliUtils;
import com.repopulse.infra.session.Session;
import com.repopulse.issue.model.RepoIssue;
import com.repopulse.issue.service.RepoIssueService;

import java.util.List;

public class IssueCLI {
    private final long repoId;

    private final RepoIssueService issueService = new RepoIssueService();

    public IssueCLI(long repoId) {
        this.repoId = repoId;
    }

    public void start() {
        while(true) {
            System.out.println("\n=== Issues ===");
            System.out.println("1. List Issues");
            System.out.println("2. Create Issue");
            System.out.println("3. Close Issue");
            System.out.println("4. Back");

            int choice = CliUtils.getIntInput("Enter Choice: ");

            if(choice == 1) {
                List<RepoIssue> issues = issueService.getIssuesByRepository(repoId);

                if(issues.isEmpty()) {
                    System.out.println("No Issues Found..!!");
                } else {
                    System.out.println("Issues: ");
                    for(RepoIssue issue : issues) {
                        System.out.println(issue.getIssueId() + " | " + issue.getDescription() + " | " + issue.getStatus());
                    }
                }
            } else if(choice == 2) {
                String title = CliUtils.getStringInput("Enter Issue Title: ");
                String description = CliUtils.getStringInput("Enter Issue Description: ");
                String priority = CliUtils.getStringInput("Enter Priority (LOW, HIGH, MEDIUM): ");

                issueService.createIssue(repoId, Session.getCurrentUser().getUserId(), title, description, priority);

            } else if(choice == 3) {
                long issueId = CliUtils.getLongInput("Enter Issue Id to Close: ");

                issueService.closeIssue(issueId, repoId, Session.getCurrentUser().getUserId());

            } else if(choice == 4) {
                return;
            } else {
                System.out.println("Invalid Choice..!!");
            }
        }
    }
}
