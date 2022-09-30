package com.example.demo.user;

import com.example.demo.task.Task;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

import static java.time.Month.*;

@Configuration
public class UserConfig {

    @Bean(name = "userInitializerBean")
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

            Task task1 = new Task(
                    "Birthday",
                    "Buy a cake for Toms birthday",
                    LocalDate.of(2022, OCTOBER, 5)
            );

            Task task2 = new Task(
                    "Cleaning",
                    "Dust the shelf and tables in the meeting room",
                    LocalDate.of(2022, SEPTEMBER, 20)
            );

            Task task3 = new Task(
                    "Code review",
                    "Look for typos in Georgs code",
                    LocalDate.of(2022, OCTOBER, 5)
            );

            mariah.addTask(task1);
            mariah.addTask(task2);
            alex.addTask(task3);

            repository.saveAll(
                    List.of(mariah, alex, lajos)
            );

        };
    }
}
