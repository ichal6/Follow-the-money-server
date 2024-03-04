package com.mlkb.ftm.service;

import com.mlkb.ftm.common.ApplicationConfig;
import com.mlkb.ftm.entity.Account;
import com.mlkb.ftm.entity.Transaction;
import com.mlkb.ftm.entity.Transfer;
import com.mlkb.ftm.entity.User;
import com.mlkb.ftm.exception.InputValidationMessage;
import com.mlkb.ftm.exception.ResourceNotFoundException;
import com.mlkb.ftm.fixture.*;
import com.mlkb.ftm.modelDTO.PaymentDTO;
import com.mlkb.ftm.modelDTO.TransactionDTO;
import com.mlkb.ftm.modelDTO.TransferDTO;
import com.mlkb.ftm.repository.*;
import com.mlkb.ftm.validation.InputValidator;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
@SpringBootTest
public class PaymentServiceTest {
    private PaymentService paymentService;
    @MockBean
    private Clock clock;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private InputValidator inputValidator;
    @MockBean
    private TransactionRepository transactionRepository;
    @MockBean
    private TransferRepository transferRepository;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private CategoryRepository categoryRepository;
    @MockBean
    private PayeeRepository payeeRepository;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService(userRepository, inputValidator, transactionRepository, accountRepository, categoryRepository, payeeRepository, transferRepository, clock);
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
                .hasSize(3)
                .containsExactlyInAnyOrder(
                        PaymentDTOFixture.buyCarTransaction(),
                        PaymentDTOFixture.cashDepositTransferMillenium(),
                        PaymentDTOFixture.buyMilkTransaction()
                );

        Optional<PaymentDTO> optionalPaymentDTO = payments
                .stream()
                .filter(PaymentDTO::getIsInternal)
                .filter(paymentDTO -> paymentDTO.getValue() < 0)
                .findFirst();

        assertThat(optionalPaymentDTO)
                .isEmpty();

        var possibleWrongBalance = payments.stream()
                .map(PaymentDTO::getBalanceAfter)
                .filter(Objects::nonNull)
                .findAny();

