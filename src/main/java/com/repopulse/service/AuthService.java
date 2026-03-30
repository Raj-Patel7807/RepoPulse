package com.repopulse.service;

import com.repopulse.model.User;
import com.repopulse.dao.UserDAO;
import com.repopulse.validator.UserValidator;

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

        UserValidator.validateUsername(username);
        UserValidator.validateEmail(email);
        UserValidator.validatePassword(password);

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
}
