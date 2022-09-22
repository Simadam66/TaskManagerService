package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("user with id " + userId + "does not exist"));
        return user;
    }

    public User addNewUser(UserRequest userRequest) {
        Optional<User> userOptional = userRepository.findUserByEmail(userRequest.getEmail());
        if (userOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        }
        User userToSave = User.of(userRequest);
        userRepository.save(userToSave);
        return userToSave;
    }

    public User deleteStudent(Long userId) {
        User user = getUser(userId);
        userRepository.deleteById(userId);
        return user;
    }

    @Transactional
    public User updateUser(Long userId, UserRequest userRequest) {
        User userToUpdate = getUser(userId);

        if (!userToUpdate.getEmail().equals(userRequest.getEmail())) {
            Optional<User> userOptional = userRepository.findUserByEmail(userRequest.getEmail());
            if (userOptional.isPresent()) {
                throw new IllegalStateException("email taken");
            }
        }

        userToUpdate.update(userRequest);
        userRepository.save(userToUpdate);
        return userToUpdate;
    }
}
