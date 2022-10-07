package com.example.demo.model.user;

import com.example.demo.dto.UserRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static java.time.Month.JANUARY;
import static java.time.Month.SEPTEMBER;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private static final User USER_1 =
            User.builder()
                    .name("Laci")
                    .email("laci@freemail.com")
                    .birthDate(LocalDate.of(1997, JANUARY, 12))
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
                    .email("lacesz@freemail.com")
                    .birthDate(LocalDate.of(1997, JANUARY, 12))
                    .build();

    @Test
    void getAge() {
        int expected = 25;

        int result = USER_1.getAge();

        assertEquals(expected, result);
    }

    @Test
    void updateDoesNotChangeUser() {
        USER_1.update(USER_1_REQUEST);

        assertEquals(USER_1.getName(), USER_1_REQUEST.getName());
        assertEquals(USER_1.getEmail(), USER_1_REQUEST.getEmail());
        assertEquals(USER_1.getBirthDate(), USER_1_REQUEST.getBirthDate());
    }

    @Test
    void updateChangesUserBasedOnRequest() {
        USER_1.update(USER_1_REQUEST_MOD);

        assertEquals(USER_1.getName(), USER_1_REQUEST_MOD.getName());
        assertEquals(USER_1.getEmail(), USER_1_REQUEST_MOD.getEmail());
        assertEquals(USER_1.getBirthDate(), USER_1_REQUEST_MOD.getBirthDate());
    }

}