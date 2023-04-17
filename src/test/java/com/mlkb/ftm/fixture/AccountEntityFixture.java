package com.mlkb.ftm.fixture;

import com.mlkb.ftm.entity.Account;
import com.mlkb.ftm.entity.AccountType;
import com.mlkb.ftm.entity.Transfer;

import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountEntityFixture {
    public static Account millennium() {
        Account account = mock(Account.class);
        when(account.getName()).thenReturn("Millenium");
        when(account.getId()).thenReturn(1L);
        when(account.getAccountType()).thenReturn(AccountType.BANK);
        when(account.getCurrentBalance()).thenReturn(0.0);
        when(account.getTransactions()).thenReturn(Set.of(TransactionEntityFixture.buyCarTransaction(),
                TransactionEntityFixture.buyMilkTransaction()));
        when(account.getIsEnabled()).thenReturn(true);
        when(account.getTransfersFrom()).thenReturn(Set.of());

        return account;
    }

    public static Account allegroPay() {
        Account account = mock(Account.class);
        when(account.getName()).thenReturn("Allegro Pay");
        when(account.getId()).thenReturn(2L);
        when(account.getAccountType()).thenReturn(AccountType.LOAN);
        when(account.getCurrentBalance()).thenReturn(0.0);
        var transfers = Set.of(TransferEntityFixture.repaymentLoan());
        when(account.getTransfersTo()).thenReturn(transfers);
        when(account.getTransactions()).thenReturn(Set.of());
        when(account.getIsEnabled()).thenReturn(true);
        when(account.getTransfersFrom()).thenReturn(Set.of());

        return account;
    }
}
