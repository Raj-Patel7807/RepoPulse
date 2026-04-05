package com.repopulse.user.model;

import java.sql.Timestamp;

public class UserFollow {
    private long followerUserId;
    private long followingUserId;
    private Timestamp followedAt;

    public UserFollow() {
        
    }

    public long getFollowerUserId() {
        return this.followerUserId;
    }
    public void setFollowerUserId(long followerUserId) {
        this.followerUserId = followerUserId;
    }

    public long getFollowingUserId() {
        return this.followingUserId;
    }
    public void setFollowingUserId(long followingUserId) {
        this.followingUserId = followingUserId;
    }

    public Timestamp getFollowedAt() {
        return this.followedAt;
    }
    public void setFollowedAt(Timestamp followedAt) {
        this.followedAt = followedAt;
    }
}
