package com.example.demo.exc;

public class TaskExistenceException extends RuntimeException {
    public TaskExistenceException(Long taskId) {
        super("task with id " + taskId + " does not exist");
    }
}
