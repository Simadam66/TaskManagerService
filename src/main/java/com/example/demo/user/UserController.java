package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(
                userService.getUsers()
                .stream()
                .map(UserResponse::of)
                .toList());
    }

    @GetMapping(path = "{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(UserResponse.of(userService.getUser(userId)));
    }

    @PostMapping
    public ResponseEntity<String> registerNewUser(@RequestBody UserRequest userRequest) {
        userService.addNewUser(User.update(userRequest));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(path = "{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "{userId}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable("userId") Long userId,
            @RequestBody UserRequest userRequest) {
        User updatedUser = userService.updateUser(userId, User.update(userRequest));
        return new ResponseEntity<>(UserResponse.of(updatedUser),HttpStatus.OK);
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
