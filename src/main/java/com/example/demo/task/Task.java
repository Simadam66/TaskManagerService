package com.example.demo.task;

import com.example.demo.user.User;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;

import javax.persistence.*;
import java.time.LocalDate;

@EqualsAndHashCode(exclude = "id")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
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

    @Column(name = "id")
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

    public boolean update(TaskRequest taskRequest) {
        boolean equals = new EqualsBuilder()
                .append(this.name, taskRequest.getName())
                .append(this.description, taskRequest.getDescription())
                .append(this.date_time, taskRequest.getDate_time())
                .isEquals();

        if (!equals) {
            this.name = taskRequest.getName();
            this.description = taskRequest.getDescription();
            this.date_time = taskRequest.getDate_time();
        }
        return !equals;
    }

    public void assignToUser(User user) {
        this.user = user;
    }

    public void removeFromUser() {
        this.user = null;
    }
}
