package com.mlkb.ftm.fixture;

import com.mlkb.ftm.modelDTO.PaymentDTO;
import com.mlkb.ftm.modelDTO.TransferDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TransferDTOFixture {

    public static TransferDTO cashDepositTransferMillennium() {
        final var transfer = new TransferDTO();
        transfer.setId(3L);
        transfer.setTitle("Cash Deposit January");
        transfer.setDate(getDate(2023, 8, 7, 17, 55));
        transfer.setValue(100.0);
        transfer.setAccountIdFrom(AccountEntityFixture.millennium().getId());
        transfer.setAccountIdTo(AccountEntityFixture.myWallet().getId());

        return transfer;
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
