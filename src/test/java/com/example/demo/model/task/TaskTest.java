package com.example.demo.model.task;

import com.example.demo.dto.TaskRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static java.time.Month.OCTOBER;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

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
    void updateDoesNotChangeTask() {
        TASK_1.update(TASK_1_REQUEST_SAME);

        assertEquals(TASK_1.getName(), TASK_1_REQUEST_SAME.getName());
        assertEquals(TASK_1.getDescription(), TASK_1_REQUEST_SAME.getDescription());
        assertEquals(TASK_1.getDate(), TASK_1_REQUEST_SAME.getDate());
    }

    @Test
    void updateChangesTaskBasedOnRequest() {
        TASK_1.update(TASK_1_REQUEST_MOD);

        assertEquals(TASK_1.getName(), TASK_1_REQUEST_MOD.getName());
        assertEquals(TASK_1.getDescription(), TASK_1_REQUEST_MOD.getDescription());
        assertEquals(TASK_1.getDate(), TASK_1_REQUEST_MOD.getDate());
    }

}