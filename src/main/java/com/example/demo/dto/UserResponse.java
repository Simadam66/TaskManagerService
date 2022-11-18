package com.example.demo.dto;

import com.example.demo.model.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public class UserResponse {

    @JsonProperty("id")
    @Schema(example = "101")
    private Long id;

    @JsonProperty("name")
    @Schema(example = "Arnold")
    private String name;

    @JsonProperty("email")
    @Schema(example = "Terminator@freemail.com")
    private String email;

    @JsonProperty("birth_date")
    @Schema(example = "1947-07-30")
    private LocalDate birthDate;

    public static UserResponse of(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .birthDate(user.getBirthDate())
                .build();
    }
}
