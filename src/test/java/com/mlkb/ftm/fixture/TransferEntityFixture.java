package com.mlkb.ftm.fixture;

import com.mlkb.ftm.entity.Account;
import com.mlkb.ftm.entity.Transfer;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransferEntityFixture {
    public static Transfer cashDepositTransfer() {
        Account accountFrom = mock(Account.class);
        when(accountFrom.getName()).thenReturn("Millenium");
        Account accountTo = mock(Account.class);
        when(accountTo.getName()).thenReturn("Wallet");

        final var transfer = new Transfer();
        transfer.setId(3L);
        transfer.setTitle("Cash Deposit January");
        transfer.setDate(new GregorianCalendar(2023, Calendar.JANUARY, 5).getTime());
        transfer.setValue(100.0);
        transfer.setAccountFrom(accountFrom);
        transfer.setAccountTo(accountTo);
        return transfer;
    }

    public static Transfer repaymentLoan() {
        final var transfer = new Transfer();
        transfer.setId(4L);
        transfer.setTitle("Repayment Loan");
        transfer.setDate(new GregorianCalendar(2023, Calendar.JANUARY, 17).getTime());
        transfer.setValue(100.0);
        Account accountsFrom = AccountEntityFixture.millennium();
        transfer.setAccountFrom(accountsFrom);
        transfer.setAccountTo(AccountEntityFixture.allegroPay());
        return transfer;
    }
}
