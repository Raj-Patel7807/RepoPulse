package com.repopulse.infra.exception;

public class DataAccessException extends AppException {
    public DataAccessException(String userMessage, Throwable cause) {
        super(userMessage, cause);
    }
}

