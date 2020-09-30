package com.mlkb.ftm.exception;

public class InputIncorrectException extends Exception {
    public InputIncorrectException(InputType inputType) {
        super(inputType.message);
    }
}
