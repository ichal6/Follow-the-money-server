package com.mlkb.ftm;

import com.mlkb.ftm.controller.AccountController;
import com.mlkb.ftm.exception.GlobalExceptionHandler;
import com.mlkb.ftm.repository.*;
import com.mlkb.ftm.service.AccountService;
import com.mlkb.ftm.service.PaymentService;
import com.mlkb.ftm.validation.AccessValidator;
import com.mlkb.ftm.validation.InputValidator;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
public class ApplicationConfig {

    @Bean
    public AccountController accountController() {
        return new AccountController(accountService(), accessValidator());
    }

    @Bean
    public AccountService accountService() {
        return Mockito.mock(AccountService.class);
    }

    @Bean
    public AccessValidator accessValidator() {return Mockito.mock(AccessValidator.class);}

    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    public PaymentService paymentService() {
        return Mockito.mock(PaymentService.class);
    }

    @Bean
    public UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }
    @Bean
    public InputValidator inputValidator() {
        return Mockito.mock(InputValidator.class);
    }

    @Bean
    public TransactionRepository transactionRepository() {
        return Mockito.mock(TransactionRepository.class);
    }

    @Bean
    public TransferRepository transferRepository() {
        return Mockito.mock(TransferRepository.class);
    }

    @Bean
    public AccountRepository accountRepository() {
        return Mockito.mock(AccountRepository.class);
    }

    @Bean
    public CategoryRepository categoryRepository() {
        return Mockito.mock(CategoryRepository.class);
    }

    @Bean
    public PayeeRepository payeeRepository() {
        return Mockito.mock(PayeeRepository.class);
    }
}
