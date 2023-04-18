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

    public static Transaction buyGroceriesTransactionFebruary() {
        Payee payee = mock(Payee.class);
        when(payee.getName()).thenReturn("Lidl");

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Groceries");

        final var transaction = new Transaction();
        transaction.setId(3L);
        transaction.setTitle("Buy Groceries");
        transaction.setDate(new GregorianCalendar(2022, Calendar.FEBRUARY, 2).getTime());
        transaction.setValue(-45.0);
        transaction.setPayee(payee);
        transaction.setCategory(category);
        transaction.setType(GeneralType.EXPENSE);
        return transaction;
    }

    public static Transaction buyGasTransactionMarch() {
        Payee payee = mock(Payee.class);
        when(payee.getName()).thenReturn("OMV");

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Gas");

        final var transaction = new Transaction();
        transaction.setId(4L);
        transaction.setTitle("Buy Gas");
        transaction.setDate(new GregorianCalendar(2022, Calendar.MARCH, 5).getTime());
        transaction.setValue(-55.0);
        transaction.setPayee(payee);
        transaction.setCategory(category);
        transaction.setType(GeneralType.EXPENSE);
        return transaction;
    }

    public static Transaction buyClothesTransactionApril() {
        Payee payee = mock(Payee.class);
        when(payee.getName()).thenReturn("H&M");

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Clothes");

        final var transaction = new Transaction();
        transaction.setId(5L);
        transaction.setTitle("Buy Clothes");
        transaction.setDate(new GregorianCalendar(2022, Calendar.APRIL, 10).getTime());
        transaction.setValue(-80.0);
        transaction.setPayee(payee);
        transaction.setCategory(category);
        transaction.setType(GeneralType.EXPENSE);
        return transaction;
    }

    public static Transaction buyGiftTransactionMay() {
        Payee payee = mock(Payee.class);
        when(payee.getName()).thenReturn("Empik");

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Gifts");

        final var transaction = new Transaction();
        transaction.setId(6L);
        transaction.setTitle("Buy Gift");
        transaction.setDate(new GregorianCalendar(2022, Calendar.MAY, 15).getTime());
        transaction.setValue(-30.0);
        transaction.setPayee(payee);
        transaction.setCategory(category);
        transaction.setType(GeneralType.EXPENSE);
        return transaction;
    }

    public static Transaction buyPhoneTransactionJune() {
        Payee payee = mock(Payee.class);
        when(payee.getName()).thenReturn("Orange");

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Phone");

        final var transaction = new Transaction();
        transaction.setId(7L);
        transaction.setTitle("Buy Phone");
        transaction.setDate(new GregorianCalendar(2022, Calendar.JUNE, 20).getTime());
        transaction.setValue(-1000.0);
        transaction.setPayee(payee);
        transaction.setCategory(category);
        transaction.setType(GeneralType.EXPENSE);
        return transaction;
    }

    public static Transaction buyRestaurantTransactionJuly() {
        Payee payee = mock(Payee.class);
        when(payee.getName()).thenReturn("Pizza Hut");

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Restaurants");

        final var transaction = new Transaction();
        transaction.setId(8L);
        transaction.setTitle("Eat at Restaurant");
        transaction.setDate(new GregorianCalendar(2022, Calendar.JULY, 25).getTime());
        transaction.setValue(-35.0);
        transaction.setPayee(payee);
        transaction.setCategory(category);
        transaction.setType(GeneralType.EXPENSE);
        return transaction;
    }

    public static Transaction buyBooksTransactionAugust() {
        Payee payee = mock(Payee.class);
        when(payee.getName()).thenReturn("Amazon");

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Books");

        final var transaction = new Transaction();
        transaction.setId(9L);
        transaction.setTitle("Buy Books");
        transaction.setDate(new GregorianCalendar(2022, Calendar.AUGUST, 15).getTime());
        transaction.setValue(-50.0);
        transaction.setPayee(payee);
        transaction.setCategory(category);
        transaction.setType(GeneralType.EXPENSE);
        return transaction;
    }

    public static Transaction buyBookTransactionSeptember2022() {
        Payee payee = mock(Payee.class);
        when(payee.getName()).thenReturn("Empik");

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Education");

        final var transaction = new Transaction();
        transaction.setId(9L);
        transaction.setTitle("Buy book");
        transaction.setDate(new GregorianCalendar(2022, Calendar.SEPTEMBER, 22).getTime());
        transaction.setValue(-40.0);
        transaction.setPayee(payee);
        transaction.setCategory(category);
        transaction.setType(GeneralType.EXPENSE);
        return transaction;
    }

    public static Transaction buyConcertTicketTransactionNovember2022() {
        Payee payee = mock(Payee.class);
        when(payee.getName()).thenReturn("Ticketmaster");

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Entertainment");

        final var transaction = new Transaction();
        transaction.setId(10L);
        transaction.setTitle("Concert ticket");
        transaction.setDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 15).getTime());
        transaction.setValue(-70.0);
        transaction.setPayee(payee);
        transaction.setCategory(category);
        transaction.setType(GeneralType.EXPENSE);
        return transaction;
    }

    public static Transaction buyHalloweenCostumeTransactionOctober2022() {
        Payee payee = mock(Payee.class);
        when(payee.getName()).thenReturn("H&M");

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Clothing");

        final var transaction = new Transaction();
        transaction.setId(11L);
        transaction.setTitle("Halloween costume");
        transaction.setDate(new GregorianCalendar(2022, Calendar.OCTOBER, 29).getTime());
        transaction.setValue(-30.0);
        transaction.setPayee(payee);
        transaction.setCategory(category);
        transaction.setType(GeneralType.EXPENSE);
        return transaction;
    }

    public static Transaction buyChristmasGiftTransaction() {
        Payee payee = mock(Payee.class);
        when(payee.getName()).thenReturn("Amazon");

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Gifts");

        final var transaction = new Transaction();
        transaction.setId(12L);
        transaction.setTitle("Christmas gift");
        transaction.setDate(new GregorianCalendar(2022, Calendar.DECEMBER, 20).getTime());
        transaction.setValue(-50.0);
        transaction.setPayee(payee);
        transaction.setCategory(category);
        transaction.setType(GeneralType.EXPENSE);
        return transaction;
    }


}
