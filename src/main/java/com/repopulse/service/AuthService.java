package com.repopulse.service;

import com.repopulse.model.User;
import com.repopulse.dao.UserDAOImpl;

public class AuthServiceImpl implements AuthService {
    private UserDAO userDAO = new UserDAOImpl();

    @Override
    public User login(String username, String password) {
        User user = userDAO.getUserByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    @Override
    public void signup(User user) {
        userDAO.createUser(user);
    }
}
