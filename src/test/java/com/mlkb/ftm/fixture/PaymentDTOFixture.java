package com.mlkb.ftm.fixture;

import com.mlkb.ftm.modelDTO.PaymentDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class PaymentDTOFixture {
    public static PaymentDTO buyCarTransaction() {
        final var transaction = new PaymentDTO();
        transaction.setId(1L);
        transaction.setTitle("Buy Car");
        transaction.setDate(new GregorianCalendar(2023, Calendar.JANUARY, 19).getTime());
        transaction.setValue(-2500.0);
        transaction.setTo("Złomex");
        transaction.setFrom("Millenium");
        transaction.setCategoryName("Car");
        transaction.setIsInternal(false);
        return transaction;
    }

    public static PaymentDTO buyCarTransactionWithBalance() {
        final var transaction = new PaymentDTO();
        transaction.setId(1L);
        transaction.setTitle("Buy Car");
        transaction.setDate(new GregorianCalendar(2023, Calendar.JANUARY, 19).getTime());
        transaction.setValue(-2500.0);
        transaction.setTo("Złomex");
        transaction.setFrom("Millenium");
        transaction.setCategoryName("Car");
        transaction.setIsInternal(false);
        transaction.setBalanceAfter(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.HALF_UP));
        return transaction;
    }

    public static PaymentDTO buyMilkTransaction() {
        final var transaction = new PaymentDTO();
        transaction.setId(2L);
        transaction.setTitle("Buy Milk");
        transaction.setDate(new GregorianCalendar(2023, Calendar.JANUARY, 16).getTime());
        transaction.setValue(-5.0);
        transaction.setTo("Biedronka");
        transaction.setFrom("Wallet");
        transaction.setCategoryName("Daily");
        transaction.setIsInternal(false);
        return transaction;
    }

    public static PaymentDTO cashDepositTransferMillenium() {
        final var transfer = new PaymentDTO();
        transfer.setId(3L);
        transfer.setTitle("Cash Deposit January");
        transfer.setDate(new GregorianCalendar(2023, Calendar.JANUARY, 5).getTime());
        transfer.setValue(100.0);
        transfer.setTo("Wallet");
        transfer.setFrom("Millenium");
        transfer.setIsInternal(true);
        return transfer;
    }

    public static PaymentDTO cashDepositTransferMilleniumWithBalance() {
        final var transfer = new PaymentDTO();
        transfer.setId(3L);
        transfer.setTitle("Cash Deposit January");
        transfer.setDate(new GregorianCalendar(2023, Calendar.JANUARY, 5).getTime());
        transfer.setValue(-100.0);
        transfer.setTo("Wallet");
        transfer.setFrom("Millenium");
        transfer.setIsInternal(true);
        transfer.setBalanceAfter(BigDecimal.valueOf(2500.0).setScale(2, RoundingMode.HALF_UP));
        return transfer;
    }
}
