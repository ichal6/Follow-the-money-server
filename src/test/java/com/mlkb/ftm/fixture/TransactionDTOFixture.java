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

        return transaction;
    }

    private static Date getDate(int year, int month, int day, int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        return cal.getTime();
    }
}
