package com.example.demo.task;

import com.example.demo.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;


@ToString
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
}
