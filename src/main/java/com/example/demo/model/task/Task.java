package com.example.demo.model.task;

import com.example.demo.dto.TaskRequest;
import com.example.demo.model.user.User;
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

    @Column(name = "date")
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public static Task of(TaskRequest taskRequest) {
        return Task.builder()
                .name(taskRequest.getName())
                .description(taskRequest.getDescription())
                .date(taskRequest.getDate())
                .build();
    }

    public boolean update(TaskRequest taskRequest) {
        boolean equals = new EqualsBuilder()
                .append(this.name, taskRequest.getName())
                .append(this.description, taskRequest.getDescription())
                .append(this.date, taskRequest.getDate())
                .isEquals();

        if (!equals) {
            this.name = taskRequest.getName();
            this.description = taskRequest.getDescription();
            this.date = taskRequest.getDate();
            return true;
        }
        return false;
    }

    public void assignToUser(User user) {
        this.user = user;
    }

    public void removeFromUser() {
        this.user = null;
    }
}
