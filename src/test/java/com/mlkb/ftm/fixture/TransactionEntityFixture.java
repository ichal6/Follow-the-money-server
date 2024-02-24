package com.mlkb.ftm.fixture;

import com.mlkb.ftm.common.Utils;
import com.mlkb.ftm.entity.Category;
import com.mlkb.ftm.entity.PaymentType;
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
        when(payee.getId()).thenReturn(4L);

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Car");
        when(category.getId()).thenReturn(5L);

        final var transaction = new Transaction();
        transaction.setId(1L);
        transaction.setTitle("Buy Car");
        transaction.setDate(new GregorianCalendar(2023, Calendar.JANUARY, 19).getTime());
        transaction.setValue(-2500.0);
        transaction.setPayee(payee);
        transaction.setCategory(category);
        transaction.setType(PaymentType.EXPENSE);
        transaction.setAccount(AccountEntityFixture.millennium());
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
        transaction.setType(PaymentType.EXPENSE);
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
        transaction.setType(PaymentType.EXPENSE);
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
        transaction.setType(PaymentType.EXPENSE);
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
        transaction.setType(PaymentType.EXPENSE);
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
        transaction.setType(PaymentType.EXPENSE);
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
        transaction.setType(PaymentType.EXPENSE);
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
        transaction.setType(PaymentType.EXPENSE);
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
        transaction.setType(PaymentType.EXPENSE);
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
        transaction.setType(PaymentType.EXPENSE);
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
        transaction.setType(PaymentType.EXPENSE);
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
        transaction.setType(PaymentType.EXPENSE);
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
        transaction.setType(PaymentType.EXPENSE);
        return transaction;
    }

    public static Transaction february2022Income() {
        Payee payee = mock(Payee.class);
        when(payee.getName()).thenReturn("Salary");

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Income");

        final var transaction = new Transaction();
        transaction.setId(17L);
        transaction.setTitle("February Income");
        transaction.setDate(new GregorianCalendar(2022, Calendar.FEBRUARY, 15).getTime());
        transaction.setValue(1500.0);
        transaction.setPayee(payee);
        transaction.setCategory(category);
        transaction.setType(PaymentType.INCOME);
        return transaction;
    }

    public static Transaction march2022Income() {
        Payee payee = mock(Payee.class);
        when(payee.getName()).thenReturn("Salary");

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Income");

        final var transaction = new Transaction();
        transaction.setId(18L);
        transaction.setTitle("March Income");
        transaction.setDate(new GregorianCalendar(2022, Calendar.MARCH, 15).getTime());
        transaction.setValue(1600.0);
        transaction.setPayee(payee);
        transaction.setCategory(category);
        transaction.setType(PaymentType.INCOME);
        return transaction;
    }

    public static Transaction april2022Income() {
        Payee payee = mock(Payee.class);
        when(payee.getName()).thenReturn("Salary");

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Income");

        final var transaction = new Transaction();
        transaction.setId(13L);
        transaction.setTitle("April Income");
        transaction.setDate(new GregorianCalendar(2022, Calendar.APRIL, 15).getTime());
        transaction.setValue(1700.0);
        transaction.setPayee(payee);
        transaction.setCategory(category);
        transaction.setType(PaymentType.INCOME);
        return transaction;
    }

    public static Transaction may2022Income() {
        Payee payee = mock(Payee.class);
        when(payee.getName()).thenReturn("Salary");

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Income");

        final var transaction = new Transaction();
        transaction.setId(14L);
        transaction.setTitle("May Income");
        transaction.setDate(new GregorianCalendar(2022, Calendar.MAY, 15).getTime());
        transaction.setValue(1500.0);
        transaction.setPayee(payee);
        transaction.setCategory(category);
        transaction.setType(PaymentType.INCOME);
        return transaction;
    }

    public static Transaction june2022Income() {
        Payee payee = mock(Payee.class);
        when(payee.getName()).thenReturn("Salary");

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Income");

        final var transaction = new Transaction();
        transaction.setId(15L);
        transaction.setTitle("June Income");
        transaction.setDate(new GregorianCalendar(2022, Calendar.JUNE, 15).getTime());
        transaction.setValue(1400.0);
        transaction.setPayee(payee);
        transaction.setCategory(category);
        transaction.setType(PaymentType.INCOME);
        return transaction;
    }

    public static Transaction july2022Income() {
        Payee payee = mock(Payee.class);
        when(payee.getName()).thenReturn("Salary");

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Income");

        final var transaction = new Transaction();
        transaction.setId(16L);
        transaction.setTitle("July Income");
        transaction.setDate(new GregorianCalendar(2022, Calendar.JULY, 15).getTime());
        transaction.setValue(1600.0);
        transaction.setPayee(payee);
        transaction.setCategory(category);
        transaction.setType(PaymentType.INCOME);
        return transaction;
    }

    public static Transaction august2022Income() {
        Payee payee = mock(Payee.class);
        when(payee.getName()).thenReturn("Salary");

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Income");

        final var transaction = new Transaction();
        transaction.setId(17L);
        transaction.setTitle("August Income");
        transaction.setDate(new GregorianCalendar(2022, Calendar.AUGUST, 15).getTime());
        transaction.setValue(1800.0);
        transaction.setPayee(payee);
        transaction.setCategory(category);
        transaction.setType(PaymentType.INCOME);
        return transaction;
    }

    public static Transaction salaryTransaction_October2022() {
        Payee payee = mock(Payee.class);
        when(payee.getName()).thenReturn("ABC Company");

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Salary");

        final var transaction = new Transaction();
        transaction.setId(14L);
        transaction.setTitle("October Salary");
        transaction.setDate(new GregorianCalendar(2022, Calendar.OCTOBER, 5).getTime());
        transaction.setValue(1500.0);
        transaction.setPayee(payee);
        transaction.setCategory(category);
        transaction.setType(PaymentType.INCOME);
        return transaction;
    }

    public static Transaction salaryTransaction_December2022() {
        Payee payee = mock(Payee.class);
        when(payee.getName()).thenReturn("ABC Company");

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Salary");

        final var transaction = new Transaction();
        transaction.setId(16L);
        transaction.setTitle("December Salary");
        transaction.setDate(new GregorianCalendar(2022, Calendar.DECEMBER, 5).getTime());
        transaction.setValue(1900.0);
        transaction.setPayee(payee);
        transaction.setCategory(category);
        transaction.setType(PaymentType.INCOME);
        return transaction;
    }

    public static Transaction buyBooksAllegroPay_November2022() {
        Payee payee = mock(Payee.class);
        when(payee.getName()).thenReturn("Allegro Pay");

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Online shop");

        final var transaction = new Transaction();
        transaction.setId(19L);
        transaction.setTitle("Buy books");
        transaction.setDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 5).getTime());
        transaction.setValue(230.0);
        transaction.setPayee(payee);
        transaction.setCategory(category);
        transaction.setType(PaymentType.EXPENSE);
        return transaction;
    }

    public static Transaction buyAlcoholAllegroPay_December2022() {
        Payee payee = mock(Payee.class);
        when(payee.getName()).thenReturn("Allegro Pay");

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Online shop");

        final var transaction = new Transaction();
        transaction.setId(20L);
        transaction.setTitle("Black Doctor");
        transaction.setDate(new GregorianCalendar(2022, Calendar.DECEMBER, 5).getTime());
        transaction.setValue(39.0);
        transaction.setPayee(payee);
        transaction.setCategory(category);
        transaction.setType(PaymentType.EXPENSE);
        return transaction;
    }

    public static Transaction buySugarTransaction() {
        Payee payee = mock(Payee.class);
        when(payee.getName()).thenReturn("Biedronka");

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Daily");

        final var transaction = new Transaction();
        transaction.setId(21L);
        transaction.setTitle("Buy Sugar");
        transaction.setDate(new GregorianCalendar(2023, Calendar.FEBRUARY, 27).getTime());
        transaction.setValue(-4.99);
        transaction.setPayee(payee);
        transaction.setCategory(category);
        transaction.setType(PaymentType.EXPENSE);
        return transaction;
    }

    public static Transaction abonamentAWSTransaction() {
        Payee payee = mock(Payee.class);
        when(payee.getName()).thenReturn("Amazon");

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Hosting");

        final var transaction = new Transaction();
        transaction.setId(22L);
        transaction.setTitle("Set Hosting");
        transaction.setDate(new GregorianCalendar(2023, Calendar.FEBRUARY, 27).getTime());
        transaction.setValue(-50.98);
        transaction.setPayee(payee);
        transaction.setCategory(category);
        transaction.setType(PaymentType.EXPENSE);
        return transaction;
    }

    public static Transaction billiardTransaction() {
        Payee payee = mock(Payee.class);
        when(payee.getName()).thenReturn("Bowling alley");

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Entertainment");

        final var transaction = new Transaction();
        transaction.setId(23L);
        transaction.setTitle("Go to Billiard");
        transaction.setDate(new GregorianCalendar(2023, Calendar.FEBRUARY, 27).getTime());
        transaction.setValue(-23.00);
        transaction.setPayee(payee);
        transaction.setCategory(category);
        transaction.setType(PaymentType.EXPENSE);
        return transaction;
    }

    public static Transaction salaryTransaction() {
        Payee payee = mock(Payee.class);
        when(payee.getName()).thenReturn("Work");

        Category category = mock(Category.class);
        when(category.getName()).thenReturn("Salary");

        final var transaction = new Transaction();
        transaction.setId(24L);
        transaction.setTitle("Salary from February");
        transaction.setDate(new GregorianCalendar(2023, Calendar.FEBRUARY, 3).getTime());
        transaction.setValue(1700.00);
        transaction.setPayee(payee);
        transaction.setCategory(category);
        transaction.setType(PaymentType.INCOME);
        return transaction;
    }

    public static Transaction getTaxiTransaction() {
        final var transaction = new Transaction();
        transaction.setId(25L);
        transaction.setTitle("Get Taxi");
        transaction.setDate(Utils.getDate(2024, Calendar.FEBRUARY, 24, 22, 13));
        transaction.setValue(-20.00);
        transaction.setPayee(PayeeEntityFixture.SuperTaxi());
        transaction.setCategory(CategoryEntityFixture.getTaxi());
        transaction.setAccount(AccountEntityFixture.millennium());
        transaction.setType(PaymentType.EXPENSE);
        return transaction;
    }
}
