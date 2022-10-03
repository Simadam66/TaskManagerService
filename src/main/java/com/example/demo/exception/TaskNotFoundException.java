package com.example.demo.exception;

public class TaskNotFoundException extends EntityNotFoundException {
    public TaskNotFoundException(Long taskId) {
        super("Task with id " + taskId + " does not exist.");
    }
}
