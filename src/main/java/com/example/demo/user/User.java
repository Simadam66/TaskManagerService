package com.example.demo.user;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;

@Builder
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
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

    @Column(name = "birthDate")
    private LocalDate birthDate;

    @Transient
    private Integer age;

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

    public User update(UserRequest userRequest) {
        this.name = userRequest.getName();
        this.email = userRequest.getEmail();
        this.birthDate = userRequest.getBirthDate();
        return this;

        /*String newEmail = userRequest.getEmail();
        if (newEmail != null && !this.email.equals(newEmail)) {
            this.email = newEmail;
        }*/
    }

}
