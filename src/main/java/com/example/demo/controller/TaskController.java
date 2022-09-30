package com.example.demo.controller;

import com.example.demo.model.task.Task;
import com.example.demo.dto.TaskRequest;
import com.example.demo.dto.TaskResponse;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PostMapping
    public ResponseEntity<TaskResponse> registerNewTask(@PathVariable("userId") Long userId,
                                                        @RequestBody @Valid TaskRequest taskRequest) {
        Task newTask = taskService.addNewTask(userId, taskRequest);
        TaskResponse response = TaskResponse.of(newTask);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable("userId") Long userId,
                                             @PathVariable("taskId") Long taskId) {
        taskService.deleteTask(userId, taskId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "{taskId}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable("userId") Long userId,
            @PathVariable("taskId") Long taskId,
            @RequestBody @Valid TaskRequest taskRequest) {
        Task updatedTask = taskService.updateTask(userId, taskId, taskRequest);
        return ResponseEntity.ok(TaskResponse.of(updatedTask));
    }
}
