package com.example.demo.DTOs;

import com.example.demo.Models.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
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

    @Builder
    public static class UserResponse {

        @JsonProperty("id")
        private Long id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("email")
        private String email;

        @JsonProperty("birth_date")
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
}
