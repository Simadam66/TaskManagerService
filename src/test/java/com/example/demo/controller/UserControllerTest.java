package com.example.demo.controller;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.exception.EmailTakenException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.user.User;
import com.example.demo.model.user.UserRepository;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.Month.FEBRUARY;
import static java.time.Month.JANUARY;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

    private static final User USER_1_MOD =
            User.builder()
                    .id(1L)
                    .name("Laci")
                    .email("laca@freemail.com")
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

    private static final UserResponse USER_1_RESPONSE_MOD =
            UserResponse.builder()
                    .id(1L)
                    .name("Laci")
                    .email("laca@freemail.com")
                    .birthDate(LocalDate.of(1997, JANUARY, 12))
                    .build();

    private static final UserResponse USER_2_RESPONSE =
            UserResponse.builder()
                    .id(2L)
                    .name("Feri")
                    .email("feri@freemail.com")
                    .birthDate(LocalDate.of(1996, FEBRUARY, 13))
                    .build();

    private static final UserRequest USER_1_REQUEST =
            UserRequest.builder()
                    .name("Laci")
                    .email("laci@freemail.com")
                    .birthDate(LocalDate.of(1997, JANUARY, 12))
                    .build();

    private static final UserRequest USER_1_REQUEST_MOD =
            UserRequest.builder()
                    .name("Laci")
                    .email("laca@freemail.com")
                    .birthDate(LocalDate.of(1997, JANUARY, 12))
                    .build();

    private static final UserRequest BAD_USER_REQUEST =
            UserRequest.builder()
                    .name("")
                    .email("valami@freemail.com")
                    .birthDate(LocalDate.of(2005, JANUARY, 12))
                    .build();

    private static final UserRequest BAD_USER_REQUEST_2 =
            UserRequest.builder()
                    .name("")
                    .email("")
                    .birthDate(LocalDate.of(2005, JANUARY, 12))
                    .build();

    private static final ArrayList<User> USER_LIST = new ArrayList<User>();

    @BeforeAll
    static void init(){
        USER_LIST.add(USER_1);
        USER_LIST.add(USER_2);
    }

    @Test
    void getUsersReturnsOk() throws Exception {
        when(service.getUsers()).thenReturn(USER_LIST);

        mockMvc.perform(get("/api/v1/user"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .string(objectMapper.writeValueAsString(List.of(USER_1_RESPONSE, USER_2_RESPONSE))));
    }

    @Test
    void getUserReturnsOk() throws Exception {
        when(service.getUser(1L)).thenReturn(USER_1);

        mockMvc.perform(get("/api/v1/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(USER_1_RESPONSE)));
    }

    @Test
    void getUserCalledWithNonExistentUserId() throws Exception {
        when(service.getUser(3L)).thenThrow(new UserNotFoundException(3L));

        mockMvc.perform(get("/api/v1/user/3"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error_message").value("User with id 3 does not exist."));
    }

    @Test
    void deleteUserReturnsOk() throws Exception {
        when(service.getUser(1L)).thenReturn(USER_1);

        mockMvc.perform(delete("/api/v1/user/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUserCalledWithNonExistentUserId() throws Exception {
        when(service.deleteUser(16L)).thenThrow(new UserNotFoundException(16L));

        mockMvc.perform(delete("/api/v1/user/16"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error_message").value("User with id 16 does not exist."));
    }

    @Test
    void registerUserReturnsOk() throws Exception {
        when(service.addNewUser(any())).thenReturn(USER_1);

        mockMvc.perform(post("/api/v1/user")
                        .content(objectMapper.writeValueAsString(USER_1_REQUEST))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .string(objectMapper.writeValueAsString(USER_1_RESPONSE)));
    }

    @Test
    void registerUserThrowsEmailTakenException() throws Exception {
        when(service.addNewUser(any())).thenThrow(new EmailTakenException("laci@freemail.com"));

        mockMvc.perform(post("/api/v1/user")
                        .content(objectMapper.writeValueAsString(USER_1_REQUEST))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("error_message")
                        .value("The email(laci@freemail.com) is already taken."));
    }

    @Test
    void registerUserCalledWithInvalidNameInput() throws Exception {
        mockMvc.perform(post("/api/v1/user")
                        .content(objectMapper.writeValueAsString(BAD_USER_REQUEST))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error_message")
                        .value("Error: The field \"name\" is invalid, reason: name can not be blank"));
    }

    @Test
    void registerUserCalledWithInvalidNameAndEmailInput() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/user")
                        .content(objectMapper.writeValueAsString(BAD_USER_REQUEST_2))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();

        assertTrue(result.getResponse().getContentAsString()
                .contains("Error: The field \\\"email\\\" is invalid, reason: email can not be blank"));
        assertTrue(result.getResponse().getContentAsString()
                .contains("Error: The field \\\"name\\\" is invalid, reason: name can not be blank"));
    }


    @Test
    void updateUserReturnsOk() throws Exception {
        when(service.updateUser(eq(1L), any())).thenReturn(USER_1_MOD);

        mockMvc.perform(put("/api/v1/user/1")
                        .content(objectMapper.writeValueAsString(USER_1_REQUEST_MOD))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .string(objectMapper.writeValueAsString(USER_1_RESPONSE_MOD)));
    }

    @Test
    void updateUserThrowsUserNotFoundException() throws Exception {
        when(service.updateUser(anyLong(), any())).thenThrow(new UserNotFoundException(16L));

        mockMvc.perform(put("/api/v1/user/16")
                        .content(objectMapper.writeValueAsString(USER_1_REQUEST))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error_message")
                        .value("User with id 16 does not exist."));
    }

    @Test
    void updateUserThrowsEmailTakenException() throws Exception {
        when(service.updateUser(anyLong(), any())).thenThrow(new EmailTakenException(USER_1_REQUEST.getEmail()));

        mockMvc.perform(put("/api/v1/user/1")
                        .content(objectMapper.writeValueAsString(USER_1_REQUEST))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("error_message")
                        .value("The email(laci@freemail.com) is already taken."));
    }

    @Test
    void updateUserCalledWithInvalidNameInput() throws Exception {
        mockMvc.perform(put("/api/v1/user/1")
                        .content(objectMapper.writeValueAsString(BAD_USER_REQUEST))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error_message")
                        .value("Error: The field \"name\" is invalid, reason: name can not be blank"));
    }

    @Test
    void updateUserCalledWithInvalidNameAndEmailInput() throws Exception {
        MvcResult result = mockMvc.perform(put("/api/v1/user/1")
                        .content(objectMapper.writeValueAsString(BAD_USER_REQUEST_2))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();

        assertTrue(result.getResponse().getContentAsString()
                .contains("Error: The field \\\"email\\\" is invalid, reason: email can not be blank"));
        assertTrue(result.getResponse().getContentAsString()
                .contains("Error: The field \\\"name\\\" is invalid, reason: name can not be blank"));

    }

}