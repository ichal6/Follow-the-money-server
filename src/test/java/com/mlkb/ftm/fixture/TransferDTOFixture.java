package com.mlkb.ftm.fixture;

import com.mlkb.ftm.modelDTO.TransferDTO;

import java.util.Calendar;
import java.util.Date;

public class TransferDTOFixture {

    public static TransferDTO cashDepositTransferMillennium() {
        final var transfer = new TransferDTO();
        transfer.setId(3L);
        transfer.setTitle("Cash Deposit January");
        transfer.setDate(getDate());
        transfer.setValue(100.0);
        transfer.setAccountIdFrom(AccountEntityFixture.millennium().getId());
        transfer.setAccountIdTo(AccountEntityFixture.myWallet().getId());

        return transfer;
    }

    public static TransferDTO cashDepositTransferFromSavingsInSockToPekao() {
        final var transfer = new TransferDTO();
        transfer.setId(3L);
        transfer.setTitle("Cash Deposit January");
        transfer.setDate(getDate());
        transfer.setValue(100.0);
        transfer.setAccountIdFrom(AccountEntityFixture.getSavingInSock().getId());
        transfer.setAccountIdTo(AccountEntityFixture.getPekaoBank().getId());

        return transfer;
    }

    private static Date getDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2023);
        cal.set(Calendar.MONTH, 8);
        cal.set(Calendar.DAY_OF_MONTH, 7);
        cal.set(Calendar.HOUR_OF_DAY, 17);
        cal.set(Calendar.MINUTE, 55);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
