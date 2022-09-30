package com.example.demo.Controllers;

import com.example.demo.DTOs.TaskRequest;
import com.example.demo.Models.User;
import com.example.demo.DTOs.UserRequest;
import com.example.demo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<TaskRequest.UserResponse>> getUsers() {
        return ResponseEntity.ok(
                userService.getUsers()
                        .stream()
                        .map(TaskRequest.UserResponse::of)
                        .toList());
    }

    @GetMapping(path = "{userId}")
    public ResponseEntity<TaskRequest.UserResponse> getUser(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(TaskRequest.UserResponse.of(userService.getUser(userId)));
    }

    @PostMapping
    public ResponseEntity<TaskRequest.UserResponse> registerNewUser(@RequestBody @Valid UserRequest userRequest) {
        User newUser = userService.addNewUser(userRequest);
        TaskRequest.UserResponse response = TaskRequest.UserResponse.of(newUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "{userId}")
    public ResponseEntity<TaskRequest.UserResponse> updateUser(
            @PathVariable("userId") Long userId,
            @RequestBody @Valid UserRequest userRequest) {
        User updatedUser = userService.updateUser(userId, userRequest);
        return ResponseEntity.ok(TaskRequest.UserResponse.of(updatedUser));
    }

    //Obsolete implementation
/*    @PutMapping(path = "{userId}")
    public void updateUser(
            @PathVariable("userId") Long userId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email) {
        userService.updateUser(userId, name, email);
    }*/
}
