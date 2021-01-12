package com.mlkb.ftm;

import com.mlkb.ftm.controller.AccountController;
import com.mlkb.ftm.exception.GlobalExceptionHandler;
import com.mlkb.ftm.service.AccountService;
import com.mlkb.ftm.validation.AccessValidator;
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
}
