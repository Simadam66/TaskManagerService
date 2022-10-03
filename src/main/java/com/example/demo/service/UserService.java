package com.example.demo.service;

import com.example.demo.exception.EmailTakenException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.task.Task;
import com.example.demo.model.user.User;
import com.example.demo.model.user.UserRepository;
import com.example.demo.dto.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
                .orElseThrow(() -> new UserNotFoundException(userId));
        return user;
    }

    public User addNewUser(UserRequest userRequest) {
        Optional<User> userOptional = userRepository.findUserByEmail(userRequest.getEmail());
        if (userOptional.isPresent()) {
            throw new EmailTakenException();
        }
        User userToSave = User.of(userRequest);
        userRepository.save(userToSave);
        return userToSave;
    }

    public boolean deleteUser(Long userId) {
        User user = getUser(userId);
        userRepository.deleteById(userId);
        return true;
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

        if (userToUpdate.update(userRequest)) {
            userRepository.save(userToUpdate);
        }
        return userToUpdate;
    }

    public boolean removeUserTask(Long userId, Task task) {
        getUser(userId).removeTask(task);
        return true;
    }

    public boolean addUserTask(Long userId, Task task) {
        getUser(userId).addTask(task);
        return true;
    }
}
