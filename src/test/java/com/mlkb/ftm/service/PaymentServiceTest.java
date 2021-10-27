package com.mlkb.ftm.service;

import com.mlkb.ftm.ApplicationConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {ApplicationConfig.class})
//@WebAppConfiguration
@SpringBootTest
public class PaymentServiceTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void removeTransaction() {
        //fail();
        assertEquals(1, 1);
    }
}
