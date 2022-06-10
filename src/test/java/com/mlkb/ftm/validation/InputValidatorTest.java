package com.mlkb.ftm.validation;

import com.mlkb.ftm.ApplicationConfig;
import com.mlkb.ftm.exception.InputIncorrectException;
import com.mlkb.ftm.exception.InputValidationMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
@SpringBootTest
class InputValidatorTest {

    private InputValidator inputValidator;

    @BeforeEach
    public void setUp(){
        this.inputValidator = new InputValidator();
    }

    @Test
    void should_return_true_if_id_is_ok() throws InputIncorrectException {
        // given
        Long id = 2L;

        // when
        boolean isOk = this.inputValidator.checkId(id);

        // then
        assertTrue(isOk);
    }

    @Test
    void should_throw_an_exception_if_id_is_not_ok() {
        // given
        Long id = -2L;

        // when
        InputIncorrectException thrown = Assertions.assertThrows(InputIncorrectException.class, () ->
                inputValidator.checkId(id), "Wrong ID");

        // then
        assertEquals("Id should be a number greater than 0.", thrown.getMessage());
    }

    @Test
    void should_return_true_if_name_is_ok() throws InputIncorrectException {
        // given
        String name = "Smith";

        // when
        boolean isOk = this.inputValidator.checkName(name);

        // then
        assertTrue(isOk);
    }

    @Test
    void should_throw_an_exception_if_name_is_empty() {
        // given
        String name = "";

        // when
        InputIncorrectException thrown = Assertions.assertThrows(InputIncorrectException.class, () ->
                inputValidator.checkName(name));

        // then
        assertEquals(InputValidationMessage.NAME.message, thrown.getMessage());
    }

    @Test
    void should_throw_an_exception_if_name_has_wrong_size() {
        // given
        String name = "ML";

        // when
        InputIncorrectException thrown = Assertions.assertThrows(InputIncorrectException.class, () ->
                inputValidator.checkName(name));

        // then
        assertEquals(InputValidationMessage.NAME.message, thrown.getMessage());
    }

    @Test
    void should_return_true_if_account_type_in_enum() throws InputIncorrectException {
        // given
        String typeCash = "CAsH";
        String typeBank = "banK";

        // when
        boolean isCashOk = this.inputValidator.checkIfAccountTypeInEnum(typeCash);
        boolean isBankOk = this.inputValidator.checkIfAccountTypeInEnum(typeBank);

        // then
        assertTrue(isCashOk);
        assertTrue(isBankOk);
    }

    @Test
    void should_throw_an_exception_if_account_type_is_not_in_enum() {
        // given
        String type = "Card";

        // when
        InputIncorrectException thrown = Assertions.assertThrows(InputIncorrectException.class, () ->
                inputValidator.checkIfAccountTypeInEnum(type));

        // then
        assertEquals(InputValidationMessage.ACCOUNT_TYPE.message, thrown.getMessage());
    }

    @Test
    void should_return_true_if_general_type_in_enum() throws InputIncorrectException {
        // given
        String typeIncome = "INCOME";
        String typeExpense = "Expense";

        // when
        boolean isIncomeOk = this.inputValidator.checkIfGeneralTypeInEnum(typeIncome);
        boolean isExpenseOk = this.inputValidator.checkIfGeneralTypeInEnum(typeExpense);

        // then
        assertTrue(isIncomeOk);
        assertTrue(isExpenseOk);
    }

    @Test
    void should_throw_an_exception_if_general_type_is_not_in_enum() {
        // given
        String type = "Multiply";

        // when
        InputIncorrectException thrown = Assertions.assertThrows(InputIncorrectException.class, () ->
                inputValidator.checkIfGeneralTypeInEnum(type));

        // then
        assertEquals(InputValidationMessage.GENERAL_TYPE.message, thrown.getMessage());
    }

    @Test
    void should_return_true_if_balance_is_ok() throws InputIncorrectException {
        // given
        Double balance = 123.0;

        // when
        boolean isOk = this.inputValidator.checkBalance(balance);

        // then
        assertTrue(isOk);
    }

    @Test
    void should_throw_an_exception_if_balance_is_null() {
        // given
        Double balance = null;

        // when
        InputIncorrectException thrown = Assertions.assertThrows(InputIncorrectException.class, () ->
                inputValidator.checkBalance(balance));

        // then
        assertEquals(InputValidationMessage.BALANCE.message, thrown.getMessage());
    }

    @Test
    void should_return_true_if_balance_positive_is_ok() throws InputIncorrectException {
        // given
        Double balance = 123.0;

        // when
        boolean isOk = this.inputValidator.checkBalancePositive(balance);

        // then
        assertTrue(isOk);
    }

    @Test
    void should_throw_an_exception_if_balance_positive_is_null() {
        // given
        Double balance = null;

        // when
        InputIncorrectException thrown = Assertions.assertThrows(InputIncorrectException.class, () ->
                inputValidator.checkBalancePositive(balance));

        // then
        assertEquals(InputValidationMessage.BALANCE_POSITIVE.message, thrown.getMessage());
    }

    @Test
    void should_throw_an_exception_if_balance_positive_is_less_than_0() {
        // given
        Double balance = -123.0;

        // when
        InputIncorrectException thrown = Assertions.assertThrows(InputIncorrectException.class, () ->
                inputValidator.checkBalancePositive(balance));

        // then
        assertEquals(InputValidationMessage.BALANCE_POSITIVE.message, thrown.getMessage());
    }

    @Test
    void checkBalance() {
    }

    @Test
    void checkBalancePositive() {
    }

    @Test
    void checkEmail() {
    }

    @Test
    void checkPassword() {
    }

    @Test
    void checkDate() {
    }
}