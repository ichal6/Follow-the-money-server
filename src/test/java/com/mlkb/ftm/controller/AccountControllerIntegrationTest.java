package com.mlkb.ftm.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mlkb.ftm.common.ApplicationConfig;
import com.mlkb.ftm.modelDTO.AccountDTO;
import com.mlkb.ftm.modelDTO.NewAccountDTO;
import com.mlkb.ftm.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
@WebAppConfiguration
@SpringBootTest
public class AccountControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void should_return_all_users_for_given_email() throws Exception {
        when(accountService.getAllAccountsFromUser(anyString())).thenReturn(buildDummyAccounts());

        mockMvc.perform(get("/api/account/anyEmail"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id").value("1"))
                .andExpect(jsonPath("$.[0].name").value("First account"))
                .andExpect(jsonPath("$.[0].accountType").value("BANK"))
                .andExpect(jsonPath("$.[0].currentBalance").value("100.0"))
                .andExpect(jsonPath("$.[0].startingBalance").value("50.0"))
                .andExpect(jsonPath("$.[1].id").value("2"))
                .andExpect(jsonPath("$.[1].name").value("Second account"))
                .andExpect(jsonPath("$.[1].accountType").value("CASH"))
                .andExpect(jsonPath("$.[1].currentBalance").value("0.0"))
                .andExpect(jsonPath("$.[1].startingBalance").value("50.0"))
                .andReturn();
    }

    @Test
    public void should_return_created_status_code_and_created_object_when_adding_new_account() throws Exception {
        //given
        NewAccountDTO newAccountDTO = new NewAccountDTO(1L, "Name", "CASH", 100.00, 40.00, "email@email.pl");
        String newAccountDTOtoJSON = convertAccountDtoToJson(newAccountDTO);

        // when
        doReturn(newAccountDTO).when(accountService).createAccount(any());
        when(accountService.isValidNewAccount(any())).thenReturn(true);
        mockMvc.perform(post("/api/account").contentType(MediaType.APPLICATION_JSON)
                .content(newAccountDTOtoJSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Name"))
                .andExpect(jsonPath("$.accountType").value("CASH"))
                .andExpect(jsonPath("$.currentBalance").value(40.0))
                .andExpect(jsonPath("$.startingBalance").value(100.0))
                .andExpect(jsonPath("$.userEmail").value("email@email.pl"));

        //then
        verify(accountService, atLeast(1)).createAccount(any(NewAccountDTO.class));
    }

    @Test
    public void should_return_ok_status_code_and_updated_object_when_modifying_account() throws Exception {
        // given
        NewAccountDTO accountDTO = new NewAccountDTO(1L, "Name", "CASH", 100.00, 40.00, "email@email.pl");
        String accountDTOtoJSON = convertAccountDtoToJson(accountDTO);

        // when
        doReturn(accountDTO).when(accountService).updateAccount(any());
        when(accountService.isValidNewAccount(any())).thenReturn(true);
        mockMvc.perform(put("/api/account").contentType(MediaType.APPLICATION_JSON)
                .content(accountDTOtoJSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Name"))
                .andExpect(jsonPath("$.accountType").value("CASH"))
                .andExpect(jsonPath("$.currentBalance").value(40.0))
                .andExpect(jsonPath("$.startingBalance").value(100.0))
                .andExpect(jsonPath("$.userEmail").value("email@email.pl"));

        // then
        verify(accountService, atLeast(1)).updateAccount(any(NewAccountDTO.class));
    }

    @Test
    public void should_return_ok_status_code_and_id_when_deleting_account() throws Exception {
        when(accountService.deleteAccount(5L, "none")).thenReturn(true);

        mockMvc.perform(delete("/api/account/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("5"));

        verify(accountService, times(1)).deleteAccount(5L, "none");
    }

    private String convertAccountDtoToJson(NewAccountDTO accountDTO) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(accountDTO);
    }

    private List<AccountDTO> buildDummyAccounts() {
        return asList(
                new AccountDTO(1L,
                        "First account",
                        "BANK",
                        100.00,
                        50.00),
                new AccountDTO(2L,
                        "Second account",
                        "CASH",
                        0.00,
                        50.00)
        );
    }
}
