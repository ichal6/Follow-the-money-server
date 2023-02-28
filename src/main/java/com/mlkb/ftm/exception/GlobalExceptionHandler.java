package com.mlkb.ftm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.format.DateTimeParseException;

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

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UsernameNotFoundException UsernameNotFoundException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(UsernameNotFoundException.getMessage());
    }

    @ExceptionHandler(IllegalAccessRuntimeException.class)
    public ResponseEntity<String> handleAccessDenied(IllegalAccessRuntimeException accessDeniedException) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(accessDeniedException.getMessage());
    }

    @ExceptionHandler(InputIncorrectException.class)
    public ResponseEntity<String> handleInputIncorrectException(InputIncorrectException inputIncorrectException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(inputIncorrectException.getMessage());
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleDateTimeParseException(DateTimeParseException dateTimeParseException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                String.format("Value = %s, shouldn't be parse as date format like: yyyy-mm-dd",
                        dateTimeParseException.getParsedString()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleTypeParseException(IllegalArgumentException illegalArgumentException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(illegalArgumentException.getMessage());
    }
}
