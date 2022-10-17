package com.example.demo.controller;

import com.example.demo.dto.TaskResponse;
import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.exception.TaskMismatchException;
import com.example.demo.exception.TaskNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.task.Task;
import com.example.demo.model.user.User;
import com.example.demo.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.bind.v2.TODO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.Month.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    TaskService taskService;

    private static final User USER_WITH_TASK1_AND_TASK2 = User.builder()
            .id(1L)
            .name("Laci")
            .email("laci@freemail.com")
            .birthDate(LocalDate.of(1997, JANUARY, 12))
            .tasks(new ArrayList<>())
            .build();

    private static final Task TASK_1 =
            Task.builder()
                    .id(1L)
                    .name("Birthday")
                    .description("Buy a cake for Toms birthday")
                    .date(LocalDate.of(2022, OCTOBER, 5))
                    .build();

    private static final Task TASK_2 =
            Task.builder()
                    .id(2L)
                    .name("Pancake")
                    .description("Make Pancakes with jam")
                    .date(LocalDate.of(2022, DECEMBER, 23))
                    .build();

    private static final TaskResponse TASK_1_RESPONSE =
            TaskResponse.builder()
                    .id(1L)
                    .name("Birthday")
                    .description("Buy a cake for Toms birthday")
                    .date(LocalDate.of(2022, OCTOBER, 5))
                    .build();

    private static final TaskResponse TASK_2_RESPONSE =
            TaskResponse.builder()
                    .id(2L)
                    .name("Pancake")
                    .description("Make Pancakes with jam")
                    .date(LocalDate.of(2022, DECEMBER, 23))
                    .build();

    @BeforeAll
    static void init() {
        USER_WITH_TASK1_AND_TASK2.addTask(TASK_1);
        USER_WITH_TASK1_AND_TASK2.addTask(TASK_2);
    }

    @Test
    void getUserTasksReturnsOk() throws Exception {
        when(taskService.getUserTasks(USER_WITH_TASK1_AND_TASK2.getId())).thenReturn(List.of(TASK_1, TASK_2));

        mockMvc.perform(get("/api/v1/user/1/task"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .string(objectMapper.writeValueAsString(List.of(TASK_1_RESPONSE, TASK_2_RESPONSE))));
    }

    @Test
    void getUserTasksThrowsUserNotFoundException() throws Exception {
        when(taskService.getUserTasks(any())).thenThrow(new UserNotFoundException(1L));

        mockMvc.perform(get("/api/v1/user/1/task"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error_message").value("User with id 1 does not exist."));
    }

    @Test
    void getTaskReturnsOk() throws Exception {
        when(taskService.getTask(any(), any())).thenReturn(TASK_1);

        mockMvc.perform(get("/api/v1/user/1/task/1"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .string(objectMapper.writeValueAsString(TASK_1_RESPONSE)));
    }

    @Test
    void getTaskThrowsThrowsUserNotFoundException() throws Exception {
        when(taskService.getTask(any(), any())).thenThrow(new UserNotFoundException(1L));

        mockMvc.perform(get("/api/v1/user/1/task/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error_message").value("User with id 1 does not exist."));
    }

    @Test
    void getTaskThrowsTaskNotFoundException() throws Exception {
        when(taskService.getTask(any(), any())).thenThrow(new TaskNotFoundException(1L));

        mockMvc.perform(get("/api/v1/user/1/task/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error_message").value("Task with id 1 does not exist."));
    }

    @Test
    void getTaskThrowsTaskMismatchException() throws Exception {
        when(taskService.getTask(any(), any())).thenThrow(new TaskMismatchException(3L));

        mockMvc.perform(get("/api/v1/user/1/task/3"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error_message").value("This user does not have a task with id: 3."));
    }

    @Test
    void deleteTaskReturnsOk() throws Exception {
        when(taskService.deleteTask(any(), any())).thenReturn(true);

        mockMvc.perform(delete("/api/v1/user/1/task/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTaskThrowsThrowsUserNotFoundException() throws Exception {
        when(taskService.deleteTask(any(), any())).thenThrow(new UserNotFoundException(2L));

        mockMvc.perform(delete("/api/v1/user/2/task/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error_message").value("User with id 2 does not exist."));
    }

    @Test
    void deleteTaskThrowsTaskNotFoundException() throws Exception {
        when(taskService.deleteTask(any(), any())).thenThrow(new TaskNotFoundException(2L));

        mockMvc.perform(delete("/api/v1/user/1/task/2"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error_message").value("Task with id 2 does not exist."));
    }

    @Test
    void deleteTaskThrowsTaskMismatchException() throws Exception {
        when(taskService.deleteTask(any(), any())).thenThrow(new TaskMismatchException(2L));

        mockMvc.perform(delete("/api/v1/user/1/task/2"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error_message").value("This user does not have a task with id: 2."));
    }

    @Test
    void registerNewTaskReturnsOk() throws Exception {

    }

    //TODO: folyt
    @Test
    void registerNewTaskThrowsUserNotFoundException() throws Exception {
        //when(taskService.addNewTask(any(), any())).thenThrow(new UserNotFoundException(2L));
        //"User with id " + userId + " does not exist."
    }

    // TODO: validation message, tobbi eset tesztel
    @Test
    void registerNewTaskCalledWithInvalidInput() throws Exception {

    }

    ////////////////////////////////////////////////////////////////////////

    @Test
    void updateTaskReturnsOk() throws Exception {

    }

    @Test
    void updateTaskThrowsThrowsUserNotFoundException() throws Exception {
        //"User with id " + userId + " does not exist."
    }

    @Test
    void updateTaskThrowsTaskNotFoundException() throws Exception {
        //"Task with id " + taskId + " does not exist."
    }

    @Test
    void updateTaskThrowsTaskMismatchException() throws Exception {
        //"This user does not have a task with id: " + taskId + "."
    }

    // TODO: validation message, tobbi eset tesztel
    @Test
    void updateTaskCalledWithInvalidInput() throws Exception {

    }
}