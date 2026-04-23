package com.repopulse.auth.service;

import com.repopulse.user.model.User;
import com.repopulse.user.dao.UserDAO;

public class AuthService {

    private static final String ADMIN_USERNAME = "admin";
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

    public User loginAdmin(String password) {
        User adminUser = userDAO.getUserByUsername(ADMIN_USERNAME);
        if(adminUser != null && adminUser.getPassword().equals(password)) {
            return adminUser;
        }
        return null;
    }
}
