package com.mlkb.ftm.fixture;

import com.mlkb.ftm.entity.Account;
import com.mlkb.ftm.entity.AccountType;

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
        when(account.getIsEnabled()).thenReturn(true);
        when(account.getTransfersFrom()).thenReturn(Set.of());

        return account;
    }

    public static Account myWallet() {
        Account account = mock(Account.class);
        when(account.getName()).thenReturn("My Wallet");
        when(account.getId()).thenReturn(2L);
        when(account.getIsEnabled()).thenReturn(true);
        when(account.getAccountType()).thenReturn(AccountType.CASH);
        when(account.getTransactions()).thenReturn(Set.of());
        when(account.getTransfersFrom()).thenReturn(Set.of());
        when(account.getTransfersTo()).thenReturn(Set.of());

        return account;
    }

    public static Account getPekaoBank() {
        Account account= new Account();
        account.setName("Pekao");
        account.setId(3L);

        return account;
    }

    public static Account getSavingInSock() {
        Account account = new Account();
        account.setName("Savings in sock");
        account.setId(4L);

        return account;
    }

    public static Account allegroPay() {
        Account account = mock(Account.class);
        when(account.getName()).thenReturn("Allegro Pay");
        when(account.getId()).thenReturn(10L);
        when(account.getAccountType()).thenReturn(AccountType.LOAN);
        when(account.getCurrentBalance()).thenReturn(0.0);
        when(account.getTransfersTo()).thenReturn(Set.of());
        when(account.getTransactions()).thenReturn(Set.of());
        when(account.getIsEnabled()).thenReturn(true);
        when(account.getTransfersFrom()).thenReturn(Set.of());

        return account;
    }

    public static Account getDisabledAccount() {
        Account account = mock(Account.class);
        when(account.getName()).thenReturn("Disabled");
        when(account.getId()).thenReturn(11L);
        when(account.getIsEnabled()).thenReturn(false);
        when(account.getAccountType()).thenReturn(AccountType.BANK);
        when(account.getTransactions()).thenReturn(Set.of());

        return account;
    }
}
