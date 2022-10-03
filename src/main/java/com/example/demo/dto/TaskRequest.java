package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
public class TaskRequest {

    @NotBlank(message = "name can not be blank")
    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @NotNull
    @JsonProperty("date_time")
    private LocalDate date_time;

}