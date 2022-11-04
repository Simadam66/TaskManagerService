package com.example.demo.service;

import com.example.demo.exception.TaskNotFoundException;
import com.example.demo.exception.TaskMismatchException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.task.Task;
import com.example.demo.model.task.TaskRepository;
import com.example.demo.dto.TaskRequest;
import com.example.demo.model.user.User;
import com.example.demo.model.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@CacheConfig(cacheNames = "task_cache")
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserService userService, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public List<Task> getUserTasks(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return user.getTasks();
    }

    @Cacheable(key = "#taskId")
    public Task getTask(Long userId, Long taskId) {
        List<Task> userTasks = getUserTasks(userId);

        taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        Optional<Task> task = userTasks.stream().filter(t -> t.getId().equals(taskId)).findFirst();
        if (task.isEmpty()) {
            throw new TaskMismatchException(taskId);
        }

        return task.get();
    }

    public Task addNewTask(Long userId, TaskRequest taskRequest) {
        Task newTask = Task.of(taskRequest);
        userService.addUserTask(userId, newTask);
        taskRepository.save(newTask);
        return newTask;
    }

    @CacheEvict(key = "#taskId")
    public boolean deleteTask(Long userId, Long taskId) {
        Task task = getTask(userId, taskId);
        userService.removeUserTask(userId, task);
        taskRepository.deleteById(taskId);
        return true;
    }

    @CachePut(key = "#taskId")
    @Transactional
    public Task updateTask(Long userId, Long taskId, TaskRequest taskRequest) {
        Task taskToUpdate = getTask(userId, taskId);
        if (taskToUpdate.update(taskRequest)) {
            taskRepository.save(taskToUpdate);
        }
        return taskToUpdate;
    }

}
