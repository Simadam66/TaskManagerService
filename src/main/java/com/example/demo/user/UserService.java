package com.example.demo.user;

import com.example.demo.task.Task;
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

    public User addNewUser(User user) {
        Optional<User> userOptional = userRepository.findUserByEmail(user.getEmail());
        if (userOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        }
        userRepository.save(user);
        return user;
    }

    public User deleteUser(Long userId) {
        User user = getUser(userId);
        userRepository.deleteById(userId);
        return user;
    }

    @Transactional
    public User updateUser(Long userId, User updatedUser) {
        User user = getUser(userId);

        String newName = updatedUser.getName();
        if (newName != null && newName.length() > 0 && !Objects.equals(user.getName(), newName)) {
            user.setName(newName);
        }

        String newEmail = updatedUser.getEmail();
        if (newEmail != null && newEmail.length() > 0 && !Objects.equals(user.getEmail(), newEmail)) {
            Optional<User> userOptional = userRepository.findUserByEmail(newEmail);
            if (userOptional.isPresent()) {
                throw new IllegalStateException("email taken");
            }
            user.setEmail(newEmail);
        }

        LocalDate newDate = updatedUser.getBirthDate();
        if (newDate != null && !Objects.equals(user.getBirthDate(), newDate)) {
            user.setBirthDate(newDate);
        }

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
