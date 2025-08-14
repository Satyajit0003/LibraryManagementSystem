package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.dto.UserDto;
import com.librarymanagementsystem.entity.User;
import com.librarymanagementsystem.enums.Role;
import com.librarymanagementsystem.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
@Tag(name = "Admin APIs")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("health-check")
    @Operation(summary = "APIs for health check")
    public String checkHealth() {
        return "Ok";
    }

    @Operation(summary = "Create a new user with ADMIN role")
    @PostMapping("/signup-admin")
    public ResponseEntity<?> signupAdmin(@RequestBody UserDto userDto) {
        User user = userService.saveUser(userDto, Role.ADMIN.toString());
        log.info("New ADMIN account created for username: [{}]", user.getUserName());
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @Operation(summary = "Create a new user with LIBRARIAN role")
    @PostMapping("/signup-librarian")
    public ResponseEntity<?> signupLibrarian(@RequestBody UserDto userDto) {
        User user = userService.saveUser(userDto, Role.LIBRARIAN.toString());
        log.info("New LIBRARIAN account created for username: [{}]", user.getUserName());
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all users")
    @GetMapping("/all-users")
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Fetching all registered users");
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/user-by-id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        log.info("Fetching user details for ID: [{}]", id);
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Delete user by ID")
    @DeleteMapping("delete-user-by-id/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable String id) {
        userService.deleteById(id);
        log.info("Deleted user with ID: [{}]", id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @Operation(summary = "Get user by username")
    @GetMapping("user-by-username/{userName}")
    public ResponseEntity<User> getUserByUserName(@PathVariable String userName) {
        log.info("Fetching user details for username: [{}]", userName);
        User user = userService.findByUserName(userName);
        return ResponseEntity.ok(user);
    }

}
