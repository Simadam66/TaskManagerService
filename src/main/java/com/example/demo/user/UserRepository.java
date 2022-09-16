package com.example.demo.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    // SELECT * FROM student WHERE email = ? ezt csinalja hatterben
    //@Query("SELECT u FROM User u WHERE u.email = ?1") masik opcio
    Optional<User> findUserByEmail(String email);

    //Page<User> findAll(Pageable pageable);

    List<User> findAll();
}
