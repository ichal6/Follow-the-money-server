package com.mlkb.ftm.exception;

public class InputIncorrectException extends Exception {
    public InputIncorrectException(InputValidationMessage inputValidationMessage) {
        super(inputValidationMessage.message);
    }
}
