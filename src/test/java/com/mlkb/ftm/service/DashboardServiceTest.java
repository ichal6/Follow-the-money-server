package com.mlkb.ftm.service;

import com.mlkb.ftm.ApplicationConfig;
import com.mlkb.ftm.entity.Account;
import com.mlkb.ftm.entity.User;
import com.mlkb.ftm.fixture.AccountEntityFixture;
import com.mlkb.ftm.fixture.TransactionEntityFixture;
import com.mlkb.ftm.fixture.TransferEntityFixture;
import com.mlkb.ftm.modelDTO.DashboardDTO;
import com.mlkb.ftm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withPrecision;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
@SpringBootTest
class DashboardServiceTest {

    private DashboardService dashboardService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Clock clock;

    @BeforeEach
    void setUp() {
        dashboardService = new DashboardService(userRepository, clock);
    }

    @Test
    void should_calculate_correct_last_30_days_value_for_loan_type() {
        // given
        final String email = "user@user.pl";
        final User user = new User();
        Account millennium = AccountEntityFixture.millennium();
        Account allegroPay = AccountEntityFixture.allegroPay();
        
        user.setEmail(email);
        user.setAccounts(Set.of(millennium, allegroPay));

        Instant instant = Instant.now(
                Clock.fixed(
                        Instant.parse("2023-01-23T12:34:56Z"), ZoneOffset.UTC
                )
        );

        // when
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(clock.instant()).thenReturn(instant);
        var transactions = Set.of(TransactionEntityFixture.buyCarTransaction(),
                TransactionEntityFixture.buyMilkTransaction());
        when(millennium.getTransactions()).thenReturn(transactions);
        var transfers = Set.of(TransferEntityFixture.repaymentLoan());
        when(allegroPay.getTransfersTo()).thenReturn(transfers);

        DashboardDTO dashboard = dashboardService.getDashboard(email);

        // then
        assertThat(dashboard.getDifference()).isCloseTo(-2605.0, withPrecision(0.01));
    }

    @Test
    void should_calculate_correct_get_expense_from_last_12_months_for_loan_type() {
        // given
        final String email = "user@user.pl";
        final User user = new User();
        Account millennium = AccountEntityFixture.millennium();
        Account allegroPay = AccountEntityFixture.allegroPay();

        user.setEmail(email);
        user.setAccounts(Set.of(millennium, allegroPay));

        Instant instant = Instant.now(
                Clock.fixed(
                        Instant.parse("2023-01-23T12:34:56Z"), ZoneOffset.UTC
                )
        );

        // when
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(clock.instant()).thenReturn(instant);
        var transactions = Set.of(
                TransactionEntityFixture.buyCarTransaction(),
                TransactionEntityFixture.buyMilkTransaction(),
                TransactionEntityFixture.buyGroceriesTransactionFebruary(),
                TransactionEntityFixture.buyGasTransactionMarch(),
                TransactionEntityFixture.buyClothesTransactionApril(),
                TransactionEntityFixture.buyGiftTransactionMay(),
                TransactionEntityFixture.buyPhoneTransactionJune(),
                TransactionEntityFixture.buyRestaurantTransactionJuly(),
                TransactionEntityFixture.buyBooksTransactionAugust(),
                TransactionEntityFixture.buyBookTransactionSeptember2022(),
                TransactionEntityFixture.buyHalloweenCostumeTransactionOctober2022(),
                TransactionEntityFixture.buyConcertTicketTransactionNovember2022(),
                TransactionEntityFixture.buyChristmasGiftTransaction()
        );
        when(millennium.getTransactions()).thenReturn(transactions);
        var transfers = Set.of(
                TransferEntityFixture.repaymentLoan(),
                TransferEntityFixture.repaymentLoanDecember2022(),
                TransferEntityFixture.repaymentLoanNovember2022(),
                TransferEntityFixture.repaymentLoanOctober2022(),
                TransferEntityFixture.repaymentLoanSeptember2022(),
                TransferEntityFixture.repaymentLoanAugust2022(),
                TransferEntityFixture.repaymentLoanJuly2022(),
                TransferEntityFixture.repaymentLoanJune2022(),
                TransferEntityFixture.repaymentLoanMay2022(),
                TransferEntityFixture.repaymentLoanApril2022(),
                TransferEntityFixture.repaymentLoanMarch2022(),
                TransferEntityFixture.repaymentLoanFebruary2022()
        );
        when(allegroPay.getTransfersTo()).thenReturn(transfers);

        DashboardDTO dashboard = dashboardService.getDashboard(email);

        // then
        var expected = Map.ofEntries (
                Map.entry(Month.JANUARY, 2605.0),
                Map.entry(Month.FEBRUARY, 195.0),
                Map.entry(Month.MARCH, 305.0),
                Map.entry(Month.APRIL, 430.0),
                Map.entry(Month.MAY, 480.0),
                Map.entry(Month.JUNE, 1550.0),
                Map.entry(Month.JULY, 685.0),
                Map.entry(Month.AUGUST, 800.0),
                Map.entry(Month.SEPTEMBER, 890.0),
                Map.entry(Month.OCTOBER, 980.0),
                Map.entry(Month.NOVEMBER, 373.0),
                Map.entry(Month.DECEMBER, 252.0)
        );

        assertThat(dashboard.getExpenseFunds()).isEqualTo(expected);
    }
}