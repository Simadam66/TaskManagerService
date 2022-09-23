package com.example.demo.task;

import com.example.demo.user.User;
import com.example.demo.user.UserRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Optional;

//@Builder
@Setter
@Getter
@Entity
@Table(name = "tasks")
public class Task {

    // Formátum: "name":"My task","description" : "Description of task", "date_time" : "2016-05-25 14:25:00"

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

    //Task(String name ,String desc, LocalDate date, User user)

    public Task() {
    }

    public Task(Long id, String name, String description, LocalDate date_time) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date_time = date_time;
    }

    public Task(String name, String description, LocalDate date_time) {
        this.name = name;
        this.description = description;
        this.date_time = date_time;
    }

    // TODO: ez valoszinuleg igy nem jo
    public static Task of(TaskRequest taskRequest) {
        /*return Task.builder()
                .name(taskRequest.getName())
                .description(taskRequest.getDescription())
                .date_time(taskRequest.getDate_time())
                .build();*/
        return new Task(taskRequest.getName(), taskRequest.getDescription(), taskRequest.getDate_time());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task )) return false;
        return id != null && id.equals(((Task) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", date_time=" + date_time +
                '}';
    }

    public Task update(TaskRequest taskRequest) {
        this.name = taskRequest.getName();
        this.description = taskRequest.getDescription();
        this.date_time = taskRequest.getDate_time();
        return this;
    }
}
