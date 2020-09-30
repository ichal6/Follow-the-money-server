package com.mlkb.ftm.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException() {
        super("Sorry, couldn't get the resource you are asking for.");
    }
}
