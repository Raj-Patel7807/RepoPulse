package com.repopulse.user.model;

import java.sql.Timestamp;

public class UserActivityLog {
    private long activityId;
    private long userId;
    private ActivityType activityType;
    private long referenceId;
    private String activityMetadata; // JSON stored as String
    private Timestamp createdAt;

    public UserActivityLog() {
    }

    public long getActivityId() {
        return this.activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public ActivityType getActivityType() {
        return this.activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public long getReferenceId() {
        return this.referenceId;
    }

    public void setReferenceId(long referenceId) {
        this.referenceId = referenceId;
    }

    public String getActivityMetadata() {
        return this.activityMetadata;
    }

    public void setActivityMetadata(String activityMetadata) {
        this.activityMetadata = activityMetadata;
    }

    public Timestamp getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public enum ActivityType {
        CREATE_REPOSITORY, COMMIT, MERGE, FOLLOW
    }
}
