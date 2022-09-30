package com.example.demo.exc;

public class EmailTakenException extends RuntimeException {
    public EmailTakenException() {
        super("this email is already taken");
    }
}
