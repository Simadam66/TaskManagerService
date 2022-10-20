package com.example.demo.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        return handleExceptionInternal(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {EmailTakenException.class})
    public ResponseEntity<ErrorResponse> handleEmailTakenException(EmailTakenException ex) {
        return handleExceptionInternal(ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {TaskMismatchException.class})
    public ResponseEntity<ErrorResponse> handleTaskMismatchException(TaskMismatchException ex) {
        return handleExceptionInternal(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        return handleExceptionInternal(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        StringBuilder sbMessage = new StringBuilder();
        ex.getFieldErrors().forEach(e -> {
            sbMessage.append("Error: The field \"");
            sbMessage.append(e.getField());
            sbMessage.append("\" is invalid, reason: ");
            sbMessage.append(e.getDefaultMessage());
            sbMessage.append("; ");
        });
        String message = sbMessage.substring(0, sbMessage.length() - 2);

        return new ResponseEntity<>(new ErrorResponse(message), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    protected ResponseEntity<ErrorResponse> handleExceptionInternal(RuntimeException ex, HttpStatus status) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), new HttpHeaders(), status);
    }

}
