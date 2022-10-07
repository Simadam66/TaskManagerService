package com.example.demo.model.user;

import com.example.demo.dto.UserRequest;
import com.example.demo.model.task.Task;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Builder
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@EqualsAndHashCode(exclude = "id")
public class User {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "birthdate")
    private LocalDate birthDate;

    @Transient
    private Integer age;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    public User(String name, String email, LocalDate birthDate) {
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
    }

    public Integer getAge() {
        return Period.between(this.birthDate, LocalDate.now()).getYears();
    }

    public static User of(UserRequest userRequest) {
        return User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .birthDate(userRequest.getBirthDate())
                .build();
    }

    public boolean update(UserRequest userRequest) {
        boolean equals = new EqualsBuilder()
                .append(this.name, userRequest.getName())
                .append(this.email, userRequest.getEmail())
                .append(this.birthDate, userRequest.getBirthDate())
                .isEquals();

        if (!equals) {
            this.name = userRequest.getName();
            this.email = userRequest.getEmail();
            this.birthDate = userRequest.getBirthDate();
            return true;
        }
        return false;

        /*String newEmail = userRequest.getEmail();
        if (newEmail != null && !this.email.equals(newEmail)) {
            this.email = newEmail;
        }*/
    }

    public void addTask(Task task) {
        tasks.add(task);
        task.assignToUser(this);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
        task.removeFromUser();
    }
}
