package com.example.demo.service;

import com.example.demo.cache.RedisClient;
import com.example.demo.exception.EmailTakenException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.task.Task;
import com.example.demo.model.user.User;
import com.example.demo.model.user.UserRepository;
import com.example.demo.dto.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@CacheConfig(cacheNames = "user_cache")
@Service
public class UserService {
    private final UserRepository userRepository;

    private final RedisClient redis;

    @Autowired
    public UserService(UserRepository userRepository, RedisClient redis) {
        this.userRepository = userRepository;
        this.redis = redis;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Cacheable(key = "#userId")
    public User getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return user;
    }

    public User addNewUser(UserRequest userRequest) {
        Optional<User> userOptional = userRepository.findUserByEmail(userRequest.getEmail());
        if (userOptional.isPresent()) {
            throw new EmailTakenException(userOptional.get().getEmail());
        }
        User userToSave = User.of(userRequest);
        userRepository.save(userToSave);

        redis.cacheUser(userToSave);
        return userToSave;
    }

    @CacheEvict(key = "#userId")
    public boolean deleteUser(Long userId) {
        User user = getUser(userId);
        userRepository.deleteById(userId);
        return true;
    }

    @CachePut(key = "#userId")
    @Transactional
    public User updateUser(Long userId, UserRequest userRequest) {
        User userToUpdate = getUser(userId);

        if (!userToUpdate.getEmail().equals(userRequest.getEmail())) {
            Optional<User> userOptional = userRepository.findUserByEmail(userRequest.getEmail());
            if (userOptional.isPresent()) {
                throw new EmailTakenException(userOptional.get().getEmail());
            }
        }

        if (userToUpdate.update(userRequest)) {
            userRepository.save(userToUpdate);
        }
        return userToUpdate;
    }

    public User getUserWithTasks(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return user;
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
