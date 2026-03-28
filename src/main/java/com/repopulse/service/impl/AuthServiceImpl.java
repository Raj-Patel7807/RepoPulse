package com.repopulse.service.impl;

import com.repopulse.model.User;
import com.repopulse.service.AuthService;
import com.repopulse.dao.UserDAO;
import com.repopulse.dao.impl.UserDAOImpl;

public class AuthServiceImpl implements AuthService {
    private UserDAO userDAO = new UserDAOImpl();

    @Override
    public User login(String username, String password) {
        User user = userDAO.getUserByUsername(username);
        System.out.println("INPUT PASSWORD: [" + password + "]");
        System.out.println("DB PASSWORD: [" + user.getPassword() + "]");

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
