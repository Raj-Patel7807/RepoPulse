package com.repopulse.user.model;

import java.sql.Timestamp;

public class UserBlock {
    private long blockerUserId;
    private long blockedUserId;
    private Timestamp blockedAt;

    public UserBlock() {

    }

    public long getBlockerUserId() {
        return this.blockerUserId;
    }

    public void setBlockerUserId(long blockerUserId) {
        this.blockerUserId = blockerUserId;
    }

    public long getBlockedUserId() {
        return this.blockedUserId;
    }

    public void setBlockedUserId(long blockedUserId) {
        this.blockedUserId = blockedUserId;
    }

    public Timestamp getBlockedAt() {
        return this.blockedAt;
    }

    public void setBlockedAt(Timestamp blockedAt) {
        this.blockedAt = blockedAt;
    }
}
