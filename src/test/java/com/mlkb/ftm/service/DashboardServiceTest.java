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
import java.time.ZoneOffset;
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
}