        assertThat(possibleWrongBalance)
                .isEmpty();
    }

    @Test
    void should_get_payments_with_no_parameters_with_subcategories() {
        // arrange
        final String email = "user@user.pl";
        final var transactions = List.of(
                TransactionEntityFixture.getTaxiTransaction(),
                TransactionEntityFixture.buyMilkTransaction()
        );

        initializeMocksForTestTransactionsWithSubcategories(transactions);

        // act
        final var payments = paymentService.getPayments(email);

        // assert
        expectedContainTwoTransactions(payments);
        exceptedContainTransactionWithSubcategories(payments);
    }

    private void initializeMocksForTestTransactionsWithSubcategories(List<Transaction> transactions) {
        final var getTaxiTransactionAsSet = Set.of(TransactionEntityFixture.getTaxiTransaction());
        final var buyMilkTransactionAsSet = Set.of(TransactionEntityFixture.buyMilkTransaction());
        final User user = mock(User.class);
        final Account millenium = mock(Account.class);
        final Account wallet = mock(Account.class);

        when(user.getAccounts()).thenReturn(Set.of(millenium, wallet));
        when(userRepository.findByEmail("user@user.pl")).thenReturn(Optional.of(user));
        when(transactionRepository.findAll()).thenReturn(transactions);
        when(millenium.getName()).thenReturn("Millenium");
        when(millenium.getTransactions()).thenReturn(getTaxiTransactionAsSet);
        when(wallet.getName()).thenReturn("Wallet");
        when(wallet.getTransactions()).thenReturn(buyMilkTransactionAsSet);
    }

    private static void exceptedContainTransactionWithSubcategories(List<PaymentDTO> payments) {
        assertThat(payments.stream()
                .filter( p -> p.getSubcategoryName() != null)
                .findAny())
                .hasValue(PaymentDTOFixture.getTaxiTransactionWithSubcategories());
    }

    private static void expectedContainTwoTransactions(List<PaymentDTO> payments) {
        assertThat(payments)
                .hasSize(2)
                .containsExactlyInAnyOrder(
                        PaymentDTOFixture.buyMilkTransaction(),
                        PaymentDTOFixture.getTaxiTransactionWithSubcategories()
                );
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

        Optional<Integer> possibleWrongScale = payments.stream()
                .map(p -> p.getBalanceAfter().scale())
                .filter(s -> s != 2)
                .findAny();

        assertThat(possibleWrongScale).isEmpty();

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

        var possibleWrongBalance = payments.stream()
                .map(PaymentDTO::getBalanceAfter)
                .filter(Objects::nonNull)
                .findAny();

        assertThat(possibleWrongBalance)
                .isEmpty();
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

    @Test
    void should_throw_exception_if_transaction_does_not_exist_user_when_try_update() {
        // given
        var transactionDto = TransactionDTOFixture.buyCarTransaction();
        var account = AccountEntityFixture.allegroPay();
        var payee = PayeeEntityFixture.MariuszTransKomis();
        var category = CategoryEntityFixture.getTransport();
        String email = "user@user.pl";
        // when
        when(transactionRepository.existsByTransactionIdAndUserEmail(transactionDto.getId(), email)).thenReturn(false);
        when(accountRepository.findByAccountIdAndUserEmail(anyLong(), anyString()))
                .thenReturn(Optional.of(account));
        when(payeeRepository.findByPayeeIdAndUserEmail(anyLong(), anyString()))
                .thenReturn(Optional.of(payee));
        when(categoryRepository.findByCategoryIdAndUserEmail(anyLong(), anyString()))
                .thenReturn(Optional.of(category));
        ResourceNotFoundException thrown = Assertions.assertThrows(ResourceNotFoundException.class, () ->
                this.paymentService.updateTransaction(transactionDto, email));

        // then
        assertEquals(
                String.format("Transaction for id = %d does not exist", transactionDto.getId()),
                thrown.getMessage());
    }

    @Test
    void should_throw_exception_if_transaction_belonging_to_other_user_when_try_update() {
        // given
        var transactionDto = TransactionDTOFixture.buyCarTransaction();
        String email = "user@user.pl";
        // when
        when(transactionRepository.existsByTransactionIdAndUserEmail(transactionDto.getId(), email)).thenReturn(false);
        when(accountRepository.findByAccountIdAndUserEmail(anyLong(), anyString()))
                .thenReturn(Optional.empty());
        ResourceNotFoundException thrown = Assertions.assertThrows(ResourceNotFoundException.class, () ->
                this.paymentService.updateTransaction(transactionDto, email));

        // then
        assertEquals(
                String.format("Account for id = %d doesn't exist",
                        transactionDto.getAccountId()),
                thrown.getMessage());
    }

    @Test
    void should_throw_exception_if_account_to_belonging_to_other_user_when_try_update_transfer() {
        // given
        var transferDTO = TransferDTOFixture.cashDepositTransferMillennium();
        String email = "user@user.pl";
        // when
        when(transferRepository.existsByTransferIdAndUserEmail(transferDTO.getId(), email)).thenReturn(true);
        when(accountRepository.findByAccountIdAndUserEmail(transferDTO.getAccountIdTo(), email))
                .thenReturn(Optional.empty());
        when(accountRepository.findByAccountIdAndUserEmail(transferDTO.getAccountIdFrom(), email))
                .thenReturn(Optional.of(new Account()));
        ResourceNotFoundException thrown = Assertions.assertThrows(ResourceNotFoundException.class, () ->
                this.paymentService.updateTransfer(transferDTO, email));

        // then
        assertEquals(
                String.format("Account for id = %d doesn't exist",
                        transferDTO.getAccountIdTo()),
                thrown.getMessage());
    }

    @Test
    void should_throw_exception_if_account_from_belonging_to_other_user_when_try_update_transfer() {
        // given
        var transferDTO = TransferDTOFixture.cashDepositTransferMillennium();
        String email = "user@user.pl";
        // when
        when(transferRepository.existsByTransferIdAndUserEmail(transferDTO.getId(), email)).thenReturn(true);
        when(accountRepository.findByAccountIdAndUserEmail(transferDTO.getAccountIdTo(), email))
                .thenReturn(Optional.of(new Account()));
        when(accountRepository.findByAccountIdAndUserEmail(transferDTO.getAccountIdFrom(), email))
                .thenReturn(Optional.empty());
        ResourceNotFoundException thrown = Assertions.assertThrows(ResourceNotFoundException.class, () ->
                this.paymentService.updateTransfer(transferDTO, email));

        // then
        assertEquals(
                String.format("Account for id = %d doesn't exist",
                        transferDTO.getAccountIdFrom()),
                thrown.getMessage());
    }

    @Test
    void should_throw_exception_if_transfer_does_not_exist_user_when_try_update() {
        // given
        var transferDTO = TransferDTOFixture.cashDepositTransferMillennium();
        String email = "user@user.pl";
        // when
        when(transferRepository.existsByTransferIdAndUserEmail(transferDTO.getId(), email)).thenReturn(false);

        ResourceNotFoundException thrown = Assertions.assertThrows(ResourceNotFoundException.class, () ->
                this.paymentService.updateTransfer(transferDTO, email));

        // then
        assertEquals(
                String.format("Transfer for id = %d does not exist", transferDTO.getId()),
                thrown.getMessage());
    }

    @Test
    void should_throw_exception_if_transfer_contain_the_same_accounts() {
        // given
        var transferDTO = TransferDTOFixture.cashDepositTransferMillennium();
        transferDTO.setAccountIdTo(1L);
        transferDTO.setAccountIdFrom(1L);
        String email = "user@user.pl";
        // when
        when(transferRepository.existsByTransferIdAndUserEmail(transferDTO.getId(), email)).thenReturn(true);

        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () ->
                this.paymentService.updateTransfer(transferDTO, email));

        // then
        assertEquals(InputValidationMessage.TRANSFER_ACCOUNTS_ID.message, thrown.getMessage());
    }

    @Test
    void should_throw_exception_if_user_email_or_transaction_id_is_wrong_when_try_get_single_transaction() {
        // given
        String email = "wrong@email.com";
        long id = 1L;

        // when
        when(transactionRepository.existsByTransactionIdAndUserEmail(id, email)).thenReturn(false);
        ResourceNotFoundException thrown = Assertions.assertThrows(ResourceNotFoundException.class, () ->
                this.paymentService.getTransaction(email, id));

        // then
        assertEquals(
                String.format("Transaction for id = %d does not exist", id),
                thrown.getMessage());
    }

    @Test
    void should_return_transactionDTO_when_try_get_single_transaction() {
        // given
        String email = "correct@email.com";
        long id = 1L;
        Transaction transactionEntity = TransactionEntityFixture.buyCarTransaction();
        TransactionDTO expectedDto = TransactionDTOFixture.buyCarTransactionBeforeUpdate();

        // when
        when(transactionRepository.existsByTransactionIdAndUserEmail(id, email)).thenReturn(true);
        when(transactionRepository.findById(id)).thenReturn(Optional.of(transactionEntity));

        TransactionDTO transactionDTO = this.paymentService.getTransaction(email, id);

        // then
        assertEquals(expectedDto, transactionDTO);
    }

    @Test
    void should_return_transactionDTO_when_try_get_single_transaction_with_subcategory() {
        // given
        String email = "correct@email.com";
        Transaction transactionEntity = TransactionEntityFixture.getTaxiTransaction();
        TransactionDTO expectedDto = TransactionDTOFixture.getTaxiTransactionWithSubcategory();
        long transactionId = transactionEntity.getId();

        // when
        when(transactionRepository.existsByTransactionIdAndUserEmail(transactionId, email)).thenReturn(true);
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transactionEntity));

        TransactionDTO actualDto = this.paymentService.getTransaction(email, transactionId);

        // then
        assertEquals(expectedDto, actualDto);
    }

    @Test
    void should_throw_exception_if_user_email_or_transfer_id_is_wrong_when_try_get_single_transfer() {
        // given
        String email = "wrong@email.com";
        long id = 1L;

        // when
        when(transferRepository.existsByTransferIdAndUserEmail(id, email)).thenReturn(false);
        ResourceNotFoundException thrown = Assertions.assertThrows(ResourceNotFoundException.class, () ->
                this.paymentService.getTransfer(email, id));

        // then
        assertEquals(
                String.format("Transfer for id = %d does not exist", id),
                thrown.getMessage());
    }

    @Test
    void should_return_transferDTO_when_try_get_single_transfer() {
        // given
        String email = "correct@email.com";
        long id = 1L;
        Transfer transferEntity = TransferEntityFixture.cashDepositTransfer();
        TransferDTO expectedDto = TransferDTOFixture.cashDepositTransferMillennium();

        // when
        when(transferRepository.existsByTransferIdAndUserEmail(id, email)).thenReturn(true);
        when(transferRepository.findById(id)).thenReturn(Optional.of(transferEntity));

        TransferDTO transferDTO = this.paymentService.getTransfer(email, id);

        // then
        assertEquals(expectedDto, transferDTO);
    }
}
