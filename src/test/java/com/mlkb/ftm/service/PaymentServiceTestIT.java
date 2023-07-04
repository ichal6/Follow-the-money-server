package com.mlkb.ftm.service;

import com.mlkb.ftm.common.IntegrationTest;
import com.mlkb.ftm.fixture.TransactionDTOFixture;
import com.mlkb.ftm.repository.*;
import com.mlkb.ftm.validation.InputValidator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.sql.*;
import java.time.Clock;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;

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
    private PayeeRepository payeeRepository;
    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private Clock clock;

    @Test
    @Disabled
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
        // when
        paymentService.updateTransaction(transactionDto, anyString());
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
    void should_return_false_if_existsByTransactionIdAndUserEmail_run_with_incorrect_email() {
        // given
        String email = "no@user.pl";
        Long transactionId = 1L;

        // when
        var isTransactionBelongToUser = transactionRepository.existsByTransactionIdAndUserEmail(transactionId, email);

        // then
        assertThat(isTransactionBelongToUser).isFalse();
    }

    @Test
    void should_return_false_if_existsByTransactionIdAndUserEmail_run_with_incorrect_id() {
        // given
        String email = "no@user.pl";
        Long transactionId = 6L;

        // when
        var isTransactionBelongToUser = transactionRepository.existsByTransactionIdAndUserEmail(transactionId, email);

        // then
        assertThat(isTransactionBelongToUser).isFalse();
    }

    @Test
    void should_return_true_if_existsByTransactionIdAndUserEmail_run_with_correct_parameters() {
        // given
        String email = "user@user.pl";
        Long transactionId = 1L;

        // when
        var isTransactionBelongToUser = transactionRepository.existsByTransactionIdAndUserEmail(transactionId, email);

        // then
        assertThat(isTransactionBelongToUser).isTrue();
    }
}
