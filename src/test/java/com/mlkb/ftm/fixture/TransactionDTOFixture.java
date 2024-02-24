package com.mlkb.ftm.fixture;

import com.mlkb.ftm.common.Utils;
import com.mlkb.ftm.modelDTO.TransactionDTO;

import java.util.Calendar;

public class TransactionDTOFixture {
    public static TransactionDTO buyCarTransaction() {
        final var transaction = new TransactionDTO();
        transaction.setId(1L);
        transaction.setTitle("Buy Car after edit");
        transaction.setAccountId(AccountEntityFixture.millennium().getId());
        transaction.setValue(-2400.0);
        transaction.setType("Expense");
        transaction.setDate(Utils.getDate(2022, 6, 23, 14, 23));
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
        transaction.setDate(Utils.getDate(2023, Calendar.JANUARY, 19, 0, 0));
        transaction.setPayeeId(PayeeEntityFixture.MariuszTransKomis().getId());
        transaction.setCategoryId(CategoryEntityFixture.getTransport().getId());

        return transaction;
    }

    public static TransactionDTO getTaxiTransactionWithSubcategory() {
        final var transaction = new TransactionDTO();
        transaction.setId(25L);
        transaction.setTitle("Get Taxi");
        transaction.setAccountId(AccountEntityFixture.millennium().getId());
        transaction.setValue(-20.0);
        transaction.setType("EXPENSE");
        transaction.setDate(Utils.getDate(2024, Calendar.FEBRUARY, 24, 22, 13));
        transaction.setPayeeId(PayeeEntityFixture.SuperTaxi().getId());
        transaction.setCategoryId(CategoryEntityFixture.getTransport().getId());
        transaction.setSubcategoryId(CategoryEntityFixture.getTaxi().getId());

        return transaction;
    }

    public static TransactionDTO buyCarTransactionReturn() {
        final var transaction = new TransactionDTO();
        transaction.setId(1L);
        transaction.setTitle("Buy Car after edit");
        transaction.setAccountId(AccountEntityFixture.millennium().getId());
        transaction.setValue(2400.0);
        transaction.setType("Income");
        transaction.setDate(Utils.getDate(2023, 8, 1, 16, 43));
        transaction.setPayeeId(PayeeEntityFixture.MariuszTransKomis().getId());
        transaction.setCategoryId(CategoryEntityFixture.getTransport().getId());

        return transaction;
    }
}
