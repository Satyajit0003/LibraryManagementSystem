package com.libraryManagementSystem.controller;

import com.libraryManagementSystem.dto.UserDto;
import com.libraryManagementSystem.entity.User;
import com.libraryManagementSystem.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/admin")
@Tag(name = "Admin APIs")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("health-check")
    @Operation(summary = "APIs for health check")
    public String checkHealth(){
        return "Ok";
    }

    @Operation(summary = "Create a new user with ADMIN role")
    @PostMapping("/signup-admin")
    public ResponseEntity<?> signupAdmin(@RequestBody UserDto user) {
        log.info("Creating user with username: {}", user.getUserName());
        try {
            User newUser = new User();
            newUser.setUserName(user.getUserName());
            newUser.setEmail(user.getEmail());
            newUser.setPassword(user.getPassword());
            newUser.setRoles(List.of("ADMIN"));
            userService.saveUser(newUser);
            log.info("User created successfully: {}", newUser.getUserName());
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating user: {}", e.getMessage(), e);
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Create a new user with LIBRARIAN role")
    @PostMapping("/signup-librarian")
    public ResponseEntity<?> signupLibrarian(@RequestBody UserDto user) {
        log.info("Creating user with username: {}", user.getUserName());
        try {
            User newUser = new User();
            newUser.setUserName(user.getUserName());
            newUser.setEmail(user.getEmail());
            newUser.setPassword(user.getPassword());
            newUser.setRoles(List.of("LIBRARIAN"));
            userService.saveUser(newUser);
            log.info("User created successfully: {}", newUser.getUserName());
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating user: {}", e.getMessage(), e);
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Get all users")
    @GetMapping("/all-users")
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Fetching all users");
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/user-by-id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id){
        log.info("Fetching user by ID: {}", id);
        User user = userService.findById(id).orElse(null);
        if (user == null) {
            log.warn("User not found with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Delete user by ID")
    @DeleteMapping("delete-user-by-id/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable String id) {
        log.info("Attempting to delete user with ID: {}", id);
        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isEmpty()) {
            log.warn("User not found with ID: {}", id);
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        User user = userOptional.get();
        if (!user.getBookIssuedList().isEmpty()) {
            log.warn("Cannot delete user with issued books. ID: {}", id);
            return new ResponseEntity<>("User has issued books and cannot be deleted", HttpStatus.BAD_REQUEST);
        }
        userService.deleteById(id);
        log.info("User deleted successfully. ID: {}", id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @Operation(summary = "Get user by username")
    @GetMapping("user-by-username/{userName}")
    public ResponseEntity<User> getUserByUserName(@PathVariable String userName) {
        log.info("Fetching user by username: {}", userName);
        User user = userService.findByUserName(userName).orElse(null);
        if (user == null) {
            log.warn("User not found with username: {}", userName);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(user);
    }

}
