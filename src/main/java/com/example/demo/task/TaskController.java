package com.example.demo.task;

import com.example.demo.user.User;
import com.example.demo.user.UserRequest;
import com.example.demo.user.UserResponse;
import com.example.demo.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/user/{userId}/task")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getUserTasks(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(
                taskService.getUserTasks(userId)
                        .stream()
                        .map(TaskResponse::of)
                        .toList()
                );
    }

    @GetMapping(path = "{taskId}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable("userId") Long userId,
                                                @PathVariable("taskId") Long taskId) {
        return ResponseEntity.ok(TaskResponse.of(taskService.getTask(userId, taskId)));
    }

    // TODO: validation, Postman: coud not get response, de mukodik
    @PostMapping
    public ResponseEntity<Task> registerNewTask(@PathVariable("userId") Long userId,
                                                @RequestBody /*@Valid*/  TaskRequest taskRequest) {
        Task newTask = taskService.addNewTask(userId,taskRequest);
        return new ResponseEntity<>(newTask, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable("userId") Long userId,
                                             @PathVariable("taskId") Long taskId) {
        taskService.deleteTask(userId,taskId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // TODO: validation, Postman: coud not get response, de mukodik
    @PutMapping(path = "{taskId}")
    public ResponseEntity<Task> updateTask(
            @PathVariable("userId") Long userId,
            @PathVariable("taskId") Long taskId,
            @RequestBody /*@Valid*/ TaskRequest taskRequest) {
        Task updatedTask = taskService.updateTask(userId, taskId, taskRequest);
        return new ResponseEntity<>(updatedTask,HttpStatus.OK);
    }
}
