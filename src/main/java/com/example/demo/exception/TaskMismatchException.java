package com.example.demo.exception;

public class TaskMismatchException extends RuntimeException {
    public TaskMismatchException(Long taskId) {
        super("This user does not have a task with id: " + taskId + ".");
    }
}
