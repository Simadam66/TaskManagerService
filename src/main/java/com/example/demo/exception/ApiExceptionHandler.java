package com.example.demo.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = { EntityNotFoundException.class })
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        return handleExceptionInternal(ex, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = { EmailTakenException.class })
    public ResponseEntity<ErrorResponse> handleEmailTakenException(EmailTakenException ex) {
        return handleExceptionInternal(ex, new HttpHeaders(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = { TaskMismatchException.class })
    public ResponseEntity<ErrorResponse> handleTaskMismatchException(TaskMismatchException ex) {
        return handleExceptionInternal(ex, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { RuntimeException.class })
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        return handleExceptionInternal(ex, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    protected ResponseEntity<ErrorResponse> handleExceptionInternal(RuntimeException ex, HttpHeaders headers, HttpStatus status) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()),headers, status);
    }

}
