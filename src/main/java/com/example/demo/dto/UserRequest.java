package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String name;

    @NotBlank(message = "email can not be blank")
    @JsonProperty("email")
    private String email;

    @JsonProperty("birth_date")
    private LocalDate birthDate;
}
