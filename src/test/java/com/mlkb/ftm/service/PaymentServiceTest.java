package com.mlkb.ftm.service;

import com.mlkb.ftm.ApplicationConfig;
import com.mlkb.ftm.entity.Account;
import com.mlkb.ftm.entity.Transaction;
import com.mlkb.ftm.entity.User;
import com.mlkb.ftm.exception.ResourceNotFoundException;
import com.mlkb.ftm.fixture.PaymentDTOFixture;
import com.mlkb.ftm.fixture.TransactionEntityFixture;
import com.mlkb.ftm.fixture.TransferEntityFixture;
import com.mlkb.ftm.modelDTO.PaymentDTO;
import com.mlkb.ftm.repository.*;
import com.mlkb.ftm.validation.InputValidator;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
@SpringBootTest
public class PaymentServiceTest {
    private PaymentService paymentService;
    @Autowired
    private Clock clock;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InputValidator inputValidator;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PayeeRepository payeeRepository;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService(userRepository, inputValidator, transactionRepository, accountRepository, categoryRepository, payeeRepository, transferRepository, clock);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void should_decrease_current_balance_when_delete_transaction() {
        //given:
        double transactionValue = 12.0;
        double currentBalance = 25.0;
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setValue(transactionValue);
        Account account = new Account();
        account.setTransactions(Collections.singleton(transaction));
        account.setCurrentBalance(currentBalance);
        String email = "user@user.pl";
        User user = new User();
        user.setAccounts(Collections.singleton(account));

        // when
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        paymentService.removeTransaction(transaction.getId(), email);

        // then
        assertEquals(currentBalance - transactionValue, account.getCurrentBalance());
    }

    @Test
    void should_get_payments_with_no_parameters() {
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
        final var transfers = List.of(TransferEntityFixture.cashDepositTransfer());
        final var buyCarTransactionAsSet = Set.of(TransactionEntityFixture.buyCarTransaction());
        final var cashDepositTransferMilleniumAsSet = Set.of(TransferEntityFixture.cashDepositTransfer());
        final var buyMilkTransactionAsSet = Set.of(TransactionEntityFixture.buyMilkTransaction());

        // when
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(transactionRepository.findAll()).thenReturn(transactions);
        when(transferRepository.findAll()).thenReturn(transfers);
        when(millenium.getName()).thenReturn("Millenium");
        when(millenium.getTransactions()).thenReturn(buyCarTransactionAsSet);
        when(millenium.getTransfersFrom()).thenReturn(cashDepositTransferMilleniumAsSet);
        when(wallet.getName()).thenReturn("Wallet");
        when(wallet.getTransactions()).thenReturn(buyMilkTransactionAsSet);
        when(wallet.getTransfersTo()).thenReturn(cashDepositTransferMilleniumAsSet);

        final var payments = paymentService.getPayments(email);

        // then
        Optional<PaymentDTO> optionalPaymentDTO = payments
                .stream()
                .filter(PaymentDTO::getIsInternal)
                .filter(paymentDTO -> paymentDTO.getValue() < 0)
                .findFirst();

        assertThat(payments)
                .hasSize(3)
                .containsExactlyInAnyOrder(
                        PaymentDTOFixture.buyCarTransaction(),
                        PaymentDTOFixture.cashDepositTransferMillenium(),
                        PaymentDTOFixture.buyMilkTransaction()
                );

        assertThat(optionalPaymentDTO)
                .isEmpty();
    }

    @Test
    void should_throw_exception_if_user_is_not_found_for_get_all_payments(){
        // given
        final String email = "user@user.pl";
        final User user = new User();
        user.setEmail(email);

        // when/then
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        final var exception = assertThrows(
                ResourceNotFoundException.class,
                () -> paymentService.getPayments(email)
        );

        assertThat(exception.getMessage())
                .isEqualTo(String.format("Couldn't find user with email %s", email));

    }

