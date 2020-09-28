package com.mlkb.ftm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleException(HttpMessageNotReadableException httpMessageNotReadableException) {
        String message = "Couldn't process your request. Your JSON is the wrong format";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}
