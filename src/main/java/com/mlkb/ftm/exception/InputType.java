package com.mlkb.ftm.exception;

public enum InputType {
    ID("Id should be a number greater than 0."),
    NAME("Name should be at least 3 characters long."),
    ACCOUNT_TYPE("The account should have a valid account type: Cash or Bank."),
    BALANCE("Balance should be a valid number (positive or negative)."),
    EMAIL("Email should be in format: user@domain.pl. No special signs allowed.");

    public final String message;

    private InputType(String message) {
        this.message = message;
    }
}
