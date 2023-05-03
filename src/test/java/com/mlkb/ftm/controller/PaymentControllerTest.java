package com.mlkb.ftm.controller;

import com.mlkb.ftm.common.ApplicationConfig;
import com.mlkb.ftm.fixture.PaymentDTOFixture;
import com.mlkb.ftm.modelDTO.PaymentDTO;
import com.mlkb.ftm.service.PaymentService;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
@WebAppConfiguration
@SpringBootTest
class PaymentControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private PaymentService paymentService;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void should_return_all_payments() throws Exception {
        // given
        List<PaymentDTO> payments = List.of(PaymentDTOFixture.buyCarTransaction(),
                PaymentDTOFixture.buyMilkTransaction(),
                PaymentDTOFixture.cashDepositTransferMillenium()
        );

        // when/then
        when(paymentService.getPayments(anyString())).thenReturn(payments);

        mockMvc.perform(get("/api/payment/anyEmail"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.[0].isInternal").value("false"))
                .andExpect(jsonPath("$.[0].id").value("1"))
                .andExpect(jsonPath("$.[0].title").value("Buy Car"))
                .andExpect(jsonPath("$.[0].value").value("-2500.0"))
                .andExpect(jsonPath("$.[0].from").value("Millenium"))
                .andExpect(jsonPath("$.[0].to").value("Złomex"))
                .andExpect(jsonPath("$.[0].categoryName").value("Car"))
                .andExpect(jsonPath("$.[0].balanceAfter").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.[1].isInternal").value("false"))
                .andExpect(jsonPath("$.[1].id").value("2"))
                .andExpect(jsonPath("$.[1].title").value("Buy Milk"))
                .andExpect(jsonPath("$.[1].value").value("-5.0"))
                .andExpect(jsonPath("$.[1].from").value("Wallet"))
                .andExpect(jsonPath("$.[1].to").value("Biedronka"))
                .andExpect(jsonPath("$.[1].categoryName").value("Daily"))
                .andExpect(jsonPath("$.[1].balanceAfter").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.[2].isInternal").value("true"))
                .andExpect(jsonPath("$.[2].id").value("3"))
                .andExpect(jsonPath("$.[2].title").value("Cash Deposit January"))
                .andExpect(jsonPath("$.[2].value").value("100.0"))
                .andExpect(jsonPath("$.[2].from").value("Millenium"))
                .andExpect(jsonPath("$.[2].to").value("Wallet"))
                .andExpect(jsonPath("$.[2].categoryName").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.[2].balanceAfter").value(IsNull.nullValue()))
                .andReturn();

        verify(paymentService, atLeast(1)).getPayments(anyString());
    }

    @Test
    void should_return_payments_for_period() throws Exception {
        // given
        List<PaymentDTO> payments = List.of(PaymentDTOFixture.buyCarTransaction(),
                PaymentDTOFixture.buyMilkTransaction(),
                PaymentDTOFixture.cashDepositTransferMillenium()
        );

        // when/then
        when(paymentService.getPaymentsForPeriod(anyString(), anyString())).thenReturn(payments);

        mockMvc.perform(get("/api/payment/anyEmail?period=290"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.[0].isInternal").value("false"))
                .andExpect(jsonPath("$.[0].id").value("1"))
                .andExpect(jsonPath("$.[0].title").value("Buy Car"))
                .andExpect(jsonPath("$.[0].value").value("-2500.0"))
                .andExpect(jsonPath("$.[0].from").value("Millenium"))
                .andExpect(jsonPath("$.[0].to").value("Złomex"))
                .andExpect(jsonPath("$.[0].categoryName").value("Car"))
                .andExpect(jsonPath("$.[0].balanceAfter").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.[1].isInternal").value("false"))
                .andExpect(jsonPath("$.[1].id").value("2"))
                .andExpect(jsonPath("$.[1].title").value("Buy Milk"))
                .andExpect(jsonPath("$.[1].value").value("-5.0"))
                .andExpect(jsonPath("$.[1].from").value("Wallet"))
                .andExpect(jsonPath("$.[1].to").value("Biedronka"))
                .andExpect(jsonPath("$.[1].categoryName").value("Daily"))
                .andExpect(jsonPath("$.[1].balanceAfter").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.[2].isInternal").value("true"))
                .andExpect(jsonPath("$.[2].id").value("3"))
                .andExpect(jsonPath("$.[2].title").value("Cash Deposit January"))
                .andExpect(jsonPath("$.[2].value").value("100.0"))
                .andExpect(jsonPath("$.[2].from").value("Millenium"))
                .andExpect(jsonPath("$.[2].to").value("Wallet"))
                .andExpect(jsonPath("$.[2].categoryName").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.[2].balanceAfter").value(IsNull.nullValue()))
                .andReturn();

        verify(paymentService, atLeast(1)).getPaymentsForPeriod(anyString(), anyString());
    }

    @Test
    void should_return_payments_for_account() throws Exception {
        // given
        List<PaymentDTO> payments = List.of(PaymentDTOFixture.buyCarTransaction(),
                PaymentDTOFixture.buyMilkTransaction(),
                PaymentDTOFixture.cashDepositTransferMillenium()
        );

        // when/then
        when(paymentService.getPaymentsWithAccount(anyString(), anyString())).thenReturn(payments);

        mockMvc.perform(get("/api/payment/anyEmail?id=5"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.[0].isInternal").value("false"))
                .andExpect(jsonPath("$.[0].id").value("1"))
                .andExpect(jsonPath("$.[0].title").value("Buy Car"))
                .andExpect(jsonPath("$.[0].value").value("-2500.0"))
                .andExpect(jsonPath("$.[0].from").value("Millenium"))
                .andExpect(jsonPath("$.[0].to").value("Złomex"))
                .andExpect(jsonPath("$.[0].categoryName").value("Car"))
                .andExpect(jsonPath("$.[0].balanceAfter").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.[1].isInternal").value("false"))
                .andExpect(jsonPath("$.[1].id").value("2"))
                .andExpect(jsonPath("$.[1].title").value("Buy Milk"))
                .andExpect(jsonPath("$.[1].value").value("-5.0"))
                .andExpect(jsonPath("$.[1].from").value("Wallet"))
                .andExpect(jsonPath("$.[1].to").value("Biedronka"))
                .andExpect(jsonPath("$.[1].categoryName").value("Daily"))
                .andExpect(jsonPath("$.[1].balanceAfter").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.[2].isInternal").value("true"))
                .andExpect(jsonPath("$.[2].id").value("3"))
                .andExpect(jsonPath("$.[2].title").value("Cash Deposit January"))
                .andExpect(jsonPath("$.[2].value").value("100.0"))
                .andExpect(jsonPath("$.[2].from").value("Millenium"))
                .andExpect(jsonPath("$.[2].to").value("Wallet"))
                .andExpect(jsonPath("$.[2].categoryName").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.[2].balanceAfter").value(IsNull.nullValue()))
                .andReturn();

        verify(paymentService, atLeast(1)).getPaymentsWithAccount(anyString(), anyString());
    }

    @Test
    void should_return_payments_for_account_and_period() throws Exception {
        // given
        List<PaymentDTO> payments = List.of(PaymentDTOFixture.buyCarTransaction(),
                PaymentDTOFixture.buyMilkTransaction(),
                PaymentDTOFixture.cashDepositTransferMillenium()
        );

        // when/then
        when(paymentService.getPaymentsWithParameters(anyString(), anyString(), anyString())).thenReturn(payments);

        mockMvc.perform(get("/api/payment/anyEmail?id=5&period=365"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.[0].isInternal").value("false"))
                .andExpect(jsonPath("$.[0].id").value("1"))
                .andExpect(jsonPath("$.[0].title").value("Buy Car"))
                .andExpect(jsonPath("$.[0].value").value("-2500.0"))
                .andExpect(jsonPath("$.[0].from").value("Millenium"))
                .andExpect(jsonPath("$.[0].to").value("Złomex"))
                .andExpect(jsonPath("$.[0].categoryName").value("Car"))
                .andExpect(jsonPath("$.[0].balanceAfter").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.[1].isInternal").value("false"))
                .andExpect(jsonPath("$.[1].id").value("2"))
                .andExpect(jsonPath("$.[1].title").value("Buy Milk"))
                .andExpect(jsonPath("$.[1].value").value("-5.0"))
                .andExpect(jsonPath("$.[1].from").value("Wallet"))
                .andExpect(jsonPath("$.[1].to").value("Biedronka"))
                .andExpect(jsonPath("$.[1].categoryName").value("Daily"))
                .andExpect(jsonPath("$.[1].balanceAfter").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.[2].isInternal").value("true"))
                .andExpect(jsonPath("$.[2].id").value("3"))
                .andExpect(jsonPath("$.[2].title").value("Cash Deposit January"))
                .andExpect(jsonPath("$.[2].value").value("100.0"))
                .andExpect(jsonPath("$.[2].from").value("Millenium"))
                .andExpect(jsonPath("$.[2].to").value("Wallet"))
                .andExpect(jsonPath("$.[2].categoryName").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.[2].balanceAfter").value(IsNull.nullValue()))
                .andReturn();

        verify(paymentService, atLeast(1)).getPaymentsWithParameters(anyString(), anyString(), anyString());
    }
}