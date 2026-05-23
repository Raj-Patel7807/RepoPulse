package com.repopulse.user.model;

import java.sql.Timestamp;

public class UserReport {
    private long reportId;
    private long reportedUserId;
    private long reporterUserId;
    private ReportReason reportReason;
    private String reportDescription;
    private ReportStatus reportStatus;
    private Long reviewedByAdminId; // nullable
    private Timestamp createdAt;
    private Timestamp reviewedAt;

    public UserReport() {

    }

    public long getReportId() {
        return this.reportId;
    }

    public void setReportId(long reportId) {
        this.reportId = reportId;
    }

    public long getReportedUserId() {
        return this.reportedUserId;
    }

    public void setReportedUserId(long reportedUserId) {
        this.reportedUserId = reportedUserId;
    }

    public long getReporterUserId() {
        return this.reporterUserId;
    }

    public void setReporterUserId(long reporterUserId) {
        this.reporterUserId = reporterUserId;
    }

    public ReportReason getReportReason() {
        return this.reportReason;
    }

    public void setReportReason(ReportReason reportReason) {
        this.reportReason = reportReason;
    }

    public String getReportDescription() {
        return this.reportDescription;
    }

    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
    }

    public ReportStatus getReportStatus() {
        return this.reportStatus;
    }

    public void setReportStatus(ReportStatus reportStatus) {
        this.reportStatus = reportStatus;
    }

    public Long getReviewedByAdminId() {
        return this.reviewedByAdminId;
    }

    public void setReviewedByAdminId(Long reviewedByAdminId) {
        this.reviewedByAdminId = reviewedByAdminId;
    }

    public Timestamp getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getReviewedAt() {
        return this.reviewedAt;
    }

    public void setReviewedAt(Timestamp reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public enum ReportReason {
        SPAM, ABUSE, FAKE_ACCOUNT
    }

    public enum ReportStatus {
        OPEN, UNDER_REVIEW, RESOLVED, REJECTED
    }
}
