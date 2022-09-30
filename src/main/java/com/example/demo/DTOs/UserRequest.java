package com.example.demo.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

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
