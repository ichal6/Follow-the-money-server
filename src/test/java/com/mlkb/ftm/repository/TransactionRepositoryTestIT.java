package com.mlkb.ftm.repository;

import com.mlkb.ftm.common.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Sql({
        "classpath:/sql/user.sql",
        "classpath:/sql/category.sql",
        "classpath:/sql/account.sql",
        "classpath:/sql/payee.sql",
        "classpath:/sql/transaction.sql"
})
@Sql(value = "classpath:/sql/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class TransactionRepositoryTestIT extends IntegrationTest {
    @Autowired
    private TransactionRepository transactionRepository;

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
    void should_return_false_if_existsByTransactionIdAndUserEmail_run_with_other_user() {
        // given
        String email = "jan.kowalski@gmail.com";
        Long transactionId = 1L;

        // when
        var isTransactionBelongToUser = transactionRepository.existsByTransactionIdAndUserEmail(transactionId, email);

        // then
        assertThat(isTransactionBelongToUser).isFalse();
    }

    @Test
    void should_return_false_if_existsByTransactionIdAndUserEmail_run_with_other_transaction() {
        // given
        String email = "user@user.pl";
        Long transactionId = 10L;

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
