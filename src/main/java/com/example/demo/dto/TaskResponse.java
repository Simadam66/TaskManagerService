package com.example.demo.dto;

import com.example.demo.model.task.Task;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public class TaskResponse {

    @JsonProperty("id")
    @Schema(example = "101")
    private Long id;

    @JsonProperty("name")
    @Schema(example = "Mission")
    private String name;

    @JsonProperty("description")
    @Schema(example = "Protect John C.")
    private String description;

    @JsonProperty("date")
    @Schema(example = "1984-06-30")
    private LocalDate date;

    public static TaskResponse of(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .date(task.getDate())
                .build();
    }
}
