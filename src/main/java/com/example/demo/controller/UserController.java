package com.example.demo.controller;

import com.example.demo.dto.UserResponse;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.user.User;
import com.example.demo.dto.UserRequest;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "User Controller", description = "This controller is responsible for any operation in connection with the users.")
@RestController()
@RequestMapping(path = "api/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get all users in a list",
            responses = { @ApiResponse(responseCode = "200",
                                       description = "Successful operation",
                                       content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))})})
    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(
                userService.getUsers()
                        .stream()
                        .map(UserResponse::of)
                        .toList());
    }

    @Operation( summary = "Get a user with its Id",
                parameters = { @Parameter(in = ParameterIn.PATH, name = "userId", description = "Id of the User", example = "1") },
                responses = { @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserResponse.class))),
                              @ApiResponse(responseCode = "404", description = "Can not find User", content = @Content(schema = @Schema(hidden = true)))})
    @GetMapping(path = "{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(UserResponse.of(userService.getUser(userId)));
    }

    @Operation( summary = "Register a new User",
                responses = { @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserResponse.class))),
                              @ApiResponse(responseCode = "409", description = "The given email is already taken", content = @Content(schema = @Schema(hidden = true))),
                              @ApiResponse(responseCode = "400", description = "Invalid request-body", content = @Content(schema = @Schema(hidden = true)))})
    @PostMapping
    public ResponseEntity<UserResponse> registerNewUser(@RequestBody @Valid UserRequest userRequest) {
        User newUser = userService.addNewUser(userRequest);
        UserResponse response = UserResponse.of(newUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation( summary = "Delete a user with its Id",
            parameters = { @Parameter(in = ParameterIn.PATH, name = "userId", description = "Id of the User", example = "1") },
            responses = { @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(hidden = true))),
                          @ApiResponse(responseCode = "404", description = "Can not find User", content = @Content(schema = @Schema(hidden = true)))})
    @DeleteMapping(path = "{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation( summary = "Update a User",
            parameters = { @Parameter(in = ParameterIn.PATH, name = "userId", description = "Id of the User", example = "1") },
            responses = { @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserResponse.class))),
                          @ApiResponse(responseCode = "409", description = "The given email is already taken", content = @Content(schema = @Schema(hidden = true))),
                          @ApiResponse(responseCode = "404", description = "Can not find User", content = @Content(schema = @Schema(hidden = true))),
                          @ApiResponse(responseCode = "400", description = "Invalid request-body", content = @Content(schema = @Schema(hidden = true)))})
    @PutMapping(path = "{userId}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable("userId") Long userId,
            @RequestBody @Valid UserRequest userRequest) {
        User updatedUser = userService.updateUser(userId, userRequest);
        return ResponseEntity.ok(UserResponse.of(updatedUser));
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
