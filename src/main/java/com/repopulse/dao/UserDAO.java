package com.repopulse.dao;

import com.repopulse.model.User;

public interface UserDAO {
    void createUser(User user);

    User getUserByUsername(String username);
}
