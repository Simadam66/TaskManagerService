package com.example.demo.controller;

import com.example.demo.dto.UserResponse;
import com.example.demo.exception.ApiExceptionHandler;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.user.User;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.Month.FEBRUARY;
import static java.time.Month.JANUARY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApiExceptionHandler exceptionHandler;

    @MockBean
    UserService service;

    private static final User USER_1 =
            User.builder()
                    .id(1L)
                    .name("Laci")
                    .email("laci@freemail.com")
                    .birthDate(LocalDate.of(1997, JANUARY, 12))
                    .tasks(new ArrayList<>())
                    .build();

    private static final User USER_2 =
            User.builder()
                    .id(2L)
                    .name("Feri")
                    .email("feri@freemail.com")
                    .birthDate(LocalDate.of(1996, FEBRUARY, 13))
                    .tasks(new ArrayList<>())
                    .build();

    private static final UserResponse USER_1_RESPONSE =
            UserResponse.builder()
                    .id(1L)
                    .name("Laci")
                    .email("laci@freemail.com")
                    .birthDate(LocalDate.of(1997, JANUARY, 12))
                    .build();

    private static final UserResponse USER_2_RESPONSE =
            UserResponse.builder()
                    .id(2L)
                    .name("Feri")
                    .email("feri@freemail.com")
                    .birthDate(LocalDate.of(1996, FEBRUARY, 13))
                    .build();

    private static final ArrayList<User> USER_LIST = new ArrayList<User>();

    @BeforeAll
    static void init(){
        USER_LIST.add(USER_1);
        USER_LIST.add(USER_2);
    }


    @Test
    void getUsersReturnsOk() throws Exception {
        ObjectWriter ow = new ObjectMapper().registerModule(new JavaTimeModule()).writer();
        when(service.getUsers()).thenReturn(USER_LIST);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/user"))
                .andExpect(status().isOk())
                .andReturn();

        String expectedResponseBody = ow.writeValueAsString(List.of(USER_1_RESPONSE, USER_2_RESPONSE));
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(expectedResponseBody, actualResponseBody);
    }

    @Test
    void getUserReturnsOk() throws Exception {
        ObjectWriter ow = new ObjectMapper().registerModule(new JavaTimeModule()).writer();
        when(service.getUser(1L)).thenReturn(USER_1);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/user/1"))
                .andExpect(status().isOk())
                .andReturn();

        String expectedResponseBody = ow.writeValueAsString(List.of(USER_1_RESPONSE));
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(expectedResponseBody, actualResponseBody);
    }

    @Test
    void getUserCalledWithNonExistentUserId() throws Exception {
        when(service.getUser(any())).thenThrow(new UserNotFoundException(3L));

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/user/3"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException))
                .andReturn();

        String expectedResponse = "User with id 3 does not exist.";
        String actualResponse = mvcResult.getResponse().getContentAsString();

        assertEquals(expectedResponse, actualResponse);
    }

}