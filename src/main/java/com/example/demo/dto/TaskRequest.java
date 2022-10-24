package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Builder
@Getter
public class TaskRequest {

    @NotBlank(message = "name can not be blank")
    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @NotNull(message = "date can not be null")
    @JsonProperty("date")
    private LocalDate date;

}
