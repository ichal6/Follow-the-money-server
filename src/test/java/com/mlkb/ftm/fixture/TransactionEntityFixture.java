package com.mlkb.ftm.fixture;

import com.mlkb.ftm.entity.Category;
import com.mlkb.ftm.entity.GeneralType;
import com.mlkb.ftm.entity.Payee;
import com.mlkb.ftm.entity.Transaction;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactionEntityFixture {
    public static Transaction buyCarTransaction() {
        Payee payee = mock(Payee.class);
        when(payee.getName()).thenReturn("Złomex");

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Car");


        final var transaction = new Transaction();
        transaction.setId(1L);
        transaction.setTitle("Buy Car");
        transaction.setDate(new GregorianCalendar(2023, Calendar.JANUARY, 19).getTime());
        transaction.setValue(-2500.0);
        transaction.setPayee(payee);
        transaction.setCategory(category);
        transaction.setType(GeneralType.EXPENSE);
        return transaction;
    }

    public static Transaction buyMilkTransaction() {
        Payee payee = mock(Payee.class);
        when(payee.getName()).thenReturn("Biedronka");

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Daily");

        final var transaction = new Transaction();
        transaction.setId(2L);
        transaction.setTitle("Buy Milk");
        transaction.setDate(new GregorianCalendar(2023, Calendar.JANUARY, 16).getTime());
        transaction.setValue(-5.0);
        transaction.setPayee(payee);
        transaction.setCategory(category);
        transaction.setType(GeneralType.EXPENSE);
        return transaction;
    }

    public static Transaction buySugarTransaction() {
        Payee payee = mock(Payee.class);
        when(payee.getName()).thenReturn("Biedronka");

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Daily");

        final var transaction = new Transaction();
        transaction.setId(3L);
        transaction.setTitle("Buy Sugar");
        transaction.setDate(new GregorianCalendar(2023, Calendar.FEBRUARY, 27).getTime());
        transaction.setValue(-4.99);
        transaction.setPayee(payee);
        transaction.setCategory(category);
        transaction.setType(GeneralType.EXPENSE);
        return transaction;
    }

    public static Transaction abonamentAWSTransaction() {
        Payee payee = mock(Payee.class);
        when(payee.getName()).thenReturn("Amazon");

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Hosting");

        final var transaction = new Transaction();
        transaction.setId(4L);
        transaction.setTitle("Set Hosting");
        transaction.setDate(new GregorianCalendar(2023, Calendar.FEBRUARY, 27).getTime());
        transaction.setValue(-50.98);
        transaction.setPayee(payee);
        transaction.setCategory(category);
        transaction.setType(GeneralType.EXPENSE);
        return transaction;
    }

    public static Transaction billiardTransaction() {
        Payee payee = mock(Payee.class);
        when(payee.getName()).thenReturn("Bowling alley");

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Entertainment");

        final var transaction = new Transaction();
        transaction.setId(5L);
        transaction.setTitle("Go to Billiard");
        transaction.setDate(new GregorianCalendar(2023, Calendar.FEBRUARY, 27).getTime());
        transaction.setValue(-23.00);
        transaction.setPayee(payee);
        transaction.setCategory(category);
        transaction.setType(GeneralType.EXPENSE);
        return transaction;
    }

    public static Transaction salaryTransaction() {
        Payee payee = mock(Payee.class);
        when(payee.getName()).thenReturn("Work");

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Salary");

        final var transaction = new Transaction();
        transaction.setId(6L);
        transaction.setTitle("Salary from February");
        transaction.setDate(new GregorianCalendar(2023, Calendar.FEBRUARY, 3).getTime());
        transaction.setValue(1700.00);
        transaction.setPayee(payee);
        transaction.setCategory(category);
        transaction.setType(GeneralType.INCOME);
        return transaction;
    }
}
