package com.mlkb.ftm.controller;

import com.mlkb.ftm.ApplicationConfig;
import com.mlkb.ftm.modelDTO.UserDTO;
import com.mlkb.ftm.service.UserService;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
@WebAppConfiguration
@SpringBootTest
class UserControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void should_get_user_by_email() throws Exception {
        //given
        Date date = new GregorianCalendar(2020, Calendar.MAY, 8).getTime();
        UserDTO user = new UserDTO("User Userowy", "user@user.pl", date);
        //when
        when(userService.getUser(anyString()))
                .thenReturn(user);

        mockMvc.perform(get("/api/user/anyEmail"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name").value("User Userowy"))
                .andExpect(jsonPath("$.email").value("user@user.pl"))
                .andExpect(jsonPath("$.date").value("07/05/2020")) //JSON return UTC time (-1 by Warsaw time)
                .andReturn();
        //then
        verify(userService, atLeast(1)).getUser(anyString());
    }

    @Test
    void createUser() {
    }
}