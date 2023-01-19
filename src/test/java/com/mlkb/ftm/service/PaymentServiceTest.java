package com.mlkb.ftm.service;

import com.mlkb.ftm.ApplicationConfig;
import com.mlkb.ftm.entity.Account;
import com.mlkb.ftm.entity.Transaction;
import com.mlkb.ftm.entity.User;
import com.mlkb.ftm.fixture.PaymentDTOFixture;
import com.mlkb.ftm.fixture.TransactionEntityFixture;
import com.mlkb.ftm.fixture.TransferEntityFixture;
import com.mlkb.ftm.repository.*;
import com.mlkb.ftm.validation.InputValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
@SpringBootTest
public class PaymentServiceTest {
    private PaymentService paymentService;

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
        paymentService = new PaymentService(userRepository, inputValidator, transactionRepository, accountRepository, categoryRepository, payeeRepository, transferRepository);
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
        assertThat(payments)
                .hasSize(4)
                .containsExactlyInAnyOrder(
                        PaymentDTOFixture.buyCarTransaction(),
                        PaymentDTOFixture.cashDepositTransferMillenium(),
                        PaymentDTOFixture.cashDepositTransWallet(),
                        PaymentDTOFixture.buyMilkTransaction()
                );
    }
}
