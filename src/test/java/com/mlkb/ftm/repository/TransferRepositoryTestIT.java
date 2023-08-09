package com.mlkb.ftm.repository;

import com.mlkb.ftm.common.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@Sql({
        "classpath:/sql/user.sql",
        "classpath:/sql/account.sql",
        "classpath:/sql/transfer.sql"
})
@Sql(value = "classpath:/sql/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class TransferRepositoryTestIT extends IntegrationTest {
    @Autowired
    private TransferRepository transferRepository;

    @Test
    void should_return_false_if_existsByTransferIdAndUserEmail_run_with_incorrect_email() {
        // given
        String email = "no@user.pl";
        Long transferId = 1L;

        // when
        var isTransferBelongToUser = transferRepository.existsByTransferIdAndUserEmail(transferId, email);

        // then
        assertThat(isTransferBelongToUser).isFalse();
    }


    @Test
    void should_return_false_if_existsByTransferIdAndUserEmail_run_with_other_user() {
        // given
        String email = "jan.kowalski@gmail.com";
        Long transferId = 1L;

        // when
        var isTransferBelongToUser = transferRepository.existsByTransferIdAndUserEmail(transferId, email);

        // then
        assertThat(isTransferBelongToUser).isFalse();
    }

    @Test
    void should_return_false_if_existsByTransferIdAndUserEmail_run_with_other_transfer() {
        // given
        String email = "user@user.pl";
        Long transferId = 5L;

        // when
        var isTransferBelongToUser = transferRepository.existsByTransferIdAndUserEmail(transferId, email);

        // then
        assertThat(isTransferBelongToUser).isFalse();
    }

    @Test
    void should_return_false_if_existsByTransferIdAndUserEmail_run_with_incorrect_id() {
        // given
        String email = "no@user.pl";
        Long transferId = 6L;

        // when
        var isTransferBelongToUser = transferRepository.existsByTransferIdAndUserEmail(transferId, email);

        // then
        assertThat(isTransferBelongToUser).isFalse();
    }

    @Test
    void should_return_true_if_existsByTransferIdAndUserEmail_run_with_correct_parameters() {
        // given
        String email = "user@user.pl";
        Long transferId = 1L;

        // when
        var isTransferBelongToUser = transferRepository.existsByTransferIdAndUserEmail(transferId, email);

        // then
        assertThat(isTransferBelongToUser).isTrue();
    }

    @Test
    void should_return_true_if_existsByTransferIdAndUserEmail_run_with_correct_parameters_2() {
        // given
        String email = "jan.kowalski@gmail.com";
        Long transferId = 5L;

        // when
        var isTransferBelongToUser = transferRepository.existsByTransferIdAndUserEmail(transferId, email);

        // then
        assertThat(isTransferBelongToUser).isTrue();
    }
}
