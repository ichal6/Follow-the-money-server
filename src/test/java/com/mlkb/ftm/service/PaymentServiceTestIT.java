package com.mlkb.ftm.service;

import com.mlkb.ftm.common.IntegrationTest;
import com.mlkb.ftm.entity.Account;
import com.mlkb.ftm.entity.Transaction;
import com.mlkb.ftm.fixture.*;
import com.mlkb.ftm.modelDTO.TransferDTO;
import com.mlkb.ftm.repository.*;
import com.mlkb.ftm.validation.InputValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.sql.*;
import java.time.Clock;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;

@Sql({
        "classpath:/sql/user.sql",
        "classpath:/sql/category.sql",
        "classpath:/sql/account.sql",
        "classpath:/sql/payee.sql",
        "classpath:/sql/transaction.sql",
        "classpath:/sql/transfer.sql"
})
@Sql(value = "classpath:/sql/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PaymentServiceTestIT extends IntegrationTest {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InputValidator inputValidator;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PayeeRepository payeeRepository;
    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private Clock clock;

    @Test
    void should_rename_transaction_if_transaction_exists() throws SQLException {
        // given
        this.paymentService = new PaymentService(
                userRepository,
                inputValidator,
                transactionRepository,
                accountRepository,
                categoryRepository,
                payeeRepository,
                transferRepository,
                clock);

        var transactionDto = TransactionDTOFixture.buyCarTransaction();
        String email = UserEntityFixture.userUserowy().getEmail();
        // when
        paymentService.updateTransaction(transactionDto, email);
        // then
        // Connect to the database
        try (Connection conn = DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword())) {
            // Create a statement to query the database
            try (Statement stmt = conn.createStatement()) {
                // Query the database for the data
                ResultSet rs = stmt.executeQuery(String.format("SELECT title FROM transaction WHERE id = %d", transactionDto.getId()));

                // Check if the data is edited
                assertThat(rs.next()).isTrue();
                assertThat(rs.getString(1)).isEqualTo(transactionDto.getTitle());
            }
        }
    }

    @Test
    void should_rename_transfer_if_transfer_exists() throws SQLException {
        // given
        this.paymentService = new PaymentService(
                userRepository,
                inputValidator,
                transactionRepository,
                accountRepository,
                categoryRepository,
                payeeRepository,
                transferRepository,
                clock);

        var transferDTO = TransferDTOFixture.cashDepositTransferMillennium();
        String email = UserEntityFixture.userUserowy().getEmail();
        // when
        paymentService.updateTransfer(transferDTO, email);
        // then
        // Connect to the database
        try (Connection conn = DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword())) {
            // Create a statement to query the database
            try (Statement stmt = conn.createStatement()) {
                // Query the database for the data
                ResultSet rs = stmt.executeQuery(String.format("SELECT title FROM transfer WHERE id = %d", transferDTO.getId()));

                // Check if the data is edited
                assertThat(rs.next()).isTrue();
                assertThat(rs.getString(1)).isEqualTo(transferDTO.getTitle());
            }
        }
    }

    @Test
    void should_update_value_in_transaction_if_transaction_exists() throws SQLException {
        // given
        this.paymentService = new PaymentService(
                userRepository,
                inputValidator,
                transactionRepository,
                accountRepository,
                categoryRepository,
                payeeRepository,
                transferRepository,
                clock);

        var transactionDto = TransactionDTOFixture.buyCarTransaction();
        String email = UserEntityFixture.userUserowy().getEmail();

        // when
        paymentService.updateTransaction(transactionDto, email);
        // then
        // Connect to the database
        try (Connection conn = DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword())) {
            // Create a statement to query the database
            try (Statement stmt = conn.createStatement()) {
                // Query the database for the data
                ResultSet rs = stmt.executeQuery(String.format("SELECT value FROM transaction WHERE id = %d", transactionDto.getId()));

                // Check if the data is edited
                assertThat(rs.next()).isTrue();
                assertThat(rs.getDouble(1)).isEqualTo(transactionDto.getValue());
            }

            // Create a statement to query the database
            try (Statement stmt = conn.createStatement()) {
                // Query the database for the data
                ResultSet rs = stmt.executeQuery(String.format("SELECT current_balance FROM account WHERE id = %d", transactionDto.getAccountId()));

                // Check if the data is edited
                assertThat(rs.next()).isTrue();
                assertThat(rs.getDouble(1)).isEqualTo(2100.0);
            }
        }
    }
    @Test
    void should_update_value_in_transfer_if_transfer_exists_and_accounts_is_the_same() throws SQLException {
        // given
        this.paymentService = new PaymentService(
                userRepository,
                inputValidator,
                transactionRepository,
                accountRepository,
                categoryRepository,
                payeeRepository,
                transferRepository,
                clock);

        var transferDTO = TransferDTOFixture.cashDepositTransferMillennium();
        String email = UserEntityFixture.userUserowy().getEmail();

        // when
        paymentService.updateTransfer(transferDTO, email);
        // then
        // Connect to the database
        try (Connection conn = DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword())) {
            // Create a statement to query the database
            try (Statement stmt = conn.createStatement()) {
                // Query the database for the data
                ResultSet rs = stmt.executeQuery(String.format("SELECT value FROM transfer WHERE id = %d", transferDTO.getId()));

                // Check if the value is edited
                assertThat(rs.next()).isTrue();
                assertThat(rs.getDouble(1)).isEqualTo(transferDTO.getValue());
            }

            // Create a statement to query the database
            try (Statement stmt = conn.createStatement()) {
                // Query the database for the data
                ResultSet rs = stmt.executeQuery(String.format("SELECT current_balance FROM account WHERE id = %d", transferDTO.getAccountIdFrom()));

                // Check if the account from current balance is edited
                assertThat(rs.next()).isTrue();
                assertThat(rs.getDouble(1)).isEqualTo(2100.0);
            }

            // Create a statement to query the database
            try (Statement stmt = conn.createStatement()) {
                // Query the database for the data
                ResultSet rs = stmt.executeQuery(String.format("SELECT current_balance FROM account WHERE id = %d", transferDTO.getAccountIdTo()));

                // Check if the account to current balance is edited
                assertThat(rs.next()).isTrue();
                assertThat(rs.getDouble(1)).isEqualTo(-1100.0);
            }
        }
    }

    @Test
    void should_update_value_in_transfer_if_transfer_exists_and_accounts_is_not_the_same() throws SQLException {
        // given
        this.paymentService = new PaymentService(
                userRepository,
                inputValidator,
                transactionRepository,
                accountRepository,
                categoryRepository,
                payeeRepository,
                transferRepository,
                clock);

        var oldTransferDto = TransferDTOFixture.cashDepositTransferMillennium();
        var transferDTO = TransferDTOFixture.cashDepositTransferFromSavingsInSockToPekao();
        String email = UserEntityFixture.userUserowy().getEmail();

        // when
        paymentService.updateTransfer(transferDTO, email);
        // then
        // Connect to the database
        try (Connection conn = DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword())) {
            checkIsValueIsEdited(conn, transferDTO);
            checkIsCurrentBalanceIsRestoreToPreviousValueAfterChangeAccountFrom(conn, oldTransferDto);
            checkIsCurrentBalanceIsRestoreToPreviousValueAfterChangeAccountTo(conn, oldTransferDto);
            checkIsCurrentBalanceIsEditedForAccountTo(conn, transferDTO);
            checkIsCurrentBalanceIsEditedForAccountFrom(conn, transferDTO);
            checkIsAccountIdFromIsEdited(conn, transferDTO);
            checkIsAccountIdToIsEdited(conn, transferDTO);
        }
    }

    @Test
    void should_update_value_and_payment_type_in_transaction_if_transaction_exists() throws SQLException {
        // given
        this.paymentService = new PaymentService(
                userRepository,
                inputValidator,
                transactionRepository,
                accountRepository,
                categoryRepository,
                payeeRepository,
                transferRepository,
                clock);

        var transactionDto = TransactionDTOFixture.buyCarTransactionReturn();
        String email = UserEntityFixture.userUserowy().getEmail();

        // when
        paymentService.updateTransaction(transactionDto, email);
        // then
        // Connect to the database
        try (Connection conn = DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword())) {
            // Create a statement to query the database
            try (Statement stmt = conn.createStatement()) {
                // Query the database for the data
                ResultSet rs = stmt.executeQuery(String.format("SELECT value FROM transaction WHERE id = %d", transactionDto.getId()));

                // Check if the data is edited
                assertThat(rs.next()).isTrue();
                assertThat(rs.getDouble(1)).isEqualTo(transactionDto.getValue());
            }

            // Create a statement to query the database
            try (Statement stmt = conn.createStatement()) {
                // Query the database for the data
                ResultSet rs = stmt.executeQuery(String.format("SELECT current_balance FROM account WHERE id = %d", transactionDto.getAccountId()));

                // Check if the data is edited
                assertThat(rs.next()).isTrue();
                assertThat(rs.getDouble(1)).isEqualTo(6900.0);
            }
        }
    }

    @Test
    void should_update_date_in_transaction_if_transaction_exists() throws SQLException {
        // given
        this.paymentService = new PaymentService(
                userRepository,
                inputValidator,
                transactionRepository,
                accountRepository,
                categoryRepository,
                payeeRepository,
                transferRepository,
                clock);

        var transactionDto = TransactionDTOFixture.buyCarTransaction();
        String email = UserEntityFixture.userUserowy().getEmail();

        // when
        paymentService.updateTransaction(transactionDto, email);
        // then
        // Connect to the database
        try (Connection conn = DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword())) {
            // Create a statement to query the database
            try (Statement stmt = conn.createStatement()) {
                // Query the database for the data
                ResultSet rs = stmt.executeQuery(String.format("SELECT date FROM transaction WHERE id = %d", transactionDto.getId()));

                // Check if the data is edited
                assertThat(rs.next()).isTrue();
                Timestamp timestamp = rs.getTimestamp(1);
                if (timestamp != null)
                    assertThat(new java.util.Date(timestamp.getTime())).isEqualTo(transactionDto.getDate());
                else
                    fail("timestamp doesn't exist");
            }
        }
    }

    @Test
    void should_update_date_in_transfer_if_transfer_exists() throws SQLException {
        // given
        this.paymentService = new PaymentService(
                userRepository,
                inputValidator,
                transactionRepository,
                accountRepository,
                categoryRepository,
                payeeRepository,
                transferRepository,
                clock);

        var transferDTO = TransferDTOFixture.cashDepositTransferMillennium();
        String email = UserEntityFixture.userUserowy().getEmail();

        // when
        paymentService.updateTransfer(transferDTO, email);
        // then
        // Connect to the database
        try (Connection conn = DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword())) {
            // Create a statement to query the database
            try (Statement stmt = conn.createStatement()) {
                // Query the database for the data
                ResultSet rs = stmt.executeQuery(String.format("SELECT date FROM transfer WHERE id = %d", transferDTO.getId()));

                // Check if the date is edited
                assertThat(rs.next()).isTrue();
                Timestamp timestamp = rs.getTimestamp(1);
                if (timestamp != null)
                    assertThat(new java.util.Date(timestamp.getTime())).isEqualTo(transferDTO.getDate());
                else
                    fail("timestamp doesn't exist");
            }
        }
    }

    @Test
    void should_update_account_in_transaction_if_transaction_exists() throws SQLException {
        // given
        this.paymentService = new PaymentService(
                userRepository,
                inputValidator,
                transactionRepository,
                accountRepository,
                categoryRepository,
                payeeRepository,
                transferRepository,
                clock);

        var transactionDto = TransactionDTOFixture.buyCarTransaction();
        var oldAccountID = transactionDto.getAccountId();
        transactionDto.setAccountId(AccountEntityFixture.myWallet().getId());
        String email = UserEntityFixture.userUserowy().getEmail();

        // when
        paymentService.updateTransaction(transactionDto, email);
        Optional<Account> oldAccount = accountRepository.findByAccountIdAndUserEmail(oldAccountID, email);
        Optional<Transaction> transactionInOldAccount = oldAccount.orElseThrow().getTransactions()
                .stream()
                .filter(t -> t.getId().equals(transactionDto.getId()))
                .findFirst();

        // then
        // check is transactions was removed from old Account
        assertThat(transactionInOldAccount.isPresent()).isFalse();
        // Connect to the database
        try (Connection conn = DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword())) {
            // Create a statement to query the database for check transaction id has changed
            try (Statement stmt = conn.createStatement()) {
                // Query the database for the data
                ResultSet rs = stmt.executeQuery(String.format("SELECT account_id FROM transaction WHERE id = %d", transactionDto.getId()));

                // Check if the data is edited
                assertThat(rs.next()).isTrue();
                assertThat(rs.getLong(1)).isEqualTo(transactionDto.getAccountId());
            }

            // Create a statement to query the database to check current balance is changed
            try (Statement stmt = conn.createStatement()) {
                // Query the database for the data
                ResultSet rs = stmt.executeQuery(String.format("SELECT current_balance FROM account WHERE id = %d",
                        AccountEntityFixture.millennium().getId()));

                // Check if the data is edited
                assertThat(rs.next()).isTrue();
                assertThat(rs.getDouble(1)).isEqualTo(4500.00);

                rs = stmt.executeQuery(String.format("SELECT current_balance FROM account WHERE id = %d",
                        AccountEntityFixture.myWallet().getId()));

                assertThat(rs.next()).isTrue();
                assertThat(rs.getDouble(1)).isEqualTo(-3400.00);
            }
        }
    }

    private void checkIsValueIsEdited(Connection conn, TransferDTO transferDTO) throws SQLException {
        // Create a statement to query the database
        try (Statement stmt = conn.createStatement()) {
            // Query the database for the data
            ResultSet rs = stmt.executeQuery(String.format("SELECT value FROM transfer WHERE id = %d", transferDTO.getId()));

            // Check if the data is edited
            assertThat(rs.next()).isTrue();
            assertThat(rs.getDouble(1)).isEqualTo(transferDTO.getValue());
        }
    }

    private void checkIsCurrentBalanceIsRestoreToPreviousValueAfterChangeAccountFrom(Connection conn, TransferDTO oldTransferDto) throws SQLException {
        // Create a statement to query the database
        try (Statement stmt = conn.createStatement()) {
            // Query the database for the data
            ResultSet rs = stmt.executeQuery(String.format("SELECT current_balance FROM account WHERE id = %d", oldTransferDto.getAccountIdFrom()));

            // Check if the data is edited
            assertThat(rs.next()).isTrue();
            assertThat(rs.getDouble(1)).isEqualTo(2200.0);
        }
    }

    private void checkIsCurrentBalanceIsRestoreToPreviousValueAfterChangeAccountTo(Connection conn, TransferDTO oldTransferDto) throws SQLException {
        // Create a statement to query the database
        try (Statement stmt = conn.createStatement()) {
            // Query the database for the data
            ResultSet rs = stmt.executeQuery(String.format("SELECT current_balance FROM account WHERE id = %d", oldTransferDto.getAccountIdTo()));

            // Check if the data is edited
            assertThat(rs.next()).isTrue();
            assertThat(rs.getDouble(1)).isEqualTo(-1200.0);
        }
    }

    private void checkIsCurrentBalanceIsEditedForAccountTo(Connection conn, TransferDTO transferDTO) throws SQLException {
        // Create a statement to query the database
        try (Statement stmt = conn.createStatement()) {
            // Query the database for the data
            ResultSet rs = stmt.executeQuery(String.format("SELECT current_balance FROM account WHERE id = %d", transferDTO.getAccountIdTo()));

            // Check if the data is edited
            assertThat(rs.next()).isTrue();
            assertThat(rs.getDouble(1)).isEqualTo(4100.0);
        }
    }

    private void checkIsCurrentBalanceIsEditedForAccountFrom(Connection conn, TransferDTO transferDTO) throws SQLException {
        // Create a statement to query the database
        try (Statement stmt = conn.createStatement()) {
            // Query the database for the data
            ResultSet rs = stmt.executeQuery(String.format("SELECT current_balance FROM account WHERE id = %d", transferDTO.getAccountIdFrom()));

            // Check if the data is edited
            assertThat(rs.next()).isTrue();
            assertThat(rs.getDouble(1)).isEqualTo(800.0);
        }
    }

    private void checkIsAccountIdFromIsEdited(Connection conn, TransferDTO transferDTO) throws SQLException {
        // Create a statement to query the database
        try (Statement stmt = conn.createStatement()) {
            // Query the database for the data
            ResultSet rs = stmt.executeQuery(String.format("SELECT account_from_id FROM transfer WHERE id = %d", transferDTO.getId()));

            // Check if the data is edited
            assertThat(rs.next()).isTrue();
            assertThat(rs.getLong(1)).isEqualTo(transferDTO.getAccountIdFrom());
        }
    }

    private void checkIsAccountIdToIsEdited(Connection conn, TransferDTO transferDTO) throws SQLException {
        // Create a statement to query the database
        try (Statement stmt = conn.createStatement()) {
            // Query the database for the data
            ResultSet rs = stmt.executeQuery(String.format("SELECT account_to_id FROM transfer WHERE id = %d", transferDTO.getId()));

            // Check if the data is edited
            assertThat(rs.next()).isTrue();
            assertThat(rs.getLong(1)).isEqualTo(transferDTO.getAccountIdTo());
        }
    }
}
