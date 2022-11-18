package com.example.demo.controller;

import com.example.demo.dto.UserResponse;
import com.example.demo.model.task.Task;
import com.example.demo.dto.TaskRequest;
import com.example.demo.dto.TaskResponse;
import com.example.demo.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Task Controller", description = "This controller is responsible for any operation in connection with the tasks.")
@RestController
@RequestMapping(path = "api/v1/user/{userId}/task")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation( summary = "Get all Tasks of a User by the its Id",
            parameters = { @Parameter(in = ParameterIn.PATH, name = "userId", description = "Id of the User", example = "1") },
            responses = { @ApiResponse(responseCode = "200", description = "Successful operation",content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TaskResponse.class)))}),
                          @ApiResponse(responseCode = "404", description = "Can not find User", content = @Content(schema = @Schema(hidden = true)))})
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getUserTasks(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(
                taskService.getUserTasks(userId)
                        .stream()
                        .map(TaskResponse::of)
                        .toList()
        );
    }

    @Operation( summary = "Get a one of a Users Tasks",
            parameters = { @Parameter(in = ParameterIn.PATH, name = "userId", description = "Id of the User", example = "1"),
                           @Parameter(in = ParameterIn.PATH, name = "taskId", description = "Id of the Task", example = "3")},
            responses = { @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TaskResponse.class))),
                          @ApiResponse(responseCode = "404", description = "Can not find User", content = @Content(schema = @Schema(hidden = true))),
                          @ApiResponse(responseCode = "404", description = "Can not find Task", content = @Content(schema = @Schema(hidden = true))),
                          @ApiResponse(responseCode = "400", description = "The requested Task does not belong to the User", content = @Content(schema = @Schema(hidden = true)))})
    @GetMapping(path = "{taskId}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable("userId") Long userId,
                                                @PathVariable("taskId") Long taskId) {
        return ResponseEntity.ok(TaskResponse.of(taskService.getTask(userId, taskId)));
    }

    @Operation( summary = "Add a new Task to a User",
            parameters = { @Parameter(in = ParameterIn.PATH, name = "userId", description = "Id of the User", example = "1") },
            responses = { @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TaskResponse.class))),
                          @ApiResponse(responseCode = "404", description = "Can not find User", content = @Content(schema = @Schema(hidden = true))),
                          @ApiResponse(responseCode = "400", description = "Invalid request-body", content = @Content(schema = @Schema(hidden = true)))})
    @PostMapping
    public ResponseEntity<TaskResponse> registerNewTask(@PathVariable("userId") Long userId,
                                                        @RequestBody @Valid TaskRequest taskRequest) {
        Task newTask = taskService.addNewTask(userId, taskRequest);
        TaskResponse response = TaskResponse.of(newTask);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation( summary = "Delete a Task of a User",
            parameters = { @Parameter(in = ParameterIn.PATH, name = "userId", description = "Id of the User", example = "1"),
                           @Parameter(in = ParameterIn.PATH, name = "taskId", description = "Id of the Task", example = "3")},
            responses = { @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(hidden = true))),
                          @ApiResponse(responseCode = "404", description = "Can not find User", content = @Content(schema = @Schema(hidden = true))),
                          @ApiResponse(responseCode = "404", description = "Can not find Task", content = @Content(schema = @Schema(hidden = true))),
                          @ApiResponse(responseCode = "400", description = "The requested Task does not belong to the User", content = @Content(schema = @Schema(hidden = true)))})
    @DeleteMapping(path = "{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable("userId") Long userId,
                                             @PathVariable("taskId") Long taskId) {
        taskService.deleteTask(userId, taskId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation( summary = "Update a User",
            parameters = { @Parameter(in = ParameterIn.PATH, name = "userId", description = "Id of the User", example = "1"),
                           @Parameter(in = ParameterIn.PATH, name = "taskId", description = "Id of the Task", example = "3")},
            responses = { @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TaskResponse.class))),
                          @ApiResponse(responseCode = "404", description = "Can not find User", content = @Content(schema = @Schema(hidden = true))),
                          @ApiResponse(responseCode = "404", description = "Can not find Task", content = @Content(schema = @Schema(hidden = true))),
                          @ApiResponse(responseCode = "400", description = "The requested Task does not belong to the User", content = @Content(schema = @Schema(hidden = true))),
                          @ApiResponse(responseCode = "400", description = "Invalid request-body", content = @Content(schema = @Schema(hidden = true)))})
    @PutMapping(path = "{taskId}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable("userId") Long userId,
            @PathVariable("taskId") Long taskId,
            @RequestBody @Valid TaskRequest taskRequest) {
        Task updatedTask = taskService.updateTask(userId, taskId, taskRequest);
        return ResponseEntity.ok(TaskResponse.of(updatedTask));
    }
}
