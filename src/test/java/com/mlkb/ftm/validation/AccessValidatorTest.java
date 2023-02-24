package com.mlkb.ftm.validation;

import com.mlkb.ftm.ApplicationConfig;
import com.mlkb.ftm.exception.IllegalAccessRuntimeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
@SpringBootTest
class AccessValidatorTest {
    private AccessValidator accessValidator;

    private Authentication authentication;

    @BeforeEach
    public void setUp(){
        this.accessValidator = new AccessValidator();
        this.authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);


        when(securityContext.getAuthentication()).thenReturn(this.authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void should_check_permissions_when_email_is_correct() {
        // given
        String email = "user@user.pl";

        //when
        when(authentication.getPrincipal()).thenReturn("user@user.pl");

        // then
        assertTrue(accessValidator.checkPermit(email));
    }

    @Test
    void should_throw_exception_when_email_is_not_correct() {
        // given
        String email = "nowy@user.pl";

        //when
        when(authentication.getPrincipal()).thenReturn(null);

        UsernameNotFoundException thrown = Assertions.assertThrows(UsernameNotFoundException.class, () ->
                accessValidator.checkPermit(email), "You have to log in before access");
        // then
        Assertions.assertEquals("You have to log in before access", thrown.getMessage());

    }

    @Test
    void should_throw_exception_when_user_does_not_log_in() {
        // given
        String email = "nowy@user.pl";

        //when
        when(authentication.getPrincipal()).thenReturn("user@user.pl");

        IllegalAccessRuntimeException thrown = Assertions.assertThrows(IllegalAccessRuntimeException.class, () ->
                accessValidator.checkPermit(email), "Access denied");
        // then
        Assertions.assertEquals("Access denied", thrown.getMessage());

    }
}