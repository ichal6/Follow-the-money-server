package com.mlkb.ftm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mlkb.ftm.common.ApplicationConfig;
import com.mlkb.ftm.exception.IllegalAccessRuntimeException;
import com.mlkb.ftm.exception.InputIncorrectException;
import com.mlkb.ftm.exception.InputValidationMessage;
import com.mlkb.ftm.fixture.PaymentDTOFixture;
import com.mlkb.ftm.fixture.TransactionDTOFixture;
import com.mlkb.ftm.fixture.TransferDTOFixture;
import com.mlkb.ftm.fixture.UserEntityFixture;
import com.mlkb.ftm.modelDTO.PaymentDTO;
import com.mlkb.ftm.modelDTO.TransactionDTO;
import com.mlkb.ftm.modelDTO.TransferDTO;
import com.mlkb.ftm.service.PaymentService;
import com.mlkb.ftm.validation.AccessValidator;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import jakarta.servlet.http.Cookie;

import java.util.*;

import static com.mlkb.ftm.common.Utils.getDate;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    @Autowired
    private AccessValidator accessValidator;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        reset(this.accessValidator);
        reset(this.paymentService);
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

    @Test
    void should_update_transaction_for_correct_data() throws Exception {
        // given
        var objectMapper = new ObjectMapper();
        var transactionUpdateDto = TransactionDTOFixture.buyCarTransaction();
        String email = UserEntityFixture.userUserowy().getEmail();
        var cookie = new Cookie("e-mail", email);

        // when/then
        mockMvc.perform(
                put("/api/payment/transaction")
                    .contentType(MediaType.APPLICATION_JSON)
                    .cookie(cookie)
                    .content(objectMapper.writeValueAsString(transactionUpdateDto))
                    .accept(MediaType.APPLICATION_JSON)
                    )
                .andExpect(status().isNoContent());

        verify(paymentService, atLeastOnce()).isValidUpdateTransaction(transactionUpdateDto);
        verify(paymentService, atLeastOnce()).updateTransaction(transactionUpdateDto, email);
    }

    @Test
    void should_throw_exception_when_update_transaction_for_incorrect_data() throws Exception {
        // given
        var objectMapper = new ObjectMapper();
        var transactionUpdateDto = new TransactionDTO();
        String email = UserEntityFixture.userUserowy().getEmail();
        var cookie = new Cookie("e-mail", email);
        // when/then
        doThrow(new InputIncorrectException(InputValidationMessage.NULL))
                .when(paymentService)
                .isValidUpdateTransaction(transactionUpdateDto);

        mockMvc.perform(
                        put("/api/payment/transaction")
                                .contentType(MediaType.APPLICATION_JSON)
                                .cookie(cookie)
                                .content(objectMapper.writeValueAsString(transactionUpdateDto))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InputIncorrectException))
                .andExpect(result -> assertEquals(InputValidationMessage.NULL.message,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));

        verify(paymentService, atLeastOnce()).isValidUpdateTransaction(transactionUpdateDto);
    }

    @Test
    void should_throw_exception_when_update_transaction_for_incorrect_user() throws Exception {
        // given
        var objectMapper = new ObjectMapper();
        var transactionUpdateDto = new TransactionDTO();
        String email = UserEntityFixture.userUserowy().getEmail();
        var cookie = new Cookie("e-mail", email);
        // when/then
        doThrow(new IllegalAccessRuntimeException("Access denied"))
                .when(accessValidator)
                .checkPermit(email);

        mockMvc.perform(
                        put("/api/payment/transaction")
                                .contentType(MediaType.APPLICATION_JSON)
                                .cookie(cookie)
                                .content(objectMapper.writeValueAsString(transactionUpdateDto))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalAccessRuntimeException))
                .andExpect(result -> assertEquals("Access denied",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));

        verify(accessValidator, atLeastOnce()).checkPermit(email);
    }

    @Test
    void should_throw_exception_when_update_transaction_when_user_not_login() throws Exception {
        // given
        var objectMapper = new ObjectMapper();
        var transactionUpdateDto = new TransactionDTO();
        String email = UserEntityFixture.userUserowy().getEmail();
        var cookie = new Cookie("e-mail", email);
        // when/then
        doThrow(new UsernameNotFoundException("You have to log in before access"))
                .when(accessValidator)
                .checkPermit(email);

        mockMvc.perform(
                        put("/api/payment/transaction")
                                .contentType(MediaType.APPLICATION_JSON)
                                .cookie(cookie)
                                .content(objectMapper.writeValueAsString(transactionUpdateDto))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UsernameNotFoundException))
                .andExpect(result -> assertEquals("You have to log in before access",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));

        verify(accessValidator, atLeastOnce()).checkPermit(email);
    }

    @Test
    void should_return_transaction_for_correct_id_and_email() throws Exception {
        // given
        TransactionDTO transactionDTO = TransactionDTOFixture.buyCarTransactionBeforeUpdate();

        // when/then
        when(paymentService.getTransaction(anyString(), anyLong())).thenReturn(transactionDTO);

        mockMvc.perform(get("/api/payment/transaction/anyEmail/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("Buy Car"))
                .andExpect(jsonPath("$.value").value("-2500.0"))
                .andExpect(jsonPath("$.accountId").value("1"))
                .andExpect(jsonPath("$.type").value("EXPENSE"))
                .andExpect(jsonPath("$.payeeId").value("4"))
                .andExpect(jsonPath("$.categoryId").value("5"))
                .andExpect(jsonPath("$.date").value(
                        new GregorianCalendar(2023, Calendar.JANUARY, 19).getTime().getTime()))
                .andReturn();

        verify(paymentService, atLeast(1)).getTransaction(anyString(), anyLong());
    }

    @Test
    void should_return_transfer_for_correct_id_and_email() throws Exception {
        // given
        TransferDTO transferDTO = TransferDTOFixture.cashDepositTransferMillennium();

        // when/then
        when(paymentService.getTransfer(anyString(), anyLong())).thenReturn(transferDTO);

        mockMvc.perform(get("/api/payment/transfer/anyEmail/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("3"))
                .andExpect(jsonPath("$.title").value("Cash Deposit January"))
                .andExpect(jsonPath("$.value").value("100.0"))
                .andExpect(jsonPath("$.accountIdFrom").value("1"))
                .andExpect(jsonPath("$.accountIdTo").value("2"))
                .andExpect(jsonPath("$.date").value(
                        getDate(2023, Calendar.SEPTEMBER, 7, 17, 55)))
                .andReturn();

        verify(paymentService, atLeast(1)).getTransfer(anyString(), anyLong());
    }

    @Test
    void should_update_transfer_for_correct_data() throws Exception {
        // given
        var objectMapper = new ObjectMapper();
        var transferUpdateDto = TransferDTOFixture.cashDepositTransferMillennium();
        String email = UserEntityFixture.userUserowy().getEmail();
        var cookie = new Cookie("e-mail", email);

        // when/then
        mockMvc.perform(
                        put("/api/payment/transfer")
                                .contentType(MediaType.APPLICATION_JSON)
                                .cookie(cookie)
                                .content(objectMapper.writeValueAsString(transferUpdateDto))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());

        verify(paymentService, atLeastOnce()).isValidUpdateTransfer(transferUpdateDto);
        verify(paymentService, atLeastOnce()).updateTransfer(transferUpdateDto, email);
    }

    @Test
    void should_throw_exception_when_update_transfer_for_incorrect_data() throws Exception {
        // given
        var objectMapper = new ObjectMapper();
        var transferUpdateDto = new TransferDTO();
        String email = UserEntityFixture.userUserowy().getEmail();
        var cookie = new Cookie("e-mail", email);
        // when/then
        doThrow(new InputIncorrectException(InputValidationMessage.NULL))
                .when(paymentService)
                .isValidUpdateTransfer(transferUpdateDto);

        mockMvc.perform(
                        put("/api/payment/transfer")
                                .contentType(MediaType.APPLICATION_JSON)
                                .cookie(cookie)
                                .content(objectMapper.writeValueAsString(transferUpdateDto))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InputIncorrectException))
                .andExpect(result -> assertEquals(InputValidationMessage.NULL.message,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));

        verify(paymentService, atLeastOnce()).isValidUpdateTransfer(transferUpdateDto);
    }

    @Test
    void should_throw_exception_when_update_transfer_for_incorrect_user() throws Exception {
        // given
        var objectMapper = new ObjectMapper();
        var transferUpdateDto = new TransferDTO();
        String email = UserEntityFixture.userUserowy().getEmail();
        var cookie = new Cookie("e-mail", email);
        // when/then
        doThrow(new IllegalAccessRuntimeException("Access denied"))
                .when(accessValidator)
                .checkPermit(email);

        mockMvc.perform(
                        put("/api/payment/transfer")
                                .contentType(MediaType.APPLICATION_JSON)
                                .cookie(cookie)
                                .content(objectMapper.writeValueAsString(transferUpdateDto))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalAccessRuntimeException))
                .andExpect(result -> assertEquals("Access denied",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));

        verify(accessValidator, atLeastOnce()).checkPermit(email);
    }

    @Test
    void should_throw_exception_when_update_transfer_when_user_not_login() throws Exception {
        // given
        var objectMapper = new ObjectMapper();
        var transferUpdateDto = new TransferDTO();
        String email = UserEntityFixture.userUserowy().getEmail();
        var cookie = new Cookie("e-mail", email);
        // when/then
        doThrow(new UsernameNotFoundException("You have to log in before access"))
                .when(accessValidator)
                .checkPermit(email);

        mockMvc.perform(
                        put("/api/payment/transfer")
                                .contentType(MediaType.APPLICATION_JSON)
                                .cookie(cookie)
                                .content(objectMapper.writeValueAsString(transferUpdateDto))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UsernameNotFoundException))
                .andExpect(result -> assertEquals("You have to log in before access",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));

        verify(accessValidator, atLeastOnce()).checkPermit(email);
    }
}
