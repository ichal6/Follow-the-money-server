package com.mlkb.ftm.validation;

import com.mlkb.ftm.entity.AccountType;
import com.mlkb.ftm.exception.InputIncorrectException;
import com.mlkb.ftm.exception.InputType;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


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
        if (types.contains(AccountType.valueOf(type))) {
            return true;
        } else {
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
}
