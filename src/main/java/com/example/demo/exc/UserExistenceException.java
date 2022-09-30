package com.example.demo.exc;

public class UserExistenceException extends RuntimeException {
    public UserExistenceException(Long userId) {
        super("user with id " + userId + " does not exist");
    }
}
