package com.example.demo.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // SELECT * FROM student WHERE email = ? ezt csinalja hatterben
    //@Query("SELECT u FROM User u WHERE u.email = ?1") masik opcio
    Optional<User> findUserByEmail(String email);
}
