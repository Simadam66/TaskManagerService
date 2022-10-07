package com.example.demo.data;

import com.example.demo.model.task.Task;
import com.example.demo.model.user.User;
import com.example.demo.model.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

import static java.time.Month.*;

@Configuration
public class DbConfig {

    @Bean(name = "userInitializerBean")
    CommandLineRunner commandLineRunner(UserRepository repository) {
        return args -> {

            User mariah = User.builder()
                    .name("Mariah")
                    .email("mariah.jamal@gmail.com")
                    .birthDate(LocalDate.of(2000, JANUARY, 5))
                    .build();

            User alex = User.builder()
                    .name("Alex")
                    .email("alex.johnson@freemail.com")
                    .birthDate(LocalDate.of(2004, FEBRUARY, 8))
                    .build();

            User lajos = User.builder()
                    .name("Lajos")
                    .email("lajos.skywalker@citromail.com")
                    .birthDate(LocalDate.of(1980, FEBRUARY, 23))
                    .build();

            Task task1 = Task.builder()
                    .name("Birthday")
                    .description("Buy a cake for Toms birthday")
                    .date_time(LocalDate.of(2022, OCTOBER, 5))
                    .build();

            Task task2 = Task.builder()
                    .name("Cleaning")
                    .description("Dust the shelf and tables in the meeting room")
                    .date_time(LocalDate.of(2022, SEPTEMBER, 20))
                    .build();

            Task task3 = Task.builder()
                    .name("Code review")
                    .description("Look for typos in George's code")
                    .date_time(LocalDate.of(2022, OCTOBER, 5))
                    .build();

            mariah.addTask(task1);
            mariah.addTask(task2);
            alex.addTask(task3);

            repository.saveAll(
                    List.of(mariah, alex, lajos)
            );

        };
    }
}
