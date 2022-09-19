package com.example.demo.user;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;

@ToString
@Setter
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

    @Column(name = "email", unique = true)
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

    public static User update(UserRequest userRequest) {
        return new User(userRequest.getName(), userRequest.getEmail(), userRequest.getBirthDate());
    }

}
