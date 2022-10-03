package com.example.demo.exception;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException(Long userId) {
        super("User with id " + userId + " does not exist.");
    }
}
