package com.mlkb.ftm.exception;

public enum InputValidationMessage {
    ID("Id should be a number greater than 0."),
    NAME("Name should be at least 3 characters long."),
    ACCOUNT_TYPE("The account should have a valid account type: Cash or Bank."),
    GENERAL_TYPE("The type should be: Income or Expense"),
    BALANCE("Balance should be a valid number (positive or negative)."),
    DATE("Date shouldn't be empty - choose a valid date format."),
    EMAIL("Email should be in format: user@domain.pl. No special signs allowed."),
    PASSWORD("Password incorrect. Should be at least...");

    public final String message;

    private InputValidationMessage(String message) {
        this.message = message;
    }
}
