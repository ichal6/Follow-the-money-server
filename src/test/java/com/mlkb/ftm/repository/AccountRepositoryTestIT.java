package com.mlkb.ftm.repository;

import com.mlkb.ftm.common.IntegrationTest;
import com.mlkb.ftm.entity.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Sql({
        "classpath:/sql/user.sql",
        "classpath:/sql/account.sql"
})
@Sql(value = "classpath:/sql/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AccountRepositoryTestIT extends IntegrationTest {
    @Autowired
    private AccountRepository accountRepository;

    @Test
    void should_find_account_for_user_email_and_account_id() {
        // given
        Long accountId = 1L;
        String email = "user@user.pl";
        // when
        Optional<Account> possibleAccount = this.accountRepository.findByAccountIdAndUserEmail(accountId, email);
        //then
        assertThat(possibleAccount).isNotEmpty();
    }


    @Test
    void should_not_find_account_for_wrong_user_email_and_correct_account_id() {
        // given
        Long accountId = 1L;
        String email = "jan.kowalski@gmail.com";
        // when
        Optional<Account> possibleAccount = this.accountRepository.findByAccountIdAndUserEmail(accountId, email);
        //then
        assertThat(possibleAccount).isEmpty();
    }

    @Test
    void should_not_find_account_for_correct_user_email_and_wrong_account_id() {
        // given
        Long accountId = 6L;
        String email = "user@user.pl";
        // when
        Optional<Account> possibleAccount = this.accountRepository.findByAccountIdAndUserEmail(accountId, email);
        //then
        assertThat(possibleAccount).isEmpty();
    }

    @Test
    void should_not_find_account_for_wrong_user_email_and_wrong_account_id() {
        // given
        Long accountId = 8L;
        String email = "jan.kowalski@gmail.com";
        // when
        Optional<Account> possibleAccount = this.accountRepository.findByAccountIdAndUserEmail(accountId, email);
        //then
        assertThat(possibleAccount).isEmpty();
    }

}
