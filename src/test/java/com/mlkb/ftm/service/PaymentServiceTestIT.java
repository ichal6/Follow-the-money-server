package com.mlkb.ftm.service;

import com.mlkb.ftm.common.IntegrationTest;
import com.mlkb.ftm.fixture.*;
import com.mlkb.ftm.repository.*;
import com.mlkb.ftm.validation.InputValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.sql.*;
import java.time.Clock;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;

@Sql({
        "classpath:/sql/user.sql",
        "classpath:/sql/category.sql",
        "classpath:/sql/account.sql",
        "classpath:/sql/payee.sql",
        "classpath:/sql/transaction.sql"

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
    private PayeesRepository payeeRepository;
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
        transactionDto.setAccountId(AccountEntityFixture.myWallet().getId());
        String email = UserEntityFixture.userUserowy().getEmail();

        // when
        paymentService.updateTransaction(transactionDto, email);
        // then
        // Connect to the database
        try (Connection conn = DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword())) {
            // Create a statement to query the database
            try (Statement stmt = conn.createStatement()) {
                // Query the database for the data
                ResultSet rs = stmt.executeQuery(String.format("SELECT account_id FROM transaction WHERE id = %d", transactionDto.getId()));

                // Check if the data is edited
                assertThat(rs.next()).isTrue();
                assertThat(rs.getLong(1)).isEqualTo(transactionDto.getAccountId());
            }
        }

        // Connect to the database
        try (Connection conn = DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword())) {
            // Create a statement to query the database
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
}
