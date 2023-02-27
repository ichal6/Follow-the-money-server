package com.mlkb.ftm.service;

import com.mlkb.ftm.ApplicationConfig;
import com.mlkb.ftm.entity.Account;
import com.mlkb.ftm.entity.User;
import com.mlkb.ftm.exception.ResourceNotFoundException;
import com.mlkb.ftm.fixture.AnalysisFinancialTableDTOFixture;
import com.mlkb.ftm.fixture.TransactionEntityFixture;
import com.mlkb.ftm.repository.TransactionRepository;
import com.mlkb.ftm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
@SpringBootTest
class AnalysisServiceTest {
    private AnalysisService analysisService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        analysisService = new AnalysisService(userRepository);
    }

    @Test
    void should_return_set_of_AnalysisFinancialTableDTO_for_correct_credentials() {
        // given
        final String email = "user@user.pl";
        final User user = new User();
        Account millenium = mock(Account.class);
        Account wallet = mock(Account.class);
        user.setEmail(email);
        user.setAccounts(Set.of(millenium, wallet));
        final var transactions = List.of(
                TransactionEntityFixture.buyCarTransaction(),
                TransactionEntityFixture.buyMilkTransaction()
        );
        final var walletTransaction = Set.of(TransactionEntityFixture.buyCarTransaction(),
                TransactionEntityFixture.billiardTransaction(), TransactionEntityFixture.buySugarTransaction());
        final var bankTransaction = Set.of(TransactionEntityFixture.buyMilkTransaction(),
                TransactionEntityFixture.abonamentAWSTransaction(), TransactionEntityFixture.salaryTransaction());

        // when
        when(userRepository.existsByEmail(email)).thenReturn(true);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(transactionRepository.findAll()).thenReturn(transactions);
        when(millenium.getName()).thenReturn("Millenium");
        when(millenium.getTransactions()).thenReturn(bankTransaction);
        when(wallet.getName()).thenReturn("Wallet");
        when(wallet.getTransactions()).thenReturn(walletTransaction);

        final var tableData = analysisService.getTableData(email, Instant.ofEpochMilli(0L));

        // then
        assertThat(tableData)
                .hasSize(2)
                .containsExactlyInAnyOrder(
                        AnalysisFinancialTableDTOFixture.walletAnalysisFinancialTableDTO(),
                        AnalysisFinancialTableDTOFixture.bankAnalysisFinancialTableDTO()
                );
    }

    @Test
    void should_throw_exception_for_incorrect_credentials() {
        // given
        final String email = "user@user.pl";

        // when/then
        when(userRepository.existsByEmail(email)).thenReturn(false);
        final var exception = assertThrows(
                ResourceNotFoundException.class,
                () -> analysisService.getTableData(email, Instant.ofEpochMilli(0L))
        );

        assertThat(exception.getMessage())
                .isEqualTo(String.format("Couldn't find a user for email: %s", email));
    }

    @Test
    void should_return_set_of_AnalysisFinancialTableDTO_for_correct_credentials_withDateStart_param() {
        // given
        LocalDate date = LocalDate.parse("2023-01-27");
        final Instant dateStart =  date.atStartOfDay(ZoneId.of("UTC")).toInstant();
        final String email = "user@user.pl";
        final User user = new User();
        Account millenium = mock(Account.class);
        Account wallet = mock(Account.class);
        user.setEmail(email);
        user.setAccounts(Set.of(millenium, wallet));
        final var transactions = List.of(
                TransactionEntityFixture.buyCarTransaction(),
                TransactionEntityFixture.buyMilkTransaction()
        );
        final var walletTransaction = Set.of(TransactionEntityFixture.buyCarTransaction(),
                TransactionEntityFixture.billiardTransaction(), TransactionEntityFixture.buySugarTransaction());
        final var bankTransaction = Set.of(TransactionEntityFixture.buyMilkTransaction(),
                TransactionEntityFixture.abonamentAWSTransaction(), TransactionEntityFixture.salaryTransaction());

        // when
        when(userRepository.existsByEmail(email)).thenReturn(true);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(transactionRepository.findAll()).thenReturn(transactions);
        when(millenium.getName()).thenReturn("Millenium");
        when(millenium.getTransactions()).thenReturn(bankTransaction);
        when(wallet.getName()).thenReturn("Wallet");
        when(wallet.getTransactions()).thenReturn(walletTransaction);

        final var tableData = analysisService.getTableData(email, dateStart);

        // then
        assertThat(tableData)
                .hasSize(2)
                .containsExactlyInAnyOrder(
                        AnalysisFinancialTableDTOFixture.walletAnalysisFinancialTableDTOFromStartDate(),
                        AnalysisFinancialTableDTOFixture.bankAnalysisFinancialTableDTOFromStartDate()
                );
    }
}