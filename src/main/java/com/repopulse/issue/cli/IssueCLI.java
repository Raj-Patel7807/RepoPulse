package com.repopulse.issue.cli;

import com.repopulse.infra.exception.AppException;
import com.repopulse.infra.session.Authz;
import com.repopulse.infra.session.Session;
import com.repopulse.infra.util.CliUtils;
import com.repopulse.issue.model.IssueLabel;
import com.repopulse.issue.model.Milestone;
import com.repopulse.issue.model.RepoIssue;
import com.repopulse.issue.service.MilestoneService;
import com.repopulse.issue.service.RepoIssueService;
import com.repopulse.repository.service.RepositoryService;

import java.util.List;

public class IssueCLI {
    private final long repoId;

    private final RepoIssueService issueService = new RepoIssueService();
    private final MilestoneService milestoneService = new MilestoneService();
    private final RepositoryService repositoryService = new RepositoryService();

    public IssueCLI(long repoId) {
        this.repoId = repoId;
    }

    public void start() {
        while(true) {
            boolean loggedIn = Authz.isLoggedIn();
            System.out.println("\n=== Issues ===");
            System.out.println("1. List Issues");
            System.out.println("2. View Issue Details");
            if(loggedIn) {
                System.out.println("3. Create Issue");
                System.out.println("4. Close Issue");
                System.out.println("5. Labels");
                System.out.println("6. Milestones");
                System.out.println("7. Back");
            } else {
                System.out.println("3. Labels");
                System.out.println("4. Milestones");
                System.out.println("5. Back");
            }

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
                long issueId = CliUtils.getLongInput("Enter Issue Id: ");
                RepoIssue issue = issueService.getIssue(issueId);
                if(issue == null || issue.getRepositoryId() != repoId) {
                    System.out.println("Issue not found.");
                } else {
                    System.out.println("ID: " + issue.getIssueId());
                    System.out.println("Title: " + issue.getTitle());
                    System.out.println("Status: " + issue.getStatus());
                    System.out.println("Priority: " + issue.getPriority());
                    System.out.println("Description: " + issue.getDescription());
                    List<Long> labels = issueService.getLabelsOfIssue(issueId);
                    System.out.println("Label IDs: " + labels);
                }
                CliUtils.waitForEnter();
            } else if(loggedIn && choice == 3) {
                Authz.requireLogin("create issue");
                ensureWriteAccess();
                String title = CliUtils.getStringInput("Enter Issue Title: ");
                String description = CliUtils.getStringInput("Enter Issue Description: ");
                String priority = CliUtils.getStringInput("Enter Priority (LOW, HIGH, MEDIUM): ");

                issueService.createIssue(repoId, Session.getCurrentUser().getUserId(), title, description, priority);

            } else if(loggedIn && choice == 4) {
                Authz.requireLogin("close issue");
                ensureWriteAccess();
                long issueId = CliUtils.getLongInput("Enter Issue Id to Close: ");

                issueService.closeIssue(issueId, repoId, Session.getCurrentUser().getUserId());

            } else if((loggedIn && choice == 5) || (!loggedIn && choice == 3)) {
                labelsMenu();
            } else if((loggedIn && choice == 6) || (!loggedIn && choice == 4)) {
                milestonesMenu();
            } else if((loggedIn && choice == 7) || (!loggedIn && choice == 5)) {
                return;
            } else {
                System.out.println("Invalid Choice..!!");
            }
        }
    }

    private void labelsMenu() {
        while(true) {
            boolean loggedIn = Authz.isLoggedIn();
            System.out.println("\n=== Issue Labels ===");
            System.out.println("1. List Labels");
            if(loggedIn) {
                System.out.println("2. Create Label");
                System.out.println("3. Assign Label to Issue");
                System.out.println("4. Back");
            } else {
                System.out.println("2. Back");
            }

            int choice = CliUtils.getIntInput("Enter Choice: ");

            if(choice == 1) {
                List<IssueLabel> labels = issueService.getLabelsForRepo(repoId);
                if(labels.isEmpty()) {
                    System.out.println("No labels found.");
                } else {
                    System.out.println("label_id | name | color");
                    for(IssueLabel l : labels) {
                        System.out.println(l.getLabelId() + " | " + l.getLabelName() + " | " + l.getLabelColor());
                    }
                }
                CliUtils.waitForEnter();
            } else if(loggedIn && choice == 2) {
                Authz.requireLogin("create label");
                ensureWriteAccess();
                String name = CliUtils.getStringInput("Label name: ");
                String color = CliUtils.getStringInput("Label color (#rrggbb): ");
                issueService.createLabel(repoId, name, color);
                System.out.println("Label created.");
                CliUtils.waitForEnter();
            } else if(loggedIn && choice == 3) {
                Authz.requireLogin("assign label");
                ensureWriteAccess();
                long issueId = CliUtils.getLongInput("Issue ID: ");
                long labelId = CliUtils.getLongInput("Label ID: ");
                issueService.assignLabelToIssue(issueId, labelId);
                System.out.println("Label assigned.");
                CliUtils.waitForEnter();
            } else if((loggedIn && choice == 4) || (!loggedIn && choice == 2)) {
                return;
            } else {
                System.out.println("Invalid choice!");
                CliUtils.waitForEnter();
            }
        }
    }

    private void milestonesMenu() {
        while(true) {
            boolean loggedIn = Authz.isLoggedIn();
            System.out.println("\n=== Milestones ===");
            System.out.println("1. List Milestones");
            if(loggedIn) {
                System.out.println("2. Create Milestone");
                System.out.println("3. Close Milestone");
                System.out.println("4. Reopen Milestone");
                System.out.println("5. Assign Milestone to Issue");
                System.out.println("6. Back");
            } else {
                System.out.println("2. Back");
            }

            int choice = CliUtils.getIntInput("Enter Choice: ");

            if(choice == 1) {
                List<Milestone> milestones = milestoneService.getMilestonesByRepo(repoId);
                if(milestones.isEmpty()) {
                    System.out.println("No milestones found.");
                } else {
                    System.out.println("id | title | status | due");
                    for(Milestone m : milestones) {
                        System.out.println(m.getMilestoneId() + " | " + m.getTitle() + " | " + m.getStatus() + " | " + m.getDueDate());
                    }
                }
                CliUtils.waitForEnter();
            } else if(loggedIn && choice == 2) {
                Authz.requireLogin("create milestone");
                ensureWriteAccess();
                String title = CliUtils.getStringInput("Title: ");
                String desc = CliUtils.getStringInput("Description: ");
                String due = CliUtils.getStringInput("Due date (YYYY-MM-DD) or empty: ");
                java.sql.Date dueDate = null;
                if(due != null && !due.trim().isEmpty()) {
                    dueDate = java.sql.Date.valueOf(due.trim());
                }
                milestoneService.createMilestone(repoId, title, desc, dueDate);
                System.out.println("Milestone created.");
                CliUtils.waitForEnter();
            } else if(loggedIn && choice == 3) {
                Authz.requireLogin("close milestone");
                ensureWriteAccess();
                long milestoneId = CliUtils.getLongInput("Milestone ID: ");
                boolean ok = milestoneService.closeMilestone(milestoneId);
                System.out.println(ok ? "Milestone closed." : "Milestone not found.");
                CliUtils.waitForEnter();
            } else if(loggedIn && choice == 4) {
                Authz.requireLogin("reopen milestone");
                ensureWriteAccess();
                long milestoneId = CliUtils.getLongInput("Milestone ID: ");
                boolean ok = milestoneService.reopenMilestone(milestoneId);
                System.out.println(ok ? "Milestone reopened." : "Milestone not found.");
                CliUtils.waitForEnter();
            } else if(loggedIn && choice == 5) {
                Authz.requireLogin("assign milestone");
                ensureWriteAccess();
                long issueId = CliUtils.getLongInput("Issue ID: ");
                long milestoneId = CliUtils.getLongInput("Milestone ID: ");
                boolean ok = issueService.assignMilestoneToIssue(issueId, milestoneId);
                System.out.println(ok ? "Milestone assigned to issue." : "Issue not found.");
                CliUtils.waitForEnter();
            } else if((loggedIn && choice == 6) || (!loggedIn && choice == 2)) {
                return;
            } else {
                System.out.println("Invalid choice!");
                CliUtils.waitForEnter();
            }
        }
    }

    private void ensureWriteAccess() {
        if(Session.getCurrentUser() == null) {
            throw new AppException("Please login first.");
        }
        if(!repositoryService.canWriteRepository(repoId, Session.getCurrentUser().getUserId())) {
            throw new AppException("You do not have write access to this repository.");
        }
    }
}
