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

    public User getVisibleUserByUsername(long requesterUserId, String username) {
        User user = userDAO.getUserByUsername(username);
        if(user == null) {
            return null;
        }
        if(user.getUserId() != requesterUserId && userDAO.hasBlockRelationship(requesterUserId, user.getUserId())) {
            return null;
        }
        return user;
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
        report.setReportReason(UserValidator.validateReportReason(reasonStr));
        report.setReportDescription(description);
        report.setReportStatus(UserReport.ReportStatus.OPEN);
        report.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        userDAO.createReport(report);
    }

    public List<UserReport> getReportsByStatus(String statusStr) {
        return userDAO.getReportsByStatus(UserValidator.validateReportStatus(statusStr));
    }

    public List<UserReport> getAllReports() {
        return userDAO.getAllReports();
    }

    public boolean reviewReport(long reportId, long adminUserId, String statusStr) {
        return userDAO.reviewReport(reportId, adminUserId, UserValidator.validateReportStatus(statusStr));
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

    public void unblockUser(long blockerUserId, long blockedUserId) {
        userDAO.unblockUser(blockerUserId, blockedUserId);
    }

    public List<Long> getBlockedUsers(long blockerUserId) {
        return userDAO.getBlockedUsers(blockerUserId);
    }

    public boolean hasBlockRelationship(long userA, long userB) {
        return userDAO.hasBlockRelationship(userA, userB);
    }

    public void logActivity(long userId, String typeStr, long referenceId, String metadataJson) {
        UserActivityLog log = new UserActivityLog();
        log.setUserId(userId);
        log.setActivityType(UserValidator.validateActivityType(typeStr));
        log.setReferenceId(referenceId);
        log.setActivityMetadata(metadataJson);
        log.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        userDAO.createActivityLog(log);
    }

    public List<UserActivityLog> getUserActivityLogs(long userId) {
        return userDAO.getActivityLogsByUser(userId);
    }
}
