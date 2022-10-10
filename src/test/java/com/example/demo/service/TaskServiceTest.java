package com.example.demo.service;

import com.example.demo.dto.TaskRequest;
import com.example.demo.exception.TaskMismatchException;
import com.example.demo.exception.TaskNotFoundException;
import com.example.demo.model.task.Task;
import com.example.demo.model.task.TaskRepository;
import com.example.demo.model.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static java.time.Month.JANUARY;
import static java.time.Month.OCTOBER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    TaskRepository taskRepository;

    @Mock
    UserService userService;

    @InjectMocks
    TaskService taskService;

    private static final User USER_1 =
            User.builder()
                    .id(1L)
                    .name("Laci")
                    .email("laci@freemail.com")
                    .birthDate(LocalDate.of(1997, JANUARY, 12))
                    .tasks(new ArrayList<>())
                    .build();

    private static final Long TASK_ID = 2L;

    private static final Task TASK_1 =
            Task.builder()
                    .id(1L)
                    .name("Birthday")
                    .description("Buy a cake for Toms birthday")
                    .date(LocalDate.of(2022, OCTOBER, 5))
                    .build();

    private static final TaskRequest TASK_1_REQUEST_SAME =
            TaskRequest.builder()
                    .name("Birthday")
                    .description("Buy a cake for Toms birthday")
                    .date(LocalDate.of(2022, OCTOBER, 5))
                    .build();

    private static final TaskRequest TASK_1_REQUEST_MOD =
            TaskRequest.builder()
                    .name("B-Day")
                    .description("Buy a cake for Tommy birthday")
                    .date(LocalDate.of(2022, OCTOBER, 5))
                    .build();

    @Test
    void getTaskThrowsTaskNotFoundException() {
        when(userService.getUser(USER_1.getId())).thenReturn(USER_1);

        TaskNotFoundException exc =
                assertThrows(TaskNotFoundException.class,() -> taskService.getTask(USER_1.getId(), TASK_ID));
        assertEquals("Task with id " + TASK_ID + " does not exist.", exc.getMessage());
        verify(taskRepository, times(1)).findById(any());
    }

    @Test
    void getTaskThrowsTaskMismatchException() {
        when(userService.getUser(USER_1.getId())).thenReturn(USER_1);
        when(taskRepository.findById(TASK_1.getId())).thenReturn(Optional.of(TASK_1));

        TaskMismatchException exc =
                assertThrows(TaskMismatchException.class,() -> taskService.getTask(USER_1.getId(), TASK_1.getId()));
        assertEquals("This user does not have a task with id: " + TASK_1.getId() + ".", exc.getMessage());
        verify(taskRepository, times(1)).findById(any());
    }

    @Test
    void getTaskReturnsRightTask() {
        User userWithTask1 = User.builder()
                        .id(1L)
                        .name("Laci")
                        .email("laci@freemail.com")
                        .birthDate(LocalDate.of(1997, JANUARY, 12))
                        .tasks(new ArrayList<>())
                        .build();
        userWithTask1.addTask(TASK_1);
        when(userService.getUser(userWithTask1.getId())).thenReturn(userWithTask1);
        when(taskRepository.findById(TASK_1.getId())).thenReturn(Optional.of(TASK_1));

        Task result = taskService.getTask(userWithTask1.getId(), TASK_1.getId());

        assertEquals(TASK_1, result);
        verify(taskRepository, times(1)).findById(any());
    }

    @Test
    void updateTaskDoesNotSaveChanges() {
        User userWithTask1 = User.builder()
                .id(1L)
                .name("Laci")
                .email("laci@freemail.com")
                .birthDate(LocalDate.of(1997, JANUARY, 12))
                .tasks(new ArrayList<>())
                .build();
        userWithTask1.addTask(TASK_1);
        when(userService.getUser(userWithTask1.getId())).thenReturn(userWithTask1);
        when(taskRepository.findById(TASK_1.getId())).thenReturn(Optional.of(TASK_1));

        Task result = taskService.updateTask(USER_1.getId(), TASK_1.getId(), TASK_1_REQUEST_SAME);

        assertEquals(TASK_1_REQUEST_SAME.getName(), result.getName());
        assertEquals(TASK_1_REQUEST_SAME.getDescription(), result.getDescription());
        assertEquals(TASK_1_REQUEST_SAME.getDate(), result.getDate());
        verify(taskRepository, never()).save(any());
    }

    @Test
    void updateTaskSaveChanges() {
        User userWithTask1 = User.builder()
                .id(1L)
                .name("Laci")
                .email("laci@freemail.com")
                .birthDate(LocalDate.of(1997, JANUARY, 12))
                .tasks(new ArrayList<>())
                .build();

        Task taskToUpdate = Task.builder()
                .id(1L)
                .name("Birthday")
                .description("Buy a cake for Toms birthday")
                .date(LocalDate.of(2022, OCTOBER, 5))
                .build();

        userWithTask1.addTask(taskToUpdate);
        when(userService.getUser(userWithTask1.getId())).thenReturn(userWithTask1);
        when(taskRepository.findById(taskToUpdate.getId())).thenReturn(Optional.of(taskToUpdate));

        Task result = taskService.updateTask(USER_1.getId(), taskToUpdate.getId(), TASK_1_REQUEST_MOD);

        assertEquals(TASK_1_REQUEST_MOD.getName(), result.getName());
        assertEquals(TASK_1_REQUEST_MOD.getDescription(), result.getDescription());
        assertEquals(TASK_1_REQUEST_MOD.getDate(), result.getDate());
        verify(taskRepository, times(1)).save(taskToUpdate);
    }
}