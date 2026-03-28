package com.repopulse.service;

import com.repopulse.model.User;

public interface AuthService {

    User login(String username, String password);

    void signup(User user);
}