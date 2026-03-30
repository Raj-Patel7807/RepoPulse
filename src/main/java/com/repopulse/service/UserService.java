package com.repopulse.service;

import com.repopulse.dao.UserDAO;
import com.repopulse.model.User;
import com.repopulse.validator.UserValidator;

import java.util.regex.Pattern;

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

    public boolean authenticate(String username, String password) {
        return userDAO.authenticate(username, password);
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
}
