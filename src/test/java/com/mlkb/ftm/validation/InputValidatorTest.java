package com.mlkb.ftm.validation;

import com.mlkb.ftm.common.ApplicationConfig;
import com.mlkb.ftm.entity.PaymentType;
import com.mlkb.ftm.exception.InputIncorrectException;
import com.mlkb.ftm.exception.InputValidationMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

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
    void should_throw_an_exception_if_id_is_null() {
        // given /when
        InputIncorrectException thrown = Assertions.assertThrows(InputIncorrectException.class, () ->
                inputValidator.checkId(null), "Wrong ID");

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
        boolean isIncomeOk = this.inputValidator.checkIfPaymentTypeInEnum(typeIncome);
        boolean isExpenseOk = this.inputValidator.checkIfPaymentTypeInEnum(typeExpense);

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
                inputValidator.checkIfPaymentTypeInEnum(type));

        // then
        assertEquals(InputValidationMessage.PAYMENT_TYPE.message, thrown.getMessage());
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
    void should_return_true_if_email_is_ok() throws InputIncorrectException {
        // given
        String email = "example@domain.com";

        // when
        boolean isOk = this.inputValidator.checkEmail(email);

        // then
        assertTrue(isOk);
    }

    @Test
    void should_throw_an_exception_if_email_is_not_ok() {
        // given
        String email = "wrong@@@|com";

        // when
        InputIncorrectException thrown = Assertions.assertThrows(InputIncorrectException.class, () ->
                inputValidator.checkEmail(email));

        // then
        assertEquals(InputValidationMessage.EMAIL.message, thrown.getMessage());
    }

    @Test
    void should_return_true_if_password_is_ok() throws InputIncorrectException {
        // given
        String password = "2na6~<_Ga;M`3sCOY]%d";

        // when
        boolean isOk = this.inputValidator.checkPassword(password);

        // then
        assertTrue(isOk);
    }

    @Test
    void should_throw_an_exception_if_password_is_blank() {
        // given
        String password = "";

        // when
        InputIncorrectException thrown = Assertions.assertThrows(InputIncorrectException.class, () ->
                inputValidator.checkPassword(password));

        // then
        assertEquals(InputValidationMessage.PASSWORD.message, thrown.getMessage());
    }

    @Test
    void should_throw_an_exception_if_password_is_null() {
        // given
        String password = null;

        // when
        InputIncorrectException thrown = Assertions.assertThrows(InputIncorrectException.class, () ->
                inputValidator.checkPassword(password));

        // then
        assertEquals(InputValidationMessage.PASSWORD.message, thrown.getMessage());
    }

    @Test
    void should_return_true_if_date_is_ok() throws InputIncorrectException {
        // given
        Date date = new Date(System.currentTimeMillis());

        // when
        boolean isOk = this.inputValidator.checkDate(date);

        // then
        assertTrue(isOk);
    }

    @Test
    void should_throw_an_exception_if_date_is_null() {
        // given
        Date date = null;

        // when
        InputIncorrectException thrown = Assertions.assertThrows(InputIncorrectException.class, () ->
                inputValidator.checkDate(date));

        // then
        assertEquals(InputValidationMessage.DATE.message, thrown.getMessage());
    }

    @Test
    void should_throw_an_exception_if_payment_type_is_income_for_minus_value() {
        // given
        PaymentType paymentType = PaymentType.INCOME;
        double value = -123.0;

        // when
        InputIncorrectException thrown = Assertions.assertThrows(InputIncorrectException.class, () ->
                inputValidator.checkIfPaymentTypeCorrectWithValue(paymentType, value));

        // then
        assertEquals(InputValidationMessage.INCORRECT_TYPE.message, thrown.getMessage());
    }

    @Test
    void should_throw_an_exception_if_payment_type_is_expense_for_positive_value() {
        // given
        PaymentType paymentType = PaymentType.EXPENSE;
        double value = 123.0;

        // when
        InputIncorrectException thrown = Assertions.assertThrows(InputIncorrectException.class, () ->
                inputValidator.checkIfPaymentTypeCorrectWithValue(paymentType, value));

        // then
        assertEquals(InputValidationMessage.INCORRECT_TYPE.message, thrown.getMessage());
    }

    @Test
    void should_not_throw_an_exception_if_payment_type_is_expense_for_minus_value() {
        // given
        PaymentType paymentType = PaymentType.EXPENSE;
        double value = -123.0;

        // when / then
       Assertions.assertDoesNotThrow(() ->
                inputValidator.checkIfPaymentTypeCorrectWithValue(paymentType, value));
    }

    @Test
    void should_not_throw_an_exception_if_payment_type_is_income_for_positive_value() {
        // given
        PaymentType paymentType = PaymentType.INCOME;
        double value = 123.0;

        // when / then
        Assertions.assertDoesNotThrow(() ->
                inputValidator.checkIfPaymentTypeCorrectWithValue(paymentType, value));
    }

    @Test
    void should_throw_an_exception_if_account_from_and_account_to_is_the_same_for_transfer() {
        // given
        long account_id = 1L;

        // when
        InputIncorrectException thrown = Assertions.assertThrows(InputIncorrectException.class, () ->
                inputValidator.checkAccountIdIsDifferent(account_id, account_id));

        // then
        assertEquals(InputValidationMessage.TRANSFER_ACCOUNTS_ID.message, thrown.getMessage());
    }

    @Test
    void should_not_throw_an_exception_if_account_from_and_account_to_is_not_the_same_for_transfer() {
        // given
        long account_id_from = 1L;
        long account_id_to = 2L;

        // when / then
        Assertions.assertDoesNotThrow(() ->
                inputValidator.checkAccountIdIsDifferent(account_id_from, account_id_to));
    }
}
