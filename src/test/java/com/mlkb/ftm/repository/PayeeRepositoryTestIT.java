package com.mlkb.ftm.repository;

import com.mlkb.ftm.common.IntegrationTest;
import com.mlkb.ftm.entity.Payee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Sql({
        "classpath:/sql/user.sql",
        "classpath:/sql/payee.sql"
})
@Sql(value = "classpath:/sql/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PayeeRepositoryTestIT extends IntegrationTest {

    @Autowired
    private PayeesRepository payeeRepository;

    @Test
    void should_find_payee_for_user_email_and_payee_id() {
        // given
        Long payeeId = 1L;
        String email = "user@user.pl";
        // when
        Optional<Payee> possiblePayee = this.payeeRepository.findByPayeeIdAndUserEmail(payeeId, email);
        //then
        assertThat(possiblePayee).isNotEmpty();
    }

    @Test
    void should_find_payees_for_user_email() {
        // given
        String email = "user@user.pl";
        // when
        Set<Payee> possiblePayee = this.payeeRepository.getPayees(email);
        //then
        assertThat(possiblePayee).isNotEmpty();
    }


    @Test
    void should_not_find_payee_for_wrong_user_email_and_correct_payee_id() {
        // given
        Long payeeId = 1L;
        String email = "jan.kowalski@gmail.com";
        // when
        Optional<Payee> possiblePayee = this.payeeRepository.findByPayeeIdAndUserEmail(payeeId, email);
        //then
        assertThat(possiblePayee).isEmpty();
    }

    @Test
    void should_not_find_payee_for_correct_user_email_and_wrong_payee_id() {
        // given
        Long payeeId = 10L;
        String email = "user@user.pl";
        // when
        Optional<Payee> possiblePayee = this.payeeRepository.findByPayeeIdAndUserEmail(payeeId, email);
        //then
        assertThat(possiblePayee).isEmpty();
    }

    @Test
    void should_not_find_payee_for_wrong_user_email_and_wrong_payee_id() {
        // given
        Long payeeId = 13L;
        String email = "jan.kowalski@gmail.com";
        // when
        Optional<Payee> possiblePayee = this.payeeRepository.findByPayeeIdAndUserEmail(payeeId, email);
        //then
        assertThat(possiblePayee).isEmpty();
    }
}
