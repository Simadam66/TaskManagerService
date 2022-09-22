package com.example.demo.task;

import com.example.demo.user.User;
import com.example.demo.user.UserResponse;
import com.example.demo.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/user/{userId}/task")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getUserTasks(@PathVariable("userId") Long userId) {
        User user = userService.getUser(userId);
        return ResponseEntity.ok(
                user.getTasks()
                        .stream()
                        .map(TaskResponse::of)
                        .toList()
                );
    }
}
