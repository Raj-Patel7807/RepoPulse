package com.repopulse.service;

import com.repopulse.model.User;
import com.repopulse.dao.UserDAO;

import java.util.regex.Pattern;

public class AuthService {

    private UserDAO userDAO = new UserDAO();

    public User login(String username, String password) {
        User user = userDAO.getUserByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public void signup(User user) {
        String username = user.getUsername();
        String email = user.getEmail();
        String password = user.getPassword();

        validateUsername(username);
        validateEmail(email);
        validatePassword(password);

        if (userDAO.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userDAO.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }

//        user.setProfileBio(bio);
//        user.setProfileAvatarUrl(avatarUrl);

        userDAO.createUser(user);
    }

    private void validateUsername(String username) {
        if(username == null || username.length() < 3 || username.length() > 20) {
            throw new IllegalArgumentException("Username must be 3-20 characters long");
        }
    }

    private void validateEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if(email == null || !Pattern.matches(emailRegex, email)) {
            throw new IllegalArgumentException("Invalid email address");
        }
    }

    private void validatePassword(String password) {
        if(password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
    }
}
