package com.mlkb.ftm.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mlkb.ftm.ApplicationConfig;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
@WebAppConfiguration
@SpringBootTest
public class GlobalExceptionHandlerIntegrationTest {
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
    public void should_return_not_found_code_when_resource_not_found() throws Exception {
        NewAccountDTO invalidNewAccountDTO = new NewAccountDTO(1L, "Name", "CASH", 100.00, 40.00, "email@email.pl");
        String invalidNewAccountDTOtoJSON = convertAccountDtoToJson(invalidNewAccountDTO);

        doReturn(true).when(accountService).isValidNewAccount(any());
        when(accountService.getAllAccountsFromUser("anyValidEmail")).thenThrow(new ResourceNotFoundException("There is no such user"));
        when(accountService.createAccount(any())).thenThrow(new ResourceNotFoundException("There is no such user"));
        when(accountService.updateAccount(any())).thenThrow(new ResourceNotFoundException("There is no such account or user"));
        when(accountService.deleteAccount(1L, "none")).thenThrow(new ResourceNotFoundException("There is no such account"));

        mockMvc.perform(get("/api/account/anyValidEmail"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("There is no such user"));

        mockMvc.perform(post("/api/account").contentType(MediaType.APPLICATION_JSON)
                .content(invalidNewAccountDTOtoJSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("There is no such user"));

        mockMvc.perform(put("/api/account").contentType(MediaType.APPLICATION_JSON)
                .content(invalidNewAccountDTOtoJSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("There is no such account or user"));

        mockMvc.perform(delete("/api/account/1"))
                .andExpect(MockMvcResultMatchers.cookie().doesNotExist("e-mail"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("There is no such account"));
    }

    @Test
    public void should_return_bad_request_code_when_provided_with_incorrect_data() throws Exception {
        NewAccountDTO incorrectAccountDTO = new NewAccountDTO(1L, "Z", "CASH", 100.00, 40.00, "email@email.pl");
        String incorrectAccountDTOtoJSON = convertAccountDtoToJson(incorrectAccountDTO);

        when(accountService.isValidNewAccount(any())).thenThrow(new InputIncorrectException(InputValidationMessage.NAME));

        mockMvc.perform(post("/api/account").contentType(MediaType.APPLICATION_JSON)
                .content(incorrectAccountDTOtoJSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value(InputValidationMessage.NAME.message));

        mockMvc.perform(put("/api/account").contentType(MediaType.APPLICATION_JSON)
                .content(incorrectAccountDTOtoJSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value(InputValidationMessage.NAME.message));
    }

    private String convertAccountDtoToJson(NewAccountDTO accountDTO) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(accountDTO);
    }
}
