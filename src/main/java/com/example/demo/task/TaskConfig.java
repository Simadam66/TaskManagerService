package com.example.demo.task;

import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

import static java.time.Month.*;

@Configuration
public class TaskConfig {

    @Bean(name = "taskInitializerBean")
    CommandLineRunner commandLineRunner(TaskRepository repository){
        return args -> {
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

            repository.saveAll(
                    List.of(task1,task2,task3)
            );

        };
    }
}
