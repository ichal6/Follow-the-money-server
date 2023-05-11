package com.mlkb.ftm.common;

import com.mlkb.ftm.controller.AccountController;
import com.mlkb.ftm.controller.AnalysisController;
import com.mlkb.ftm.controller.PaymentController;
import com.mlkb.ftm.controller.UserController;
import com.mlkb.ftm.exception.GlobalExceptionHandler;
import com.mlkb.ftm.repository.*;
import com.mlkb.ftm.service.AccountService;
import com.mlkb.ftm.service.AnalysisService;
import com.mlkb.ftm.service.PaymentService;
import com.mlkb.ftm.service.UserService;
import com.mlkb.ftm.validation.AccessValidator;
import com.mlkb.ftm.validation.InputValidator;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.Clock;

@EnableWebMvc
@Configuration
public class ApplicationConfig {

    @Bean
    public AccountController accountController() {
        return new AccountController(accountService(), accessValidator());
    }

    @Bean
    public PaymentController paymentController() {
        return new PaymentController(paymentService(), accessValidator());
    }

    @Bean
    public AnalysisController analysisController() {
        return new AnalysisController(analysisService(), accessValidator());
    }

    @Bean
    public AccountService accountService() {
        return Mockito.mock(AccountService.class);
    }

    @Bean
    public UserController userController(){
        return new UserController(userService(), accessValidator());
    }

    @Bean
    public UserService userService() {
        return Mockito.mock(UserService.class);
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
    public AnalysisService analysisService() {
        return Mockito.mock(AnalysisService.class);
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

    @Bean
    public Clock clock() {
        return Mockito.mock(Clock.class);
    }
}
