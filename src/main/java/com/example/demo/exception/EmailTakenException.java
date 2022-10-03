package com.example.demo.exception;

public class EmailTakenException extends RuntimeException {
    public EmailTakenException() {
        super("this email is already taken");
    }
}
