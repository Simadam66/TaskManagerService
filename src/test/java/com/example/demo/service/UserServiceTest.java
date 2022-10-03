package com.example.demo.service;

import com.example.demo.model.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository repo;

    @InjectMocks
    UserService service;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void canGetUsers() {
        // given

        // when

        // then

    }

    @Test
    @Disabled
    void getUser() {
    }

    @Test
    @Disabled
    void addNewUser() {
    }

    @Test
    @Disabled
    void deleteUser() {
    }

    @Test
    @Disabled
    void updateUser() {
    }

    @Test
    @Disabled
    void removeUserTask() {
    }

    @Test
    @Disabled
    void addUserTask() {
    }
}