package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(example = "Mission")
    private String name;

    @JsonProperty("description")
    @Schema(example = "Protect John C.")
    private String description;

    @NotNull(message = "date can not be null")
    @JsonProperty("date")
    @Schema(example = "1984-06-30")
    private LocalDate date;

}
