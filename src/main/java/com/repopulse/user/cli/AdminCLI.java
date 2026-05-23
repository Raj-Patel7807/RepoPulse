package com.repopulse.user.cli;

import com.repopulse.infra.session.Session;
import com.repopulse.infra.util.CliUtils;
import com.repopulse.user.model.UserReport;
import com.repopulse.user.service.UserService;

import java.util.List;

public class AdminCLI {
    private final UserService userService = new UserService();

    public void start() {
        while(true) {
            System.out.println("\n=== Admin Panel ===");
            System.out.println("1. List Reports by Status");
            System.out.println("2. List All Reports");
            System.out.println("3. Review Report");
            System.out.println("4. Block User");
            System.out.println("5. Unblock User");
            System.out.println("6. Logout");

            int choice = CliUtils.getIntInput("Enter Choice: ");

            if(choice == 1) {
                String status = CliUtils.getStringInput("Status (OPEN/UNDER_REVIEW/RESOLVED/REJECTED): ");
                List<UserReport> reports = userService.getReportsByStatus(status);
                printReports(reports);
            } else if(choice == 2) {
                printReports(userService.getAllReports());
            } else if(choice == 3) {
                long reportId = CliUtils.getLongInput("Report ID: ");
                String status = CliUtils.getStringInput("New Status (UNDER_REVIEW/RESOLVED/REJECTED): ");
                long adminId = Session.getCurrentUser().getUserId();
                boolean ok = userService.reviewReport(reportId, adminId, status);
                System.out.println(ok ? "Report reviewed." : "Report not found.");
                CliUtils.waitForEnter();
            } else if(choice == 4) {
                long blockerId = CliUtils.getLongInput("Blocker User ID: ");
                long blockedId = CliUtils.getLongInput("Blocked User ID: ");
                userService.blockUser(blockerId, blockedId);
                System.out.println("User blocked.");
                CliUtils.waitForEnter();
            } else if(choice == 5) {
                long blockerId = CliUtils.getLongInput("Blocker User ID: ");
                long blockedId = CliUtils.getLongInput("Blocked User ID: ");
                userService.unblockUser(blockerId, blockedId);
                System.out.println("User unblocked.");
                CliUtils.waitForEnter();
            } else if(choice == 6) {
                Session.logout();
                return;
            } else {
                System.out.println("Invalid choice.");
                CliUtils.waitForEnter();
            }
        }
    }

    private void printReports(List<UserReport> reports) {
        if(reports.isEmpty()) {
            System.out.println("No reports found.");
        } else {
            System.out.println("id | reported | reporter | reason | status | reviewed_by");
            for(UserReport report : reports) {
                System.out.println(report.getReportId() + " | " + report.getReportedUserId() + " | " + report.getReporterUserId() + " | " + report.getReportReason() + " | " + report.getReportStatus() + " | " + report.getReviewedByAdminId());
            }
        }
        CliUtils.waitForEnter();
    }
}
