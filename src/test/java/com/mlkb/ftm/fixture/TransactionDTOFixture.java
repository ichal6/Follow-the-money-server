package com.mlkb.ftm.fixture;

import com.mlkb.ftm.modelDTO.TransactionDTO;

public class TransactionDTOFixture {
    public static TransactionDTO buyCarTransaction() {
        final var transaction = new TransactionDTO();
        transaction.setId(1L);
        transaction.setTitle("Buy Car after edit");
        transaction.setAccountId(AccountEntityFixture.millennium().getId());
        transaction.setValue(-2400.0);
        transaction.setType("Expense");

        return transaction;
    }

    public static TransactionDTO buyCarTransactionReturn() {
        final var transaction = new TransactionDTO();
        transaction.setId(1L);
        transaction.setTitle("Buy Car after edit");
        transaction.setAccountId(AccountEntityFixture.millennium().getId());
        transaction.setValue(2400.0);
        transaction.setType("Income");

        return transaction;
    }
}
