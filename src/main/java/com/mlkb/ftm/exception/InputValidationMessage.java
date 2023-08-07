package com.mlkb.ftm.exception;

public enum InputValidationMessage {
    ID("Id should be a number greater than 0."),
    NAME("Name should be at least 3 characters long."),
    ACCOUNT_TYPE("The account should have a valid account type: Cash or Bank."),
    PAYMENT_TYPE("The type should be: Income or Expense"),
    BALANCE("Balance should be a valid number (positive or negative)."),
    BALANCE_POSITIVE("Balance should be a valid number above 0."),
    DATE("Date shouldn't be empty - choose a valid date format."),
    EMAIL("Email should be in format: user@domain.pl. No special signs allowed."),
    PASSWORD("Password incorrect. Should be at least..."),
    NULL("Object is null"),
    INCORRECT_TYPE("Incorrect payment type for value"),
    TRANSFER_ACCOUNTS_ID("For transfer account from and account to should be different");

    public final String message;

    private InputValidationMessage(String message) {
        this.message = message;
    }
}
