package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@Getter
public class UserRequest {

    @NotBlank(message = "name can not be blank")
    @JsonProperty("name")
    @Schema(example = "Arnold")
    private String name;

    @NotBlank(message = "email can not be blank")
    @JsonProperty("email")
    @Schema(example = "Terminator@freemail.com")
    private String email;

    @JsonProperty("birth_date")
    @Schema(example = "1947-07-30")
    private LocalDate birthDate;
}
