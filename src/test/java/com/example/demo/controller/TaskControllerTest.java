package com.example.demo.controller;

import com.example.demo.dto.TaskRequest;
import com.example.demo.dto.TaskResponse;
import com.example.demo.dto.UserRequest;
import com.example.demo.exception.TaskMismatchException;
import com.example.demo.exception.TaskNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.task.Task;
import com.example.demo.model.user.User;
import com.example.demo.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    private static final TaskRequest TASK_1_REQUEST =
            TaskRequest.builder()
                    .name("Birthday")
                    .description("Buy a cake for Toms birthday")
                    .date(LocalDate.of(2022, OCTOBER, 5))
                    .build();

    private static final TaskRequest BAD_TASK_REQUEST =
            TaskRequest.builder()
                    .name("B-day")
                    .description("Buy a cake for Tommy birthday")
                    .build();

    private static final TaskRequest BAD_TASK_REQUEST_2 =
            TaskRequest.builder()
                    .description("Buy a cake for Tommy birthday")
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
        when(taskService.addNewTask(anyLong(), any())).thenReturn(TASK_1);

        mockMvc.perform(post("/api/v1/user/1/task")
                        .content(objectMapper.writeValueAsString(TASK_1_REQUEST))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .string(objectMapper.writeValueAsString(TASK_1_RESPONSE)));
    }

    @Test
    void registerNewTaskThrowsUserNotFoundException() throws Exception {
        when(taskService.addNewTask(anyLong(), any())).thenThrow(new UserNotFoundException(1L));

        mockMvc.perform(post("/api/v1/user/1/task")
                .content(objectMapper.writeValueAsString(TASK_1_REQUEST))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error_message").value("User with id 1 does not exist."));
    }

    @Test
    void registerNewTaskCalledWithInvalidDateInput() throws Exception {

        mockMvc.perform(post("/api/v1/user/1/task")
                        .content(objectMapper.writeValueAsString(BAD_TASK_REQUEST))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error_message")
                        .value("Error: The field \"date\" is invalid, reason: date can not be null"));
    }

    @Test
    void registerNewTaskCalledWithInvalidDateAndNameInput() throws Exception {

        MvcResult result = mockMvc.perform(post("/api/v1/user/1/task")
                        .content(objectMapper.writeValueAsString(BAD_TASK_REQUEST_2))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();

        assertTrue(result.getResponse().getContentAsString()
                .contains("Error: The field \\\"date\\\" is invalid, reason: date can not be null"));
        assertTrue(result.getResponse().getContentAsString()
                .contains("Error: The field \\\"name\\\" is invalid, reason: name can not be blank"));
    }

    @Test
    void updateTaskReturnsOk() throws Exception {
        when(taskService.updateTask(any(), any(), any())).thenReturn(TASK_1);

        mockMvc.perform(put("/api/v1/user/1/task/1")
                        .content(objectMapper.writeValueAsString(TASK_1_REQUEST))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .string(objectMapper.writeValueAsString(TASK_1_RESPONSE)));
    }

    @Test
    void updateTaskThrowsThrowsUserNotFoundException() throws Exception {
        when(taskService.updateTask(any(), any(), any())).thenThrow(new UserNotFoundException(1L));

        mockMvc.perform(put("/api/v1/user/1/task/1")
                .content(objectMapper.writeValueAsString(TASK_1_REQUEST))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error_message").value("User with id 1 does not exist."));
    }

    @Test
    void updateTaskThrowsTaskNotFoundException() throws Exception {
        when(taskService.updateTask(any(), any(), any())).thenThrow(new TaskNotFoundException(2L));

        mockMvc.perform(put("/api/v1/user/1/task/2")
                .content(objectMapper.writeValueAsString(TASK_1_REQUEST))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error_message").value("Task with id 2 does not exist."));
    }

    @Test
    void updateTaskThrowsTaskMismatchException() throws Exception {
        when(taskService.updateTask(any(), any(), any())).thenThrow(new TaskMismatchException(2L));

        mockMvc.perform(put("/api/v1/user/1/task/2")
                .content(objectMapper.writeValueAsString(TASK_1_REQUEST))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error_message").value("This user does not have a task with id: 2."));
    }

    @Test
    void updateTaskCalledWithInvalidDateInput() throws Exception {
        mockMvc.perform(put("/api/v1/user/1/task/1")
                        .content(objectMapper.writeValueAsString(BAD_TASK_REQUEST))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error_message")
                        .value("Error: The field \"date\" is invalid, reason: date can not be null"));
    }

    @Test
    void updateTaskCalledWithInvalidDateAndNameInput() throws Exception {
        MvcResult result = mockMvc.perform(put("/api/v1/user/1/task/1")
                        .content(objectMapper.writeValueAsString(BAD_TASK_REQUEST_2))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();

        assertTrue(result.getResponse().getContentAsString()
                .contains("Error: The field \\\"date\\\" is invalid, reason: date can not be null"));
        assertTrue(result.getResponse().getContentAsString()
                .contains("Error: The field \\\"name\\\" is invalid, reason: name can not be blank"));
    }


}