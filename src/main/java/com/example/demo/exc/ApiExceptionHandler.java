package com.example.demo.exc;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = { UserExistenceException.class, EmailTakenException.class })
    public ResponseEntity<Object> handleUserRelatedException(RuntimeException ex) {

        if (ex instanceof UserExistenceException) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            UserExistenceException ue = (UserExistenceException) ex;
            return handleExceptionInternal(ue, new HttpHeaders(), status);
        }

        else if (ex instanceof EmailTakenException) {
            HttpStatus status = HttpStatus.CONFLICT;
            EmailTakenException et = (EmailTakenException) ex;
            return handleExceptionInternal(et, new HttpHeaders(), status);
        }

        else {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            return handleExceptionInternal(ex, new HttpHeaders(), status);
        }
    }

    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, HttpHeaders headers, HttpStatus status) {
        return new ResponseEntity<>(ex,headers, status);
    }

}
