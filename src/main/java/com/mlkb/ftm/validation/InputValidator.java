package com.mlkb.ftm.validation;

import com.mlkb.ftm.entity.AccountType;
import com.mlkb.ftm.exception.InputIncorrectException;
import com.mlkb.ftm.exception.InputType;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class InputValidator {

    public boolean checkId(Long id) throws InputIncorrectException {
        if (id >= 1) {
            return true;
        } else {
            throw new InputIncorrectException(InputType.ID);
        }
    }

    public boolean checkName(String name) throws InputIncorrectException {
        if (name != null && name.length() >= 3) {
            return true;
        } else {
            throw new InputIncorrectException(InputType.NAME);
        }
    }

    public boolean checkIfAccountTypeInEnum(String type) throws InputIncorrectException {
        List<AccountType> types = Arrays.asList(AccountType.values());
        try {
            return types.contains(AccountType.valueOf(type.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new InputIncorrectException(InputType.ACCOUNT_TYPE);
        }
    }

    public boolean checkBalance(Double balance) throws InputIncorrectException {
        if (balance != null) {
            return true;
        } else {
            throw new InputIncorrectException(InputType.BALANCE);
        }
    }

    public boolean checkEmail(String email) throws InputIncorrectException {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return true;
        } else {
            throw new InputIncorrectException(InputType.EMAIL);
        }
    }

    public boolean checkPassword(String password) throws InputIncorrectException {
        // TODO - should add more password validation logic
        return password != null && !password.isBlank();
    }
}
