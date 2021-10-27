package com.mlkb.ftm.service;

import com.mlkb.ftm.ApplicationConfig;
import com.mlkb.ftm.entity.Account;
import com.mlkb.ftm.entity.Transaction;
import com.mlkb.ftm.entity.User;
import com.mlkb.ftm.repository.*;
import com.mlkb.ftm.validation.InputValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
//@WebAppConfiguration
@SpringBootTest
public class PaymentServiceTest {
    private PaymentService paymentService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InputValidator inputValidator;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PayeeRepository payeeRepository;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService(userRepository, inputValidator, transactionRepository, accountRepository, categoryRepository, payeeRepository, transferRepository);
    }

    @AfterEach
    void tearDown() {
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
}
