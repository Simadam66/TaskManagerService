package com.example.demo.task;

import com.example.demo.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "tasks")
@ToString(exclude = "user")
public class Task {

    @Id
    @SequenceGenerator(
            name = "task_sequence",
            sequenceName = "task_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "task_sequence"
    )
    @Column(name = "Id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "date_time")
    private LocalDate date_time;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Task(String name, String description, LocalDate date_time) {
        this.name = name;
        this.description = description;
        this.date_time = date_time;
    }

    public static Task of(TaskRequest taskRequest) {
        return Task.builder()
                .name(taskRequest.getName())
                .description(taskRequest.getDescription())
                .date_time(taskRequest.getDate_time())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        return id != null && id.equals(((Task) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public Task update(TaskRequest taskRequest) {
        this.name = taskRequest.getName();
        this.description = taskRequest.getDescription();
        this.date_time = taskRequest.getDate_time();
        return this;
    }
}
