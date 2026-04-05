package com.repopulse.user.service;

import com.repopulse.user.dao.UserDAO;
import com.repopulse.user.model.*;
import com.repopulse.user.validator.UserValidator;

import java.sql.Timestamp;
import java.util.List;

public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public User getUserById(long userId) {
        return userDAO.getUserByUserId(userId);
    }

    public User getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    public void updateUserProfile(long userId, String username, String email, String bio, String avatarUrl) {
        UserValidator.validateUsername(username);
        UserValidator.validateEmail(email);

        User user = getUserById(userId);
        if(user == null) {
            throw new IllegalArgumentException("User not found");
        }

        user.setUsername(username);
        user.setEmail(email);
        user.setProfileBio(bio);
        user.setProfileAvatarUrl(avatarUrl);

        userDAO.updateUser(user);
    }

    public void changePassword(long userId, String newPassword) {
        UserValidator.validatePassword(newPassword);
        userDAO.updatePassword(userId, newPassword);
    }

    public void changeEmail(long userId, String newEmail) {
        UserValidator.validateEmail(newEmail);
        userDAO.updateEmail(userId, newEmail);
    }

    public void updateProfileBioAndAvatar(long userId, String bio, String avatarUrl) {
        userDAO.updateProfile(userId, bio, avatarUrl);
    }

    public void deleteUser(long userId) {
        userDAO.softDeleteUser(userId);
    }

    public int getFollowersCount(long userId) {
        return userDAO.getFollowersCount(userId);
    }

    public int getFollowingCount(long userId) {
        return userDAO.getFollowingCount(userId);
    }

    public int getStarredReposCount(long userId) {
        return userDAO.getStarredReposCount(userId);
    }

    public void reportUser(long reportedUserId, long reporterUserId, String reasonStr, String description) {
        UserReport report = new UserReport();
        report.setReportedUserId(reportedUserId);
        report.setReporterUserId(reporterUserId);
        report.setReportReason(validateReason(reasonStr));
        report.setReportDescription(description);
        report.setReportStatus(UserReport.ReportStatus.OPEN);
        report.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        userDAO.createReport(report);
    }

    public List<UserReport> getReportsByStatus(String statusStr) {
        return userDAO.getReportsByStatus(validateStatus(statusStr));
    }

    private UserReport.ReportReason validateReason(String reason) {
        switch(reason.toUpperCase()) {
            case "SPAM", "ABUSE", "FAKE_ACCOUNT" -> {}
            default -> throw new IllegalArgumentException("Invalid report reason: " + reason);
        }
        return UserReport.ReportReason.valueOf(reason.toUpperCase());
    }

    private UserReport.ReportStatus validateStatus(String status) {
        switch(status.toUpperCase()) {
            case "OPEN", "UNDER_REVIEW", "RESOLVED", "REJECTED" -> {}
            default -> throw new IllegalArgumentException("Invalid report status: " + status);
        }
        return UserReport.ReportStatus.valueOf(status.toUpperCase());
    }

    public void pinRepo(long userId, long repositoryId) {
        UserPinnedRepo pinned = new UserPinnedRepo();
        pinned.setUserId(userId);
        pinned.setRepositoryId(repositoryId);
        pinned.setPinnedAt(new Timestamp(System.currentTimeMillis()));
        userDAO.pinRepo(pinned);
    }

    public List<Long> getPinnedRepos(long userId) {
        return userDAO.getPinnedRepos(userId);
    }

    public void followUser(long followerId, long followingId) {
        UserFollow uf = new UserFollow();
        uf.setFollowerUserId(followerId);
        uf.setFollowingUserId(followingId);
        uf.setFollowedAt(new Timestamp(System.currentTimeMillis()));
        userDAO.followUser(uf);
    }

    public void unfollowUser(long followerId, long followingId) {
        userDAO.unfollowUser(followerId, followingId);
    }

    public List<UserFollow> getFollowers(long userId) {
        return userDAO.getFollowers(userId);
    }

    public void blockUser(long blockerUserId, long blockedUserId) {
        UserBlock block = new UserBlock();
        block.setBlockerUserId(blockerUserId);
        block.setBlockedUserId(blockedUserId);
        block.setBlockedAt(new Timestamp(System.currentTimeMillis()));
        userDAO.blockUser(block);
    }

    public List<Long> getBlockedUsers(long blockerUserId) {
        return userDAO.getBlockedUsers(blockerUserId);
    }

    public void logActivity(long userId, String typeStr, long referenceId, String metadataJson) {
        UserActivityLog log = new UserActivityLog();
        log.setUserId(userId);
        log.setActivityType(validateType(typeStr));
        log.setReferenceId(referenceId);
        log.setActivityMetadata(metadataJson);
        log.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        userDAO.createActivityLog(log);
    }

    public List<UserActivityLog> getUserActivityLogs(long userId) {
        return userDAO.getActivityLogsByUser(userId);
    }

    private UserActivityLog.ActivityType validateType(String type) {
        switch(type.toUpperCase()) {
            case "CREATE_REPOSITORY", "COMMIT", "MERGE", "FOLLOW" -> {}
            default -> throw new IllegalArgumentException("Invalid activity type: " + type);
        }
        return UserActivityLog.ActivityType.valueOf(type.toUpperCase());
    }
}
