package com.repopulse.auth.service;

import com.repopulse.user.model.User;
import com.repopulse.user.dao.UserDAO;

public class AuthService {

    private UserDAO userDAO = new UserDAO();

    public User login(String username, String password) {
        User user = userDAO.getUserByUsername(username);

        if(user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public void signup(User user) {
        userDAO.createUser(user);
    }
}
