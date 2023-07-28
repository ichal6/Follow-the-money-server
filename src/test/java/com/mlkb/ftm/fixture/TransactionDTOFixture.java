package com.mlkb.ftm.fixture;

import com.mlkb.ftm.modelDTO.TransactionDTO;

public class TransactionDTOFixture {
    public static TransactionDTO buyCarTransaction() {
        final var transaction = new TransactionDTO();
        transaction.setId(1L);
        transaction.setTitle("Buy Car after edit");

        return transaction;
    }
}
