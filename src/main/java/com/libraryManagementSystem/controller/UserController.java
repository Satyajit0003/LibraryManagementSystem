package com.libraryManagementSystem.controller;

import com.libraryManagementSystem.dto.UserDto;
import com.libraryManagementSystem.entity.User;
import com.libraryManagementSystem.services.BookIssuedService;
import com.libraryManagementSystem.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "User APIs")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookIssuedService bookIssuedService;

    @PutMapping("update-user")
    @Operation(summary = "Update User Details")
    public ResponseEntity<?> updateUser(@RequestBody UserDto user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        log.info("Updating user with username: {}", userName);
        User oldUser = userService.findByUserName(userName).orElse(null);
        if (oldUser == null) {
            log.warn("User not found: {}", userName);
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        oldUser.setUserName(user.getUserName());
        oldUser.setPassword(user.getPassword());
        oldUser.setEmail(user.getEmail());
        userService.saveUser(oldUser);
        log.info("User updated successfully: {}", user.getUserName());
        return ResponseEntity.ok(oldUser);
    }

    @Operation(summary = "Delete User Account")
    @DeleteMapping("delete-user")
    public ResponseEntity<String> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        log.info("Attempting to delete user with userName: {}", userName);
        Optional<User> userOptional = userService.findByUserName(userName);
        User user = userOptional.get();
        String id = user.getUserId();
        if (!user.getBookIssuedList().isEmpty()) {
            log.warn("Cannot delete user with issued books. ID: {}", id);
            return new ResponseEntity<>("User has issued books and cannot be deleted", HttpStatus.BAD_REQUEST);
        }
        userService.deleteById(id);
        log.info("User deleted successfully. ID: {}", id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @Operation(summary = "Issue Book to User by Book ID")
    @GetMapping("/issued/bookId/{id}")
    public ResponseEntity<String> issued(@PathVariable String id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Optional<User> userOptional = userService.findByUserName(userName);
        if (userOptional.isEmpty()) {
            log.warn("Authenticated user not found: {}", userName);
            return new ResponseEntity<>("User not found or may be deleted", HttpStatus.NOT_FOUND);
        }
        log.info("Issuing book with ID {} to user {}", id, userName);
        try {
            bookIssuedService.issuedBook(userName, id);
            log.info("Book issued successfully to user {}", userName);
            return ResponseEntity.ok("Issued book successfully");
        } catch (Exception e) {
            log.error("Error issuing book to user {}: {}", userName, e.getMessage(), e);
            return new ResponseEntity<>("Error issuing book: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Return Issued Book by ID")
    @GetMapping("/returned/issuedId/{id}")
    public ResponseEntity<String> returned(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Optional<User> userOptional = userService.findByUserName(userName);
        if (userOptional.isEmpty()) {
            log.warn("Authenticated user not found: {}", userName);
            return new ResponseEntity<>("User not found or may be deleted", HttpStatus.NOT_FOUND);
        }
        User user = userOptional.get();
        if (user.getBookIssuedList().stream().noneMatch(b -> b.getIssuedId().equals(id))) {
            log.warn("Issued book ID {} not found in user {}'s list", id, userName);
            return new ResponseEntity<>("Issued book not found for this user", HttpStatus.NOT_FOUND);
        }
        log.info("Returning issued book ID {} for user {}", id, userName);
        try {
            bookIssuedService.returnedBook(id, userName);
            log.info("Book returned successfully by user {}", userName);
            return ResponseEntity.ok("Returned book successfully");
        } catch (Exception e) {
            log.error("Error returning book for user {}: {}", userName, e.getMessage(), e);
            return new ResponseEntity<>("Error returning book: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
