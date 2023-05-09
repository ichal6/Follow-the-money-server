package com.mlkb.ftm.service;

import com.mlkb.ftm.common.ApplicationConfig;
import com.mlkb.ftm.entity.User;
import com.mlkb.ftm.fixture.AccountEntityFixture;
import com.mlkb.ftm.modelDTO.AccountDTO;
import com.mlkb.ftm.repository.AccountRepository;
import com.mlkb.ftm.repository.UserRepository;
import com.mlkb.ftm.validation.InputValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
@SpringBootTest
class AccountServiceTest {

    private AccountService accountService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InputValidator inputValidator;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    public void setUp() {
        this.accountService =  new AccountService(userRepository, inputValidator, accountRepository);
    }

    @Test
    void check_if_all_account_has_correct_scale() {
        // given
        int SCALE = 2;
        String email = "user@user.pl";
        final User user = new User();
        user.setAccounts(Set.of(
                AccountEntityFixture.allegroPay(),
                AccountEntityFixture.millennium()
        ));
        // when
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        List<AccountDTO> allAccountsFromUser = accountService.getAllAccountsFromUser(email);

        // then
        assertThat(allAccountsFromUser)
                .hasSize(2);

        Optional<Integer> possibleWrongScale = allAccountsFromUser.stream()
                .map(a -> {
                    var asString = String.valueOf(a.getCurrentBalance());
                    int indexOfDecimal = asString.indexOf(".");
                    var decimalPart = asString.substring(indexOfDecimal);
                    return decimalPart.length();
                })
                .filter(s -> s > SCALE)
                .findAny();
        assertThat(possibleWrongScale)
                .isEmpty();
    }
}
