package com.mlkb.ftm.fixture;

import com.mlkb.ftm.modelDTO.TransactionDTO;

import java.util.Calendar;
import java.util.Date;

public class TransactionDTOFixture {
    public static TransactionDTO buyCarTransaction() {
        final var transaction = new TransactionDTO();
        transaction.setId(1L);
        transaction.setTitle("Buy Car after edit");
        transaction.setAccountId(AccountEntityFixture.millennium().getId());
        transaction.setValue(-2400.0);
        transaction.setType("Expense");
        transaction.setDate(getDate(2022, 6, 23, 14, 23));
        transaction.setPayeeId(PayeeEntityFixture.MariuszTransKomis().getId());
        transaction.setCategoryId(CategoryEntityFixture.getTransport().getId());

        return transaction;
    }

    public static TransactionDTO buyCarTransactionBeforeUpdate() {
        final var transaction = new TransactionDTO();
        transaction.setId(1L);
        transaction.setTitle("Buy Car");
        transaction.setAccountId(AccountEntityFixture.millennium().getId());
        transaction.setValue(-2500.0);
        transaction.setType("EXPENSE");
        transaction.setDate(getDate(2023, Calendar.JANUARY, 19, 0, 0));
        transaction.setPayeeId(PayeeEntityFixture.MariuszTransKomis().getId());
        transaction.setCategoryId(CategoryEntityFixture.getTransport().getId());

        return transaction;
    }

    public static TransactionDTO buyCarTransactionReturn() {
        final var transaction = new TransactionDTO();
        transaction.setId(1L);
        transaction.setTitle("Buy Car after edit");
        transaction.setAccountId(AccountEntityFixture.millennium().getId());
        transaction.setValue(2400.0);
        transaction.setType("Income");
        transaction.setDate(getDate(2023, 8, 1, 16, 43));
        transaction.setPayeeId(PayeeEntityFixture.MariuszTransKomis().getId());
        transaction.setCategoryId(CategoryEntityFixture.getTransport().getId());

        return transaction;
    }

    private static Date getDate(int year, int month, int day, int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
