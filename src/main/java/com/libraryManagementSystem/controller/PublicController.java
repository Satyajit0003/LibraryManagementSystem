package com.libraryManagementSystem.controller;

import com.libraryManagementSystem.dto.UserDto;
import com.libraryManagementSystem.entity.User;
import com.libraryManagementSystem.enums.Role;
import com.libraryManagementSystem.services.UserDetailsServiceImpl;
import com.libraryManagementSystem.services.UserService;
import com.libraryManagementSystem.utilis.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@Slf4j
@Tag(name = "Public APIs")
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signup")
    @Operation(summary = "Create a new user")
    public ResponseEntity<?> signup(@RequestBody UserDto userDto) {
        User newUser = userService.saveUser(userDto, Role.STUDENT.toString());
        log.info("New {} account created successfully for username: [{}]", Role.STUDENT, newUser.getUserName());
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @Operation(summary = "Login user and return JWT token")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto userDTO) {
        try {
            log.info("Login attempt for username: [{}]", userDTO.getUserName());

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.getUserName(), userDTO.getPassword()));

            UserDetails userDetails = userDetailsService.loadUserByUsername(userDTO.getUserName());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());

            log.info("Login successful for username: [{}], JWT generated", userDTO.getUserName());
            return new ResponseEntity<>(jwt, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Authentication failed for username: [{}]. Reason: {}", userDTO.getUserName(), e.getMessage());
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }
}
