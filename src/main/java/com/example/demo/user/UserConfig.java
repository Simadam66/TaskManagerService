package com.example.demo.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

import static java.time.Month.FEBRUARY;
import static java.time.Month.JANUARY;

@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository repository) {
        return args -> {
            User mariah = new User(
                    "Mariah",
                    "mariah.jamal@gmail.com",
                    LocalDate.of(2000, JANUARY, 5)
            );

            User alex = new User(
                    "Alex",
                    "alex.johnson@freemail.com",
                    LocalDate.of(2004, FEBRUARY, 8)
            );

            User lajos = new User(
                    "Lajos",
                    "lajos.skywalker@citromail.com",
                    LocalDate.of(1980, FEBRUARY, 23)
            );

            repository.saveAll(
                    List.of(mariah, alex, lajos)
            );

        };
    }
}