    @Test
    void should_get_payments_with_account_parameter() {
        // given
        final String email = "user@user.pl";
        final Long accountId = 1L;
        final User user = new User();
        Account millenium = mock(Account.class);
        Account wallet = mock(Account.class);
        user.setEmail(email);
        user.setAccounts(Set.of(millenium, wallet));

        final var transactions = List.of(
                TransactionEntityFixture.buyCarTransaction(),
                TransactionEntityFixture.buyMilkTransaction()
        );
        final var transfers = List.of(TransferEntityFixture.cashDepositTransfer());
        final var buyCarTransactionAsSet = Set.of(TransactionEntityFixture.buyCarTransaction());
        final var cashDepositTransferMilleniumAsSet = Set.of(TransferEntityFixture.cashDepositTransfer());
        final var buyMilkTransactionAsSet = Set.of(TransactionEntityFixture.buyMilkTransaction());

        // when
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(transactionRepository.findAll()).thenReturn(transactions);
        when(transferRepository.findAll()).thenReturn(transfers);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(millenium));
        when(millenium.getName()).thenReturn("Millenium");
        when(millenium.getId()).thenReturn(accountId);
        when(millenium.getTransactions()).thenReturn(buyCarTransactionAsSet);
        when(millenium.getTransfersFrom()).thenReturn(cashDepositTransferMilleniumAsSet);
        when(wallet.getName()).thenReturn("Wallet");
        when(wallet.getTransactions()).thenReturn(buyMilkTransactionAsSet);
        when(wallet.getTransfersTo()).thenReturn(cashDepositTransferMilleniumAsSet);

        final var payments = paymentService.getPaymentsWithAccount(email, "1");

        // then
        assertThat(payments)
                .hasSize(2)
                .containsExactlyInAnyOrder(
                        PaymentDTOFixture.buyCarTransactionWithBalance(),
                        PaymentDTOFixture.cashDepositTransferMilleniumWithBalance()
                );
    }

    @Test
    void should_get_payments_with_period_parameter() {
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
        final var transfers = List.of(TransferEntityFixture.cashDepositTransfer());
        final var buyCarTransactionAsSet = Set.of(TransactionEntityFixture.buyCarTransaction());
        final var cashDepositTransferMilleniumAsSet = Set.of(TransferEntityFixture.cashDepositTransfer());
        final var buyMilkTransactionAsSet = Set.of(TransactionEntityFixture.buyMilkTransaction());

        Instant instant = Instant.now(
                Clock.fixed(
                        Instant.parse("2023-01-23T12:34:56Z"), ZoneOffset.UTC
                )
        );

        // when
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(transactionRepository.findAll()).thenReturn(transactions);
        when(transferRepository.findAll()).thenReturn(transfers);
        when(millenium.getName()).thenReturn("Millenium");
        when(millenium.getTransactions()).thenReturn(buyCarTransactionAsSet);
        when(millenium.getTransfersFrom()).thenReturn(cashDepositTransferMilleniumAsSet);
        when(wallet.getName()).thenReturn("Wallet");
        when(wallet.getTransactions()).thenReturn(buyMilkTransactionAsSet);
        when(wallet.getTransfersTo()).thenReturn(cashDepositTransferMilleniumAsSet);
        when(clock.instant()).thenReturn(instant);

        final var payments = paymentService.getPaymentsForPeriod(email, "365");

        // then
        assertThat(payments)
                .hasSize(3)
                .containsExactlyInAnyOrder(
                        PaymentDTOFixture.buyCarTransaction(),
                        PaymentDTOFixture.cashDepositTransferMillenium(),
                        PaymentDTOFixture.buyMilkTransaction()
                );
    }

    @Test
    void should_return_exception_if_user_does_not_exists(){
        //given
        String email = "NoOne@example.com";
        //when
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        ResourceNotFoundException thrown = Assertions.assertThrows(ResourceNotFoundException.class, () ->
                this.paymentService.removeTransaction(1L, email));

        // then
        assertEquals("User for this email does not exist", thrown.getMessage());
    }
}

