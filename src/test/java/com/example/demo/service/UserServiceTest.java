package com.example.demo.service;

import com.example.demo.cache.RedisClient;
import com.example.demo.dto.UserRequest;
import com.example.demo.exception.EmailTakenException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.user.User;
import com.example.demo.model.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository repo;

    @InjectMocks
    UserService service;

    @Mock
    RedisClient redis;

    private static final Long USER_ID_1 = 1L;
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

    private static final User USER_2 =
            User.builder()
                    .name("Laszlo")
                    .email("lacesz@freemail.com")
                    .birthDate(LocalDate.of(2001, SEPTEMBER, 4))
                    .build();

    @Test
    void getUsersReturnsAllOfTheUsers() {
        service.getUsers();

        verify(repo, times(1)).findAll();
    }

    @Test
    void getUserThrowsUserNotFoundException() {
        when(repo.findById(anyLong())).thenReturn(Optional.empty());

        UserNotFoundException exc = assertThrows(UserNotFoundException.class, () -> service.getUser(USER_ID_1));
        assertEquals("User with id " + USER_ID_1 + " does not exist.", exc.getMessage());
    }

    @Test
    void getUserReturnsRightUser() {
        when(repo.findById(anyLong())).thenReturn(Optional.of(USER_1));

        User user = service.getUser(USER_ID_1);

        verify(repo, times(1)).findById(USER_ID_1);
        assertEquals(USER_1, user);
    }


    @Test
    void addNewUserThrowsEmailTakenException() {
        when(repo.findUserByEmail(USER_1_REQUEST.getEmail())).thenReturn(Optional.of(USER_1));

        EmailTakenException exc = assertThrows(EmailTakenException.class, () -> service.addNewUser(USER_1_REQUEST));
        assertEquals("The email(" + USER_1_REQUEST.getEmail() + ") is already taken.", exc.getMessage());
        verify(repo, never()).save(any());
    }

    @Test
    void addNewUserAddsANewUser() {
        when(repo.findUserByEmail(USER_1_REQUEST.getEmail())).thenReturn(Optional.empty());

        User user = service.addNewUser(USER_1_REQUEST);

        verify(repo, times(1)).findUserByEmail(USER_1_REQUEST.getEmail());
        verify(repo, times(1)).save(user);
        assertEquals(USER_1, user);
    }

    @Test
    void updateUserThrowsEmailTakenException() {
        when(repo.findById(any())).thenReturn(Optional.of(USER_1));
        when(repo.findUserByEmail(USER_1_REQUEST_MOD.getEmail())).thenReturn(Optional.of(USER_2));

        EmailTakenException exc = assertThrows(EmailTakenException.class, () -> service.updateUser(USER_ID_1 ,USER_1_REQUEST_MOD));
        assertEquals("The email(" + USER_1_REQUEST_MOD.getEmail() + ") is already taken.", exc.getMessage());
        verify(repo, times(1)).findUserByEmail(USER_1_REQUEST_MOD.getEmail());
        verify(repo, never()).save(any());
    }

    @Test
    void updateUserUpdatesUsersNewEmailAddress() {
        User userToUpdate = User.builder()
                .name("Laci")
                .email("laci@freemail.com")
                .birthDate(LocalDate.of(1997, JANUARY, 12)).build();
        when(repo.findById(any())).thenReturn(Optional.of(userToUpdate));
        when(repo.findUserByEmail(USER_1_REQUEST_MOD.getEmail())).thenReturn(Optional.empty());

        service.updateUser(USER_ID_1, USER_1_REQUEST_MOD);

        verify(repo, times(1)).findUserByEmail(USER_1_REQUEST_MOD.getEmail());
        verify(repo, times(1)).save(any());
        assertEquals(USER_1_REQUEST_MOD.getEmail(), userToUpdate.getEmail());
    }

    @Test
    void updateUserDoesNotUpdateAnyFieldOfTheUserBasedOnTheRequest() {
        when(repo.findById(any())).thenReturn(Optional.of(USER_1));

        service.updateUser(USER_1.getId(), USER_1_REQUEST);

        verify(repo, never()).findUserByEmail(any());
        verify(repo, never()).save(any());
        assertEquals(USER_1_REQUEST.getEmail(), USER_1.getEmail());
    }

}