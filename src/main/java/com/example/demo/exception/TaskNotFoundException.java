package com.example.demo.exception;

public class TaskNotFoundException extends EntityNotFoundException {
    public TaskNotFoundException(Long taskId) {
        super("task with id " + taskId + " does not exist");
    }
}
