package com.mlkb.ftm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleJSONParseException(HttpMessageNotReadableException httpMessageNotReadableException) {
        String message = "Couldn't process your request. Your JSON is the wrong format";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException resourceNotFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resourceNotFoundException.getMessage());
    }

    @ExceptionHandler(InputIncorrectException.class)
    public ResponseEntity<String> handleInputIncorrectException(InputIncorrectException inputIncorrectException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(inputIncorrectException.getMessage());
    }
}
