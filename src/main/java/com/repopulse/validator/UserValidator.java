package com.repopulse.validator;

import java.util.regex.Pattern;

public class UserValidator {

    private UserValidator() {

    }

    public static void validateUsername(String username) {
        if(username == null || username.length() < 3 || username.length() > 20) {
            throw new IllegalArgumentException("Username must be 3-20 characters long");
        }
    }

    public static void validateEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if(email == null || !Pattern.matches(emailRegex, email)) {
            throw new IllegalArgumentException("Invalid email address");
        }
    }

    public static void validatePassword(String password) {
        if(password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
    }
}
