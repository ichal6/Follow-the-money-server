package com.mlkb.ftm.fixture;

import com.mlkb.ftm.entity.Account;
import com.mlkb.ftm.entity.Transfer;

import java.util.Calendar;
import java.util.GregorianCalendar;

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

    public static Transfer repaymentLoanDecember2022() {
        final var transfer = new Transfer();
        transfer.setId(5L);
        transfer.setTitle("Repayment Loan");
        transfer.setDate(new GregorianCalendar(2022, Calendar.DECEMBER, 17).getTime());
        transfer.setValue(202.0);
        Account accountsFrom = AccountEntityFixture.millennium();
        transfer.setAccountFrom(accountsFrom);
        transfer.setAccountTo(AccountEntityFixture.allegroPay());
        return transfer;
    }

    public static Transfer repaymentLoanNovember2022() {
        final var transfer = new Transfer();
        transfer.setId(6L);
        transfer.setTitle("Repayment Loan");
        transfer.setDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 17).getTime());
        transfer.setValue(303.0);
        Account accountsFrom = AccountEntityFixture.millennium();
        transfer.setAccountFrom(accountsFrom);
        transfer.setAccountTo(AccountEntityFixture.allegroPay());
        return transfer;
    }

    public static Transfer repaymentLoanFebruary2022() {
        final var transfer = new Transfer();
        transfer.setId(7L);
        transfer.setTitle("Repayment Loan");
        transfer.setDate(new GregorianCalendar(2022, Calendar.FEBRUARY, 10).getTime());
        transfer.setValue(150.0);
        Account accountsFrom = AccountEntityFixture.millennium();
        transfer.setAccountFrom(accountsFrom);
        transfer.setAccountTo(AccountEntityFixture.allegroPay());
        return transfer;
    }

    public static Transfer repaymentLoanMarch2022() {
        final var transfer = new Transfer();
        transfer.setId(8L);
        transfer.setTitle("Repayment Loan");
        transfer.setDate(new GregorianCalendar(2022, Calendar.MARCH, 15).getTime());
        transfer.setValue(250.0);
        Account accountsFrom = AccountEntityFixture.millennium();
        transfer.setAccountFrom(accountsFrom);
        transfer.setAccountTo(AccountEntityFixture.allegroPay());
        return transfer;
    }

    public static Transfer repaymentLoanApril2022() {
        final var transfer = new Transfer();
        transfer.setId(9L);
        transfer.setTitle("Repayment Loan");
        transfer.setDate(new GregorianCalendar(2022, Calendar.APRIL, 20).getTime());
        transfer.setValue(350.0);
        Account accountsFrom = AccountEntityFixture.millennium();
        transfer.setAccountFrom(accountsFrom);
        transfer.setAccountTo(AccountEntityFixture.allegroPay());
        return transfer;
    }

    public static Transfer repaymentLoanMay2022() {
        final var transfer = new Transfer();
        transfer.setId(10L);
        transfer.setTitle("Repayment Loan");
        transfer.setDate(new GregorianCalendar(2022, Calendar.MAY, 5).getTime());
        transfer.setValue(450.0);
        Account accountsFrom = AccountEntityFixture.millennium();
        transfer.setAccountFrom(accountsFrom);
        transfer.setAccountTo(AccountEntityFixture.allegroPay());
        return transfer;
    }

    public static Transfer repaymentLoanJune2022() {
        final var transfer = new Transfer();
        transfer.setId(11L);
        transfer.setTitle("Repayment Loan");
        transfer.setDate(new GregorianCalendar(2022, Calendar.JUNE, 12).getTime());
        transfer.setValue(550.0);
        Account accountsFrom = AccountEntityFixture.millennium();
        transfer.setAccountFrom(accountsFrom);
        transfer.setAccountTo(AccountEntityFixture.allegroPay());
        return transfer;
    }

    public static Transfer repaymentLoanJuly2022() {
        final var transfer = new Transfer();
        transfer.setId(12L);
        transfer.setTitle("Repayment Loan");
        transfer.setDate(new GregorianCalendar(2022, Calendar.JULY, 18).getTime());
        transfer.setValue(650.0);
        Account accountsFrom = AccountEntityFixture.millennium();
        transfer.setAccountFrom(accountsFrom);
        transfer.setAccountTo(AccountEntityFixture.allegroPay());
        return transfer;
    }

    public static Transfer repaymentLoanAugust2022() {
        final var transfer = new Transfer();
        transfer.setId(13L);
        transfer.setTitle("Repayment Loan");
        transfer.setDate(new GregorianCalendar(2022, Calendar.AUGUST, 23).getTime());
        transfer.setValue(750.0);
        Account accountsFrom = AccountEntityFixture.millennium();
        transfer.setAccountFrom(accountsFrom);
        transfer.setAccountTo(AccountEntityFixture.allegroPay());
        return transfer;
    }

    public static Transfer repaymentLoanSeptember2022() {
        final var transfer = new Transfer();
        transfer.setId(14L);
        transfer.setTitle("Repayment Loan");
        transfer.setDate(new GregorianCalendar(2022, Calendar.SEPTEMBER, 27).getTime());
        transfer.setValue(850.0);
        Account accountsFrom = AccountEntityFixture.millennium();
        transfer.setAccountFrom(accountsFrom);
        transfer.setAccountTo(AccountEntityFixture.allegroPay());
        return transfer;
    }

    public static Transfer repaymentLoanOctober2022() {
        final var transfer = new Transfer();
        transfer.setId(15L);
        transfer.setTitle("Repayment Loan");
        transfer.setDate(new GregorianCalendar(2022, Calendar.OCTOBER, 13).getTime());
        transfer.setValue(950.0);
        Account accountsFrom = AccountEntityFixture.millennium();
        transfer.setAccountFrom(accountsFrom);
        transfer.setAccountTo(AccountEntityFixture.allegroPay());
        return transfer;
    }

}
