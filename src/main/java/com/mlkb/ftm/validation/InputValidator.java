package com.mlkb.ftm.validation;

import com.mlkb.ftm.entity.AccountType;
import com.mlkb.ftm.entity.GeneralType;
import com.mlkb.ftm.exception.InputIncorrectException;
import com.mlkb.ftm.exception.InputValidationMessage;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class InputValidator {

    public boolean checkId(Long id) throws InputIncorrectException {
        if (id >= 1) {
            return true;
        } else {
            throw new InputIncorrectException(InputValidationMessage.ID);
        }
    }

    public boolean checkName(String name) throws InputIncorrectException {
        if (name != null && name.length() >= 3) {
            return true;
        } else {
            throw new InputIncorrectException(InputValidationMessage.NAME);
        }
    }

    public boolean checkIfAccountTypeInEnum(String type) throws InputIncorrectException {
        List<AccountType> types = Arrays.asList(AccountType.values());
        try {
            return types.contains(AccountType.valueOf(type.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new InputIncorrectException(InputValidationMessage.ACCOUNT_TYPE);
        }
    }

    public boolean checkIfGeneralTypeInEnum(String type) throws InputIncorrectException {
        List<GeneralType> types = Arrays.asList(GeneralType.values());
        try {
            return types.contains(GeneralType.valueOf(type.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new InputIncorrectException(InputValidationMessage.GENERAL_TYPE);
        }
    }

    public boolean checkBalance(Double balance) throws InputIncorrectException {
        if (balance != null) {
            return true;
        } else {
            throw new InputIncorrectException(InputValidationMessage.BALANCE);
        }
    }

    public boolean checkBalancePositive(Double balance) throws InputIncorrectException {
        if (balance != null && balance >= 0) {
            return true;
        } else {
            throw new InputIncorrectException(InputValidationMessage.BALANCE_POSITIVE);
        }
    }

    public boolean checkEmail(String email) throws InputIncorrectException {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return true;
        } else {
            throw new InputIncorrectException(InputValidationMessage.EMAIL);
        }
    }

    public boolean checkPassword(String password) throws InputIncorrectException {
        // TODO - should add more password validation logic and message to enum
        if (password != null && !password.isBlank()) {
            return true;
        } else {
            throw new InputIncorrectException(InputValidationMessage.PASSWORD);
        }
    }

    public boolean checkDate(Date date) throws InputIncorrectException {
        if (date != null) {
            return true;
        } else {
            throw new InputIncorrectException(InputValidationMessage.DATE);
        }
    }
}
