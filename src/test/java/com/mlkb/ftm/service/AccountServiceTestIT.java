package com.mlkb.ftm.service;

import com.mlkb.ftm.common.IntegrationTest;
import com.mlkb.ftm.fixture.NewAccountDTOFixture;
import com.mlkb.ftm.repository.AccountRepository;
import com.mlkb.ftm.repository.UserRepository;
import com.mlkb.ftm.validation.InputValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.sql.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Sql({
        "classpath:/sql/user.sql",
        "classpath:/sql/account.sql",
        "classpath:/sql/payee.sql",
        "classpath:/sql/category.sql",
        "classpath:/sql/transaction.sql",
        "classpath:/sql/transfer.sql"
})
@Sql(value = "classpath:/sql/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AccountServiceTestIT extends IntegrationTest {
    private AccountService accountService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    InputValidator inputValidator;

    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    public void setUp() {
        accountService = new AccountService(userRepository, inputValidator, accountRepository);
    }

    @Test
    void should_contain_all_transactions_after_update_account() throws SQLException {
        // given
        var dto = NewAccountDTOFixture.millenniumNewAccountDTO();
        // when
        this.accountService.updateAccount(dto);
        // then
        // Connect to the database
        try (Connection conn = DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword())) {
            // Create a statement to query the database
            try (Statement stmt = conn.createStatement()) {
                // Query the database for the data
                ResultSet rs = stmt.executeQuery(String.format("""
                        SELECT SUM(count)
                        FROM (
                           SELECT COUNT(*) as count
                            FROM Transaction
                            WHERE account_id = %1$d
                        
                            UNION ALL
                        
                            SELECT COUNT(*) as count
                            FROM Transfer
                            WHERE account_from_id = %1$d OR account_to_id = %1$d
                        ) as combined_counts;""", dto.getId()));
                // Check if the data is edited
                assertThat(rs.next()).isTrue();
                assertThat(rs.getLong(1)).isEqualTo(8L);
            }
        }
    }

}
