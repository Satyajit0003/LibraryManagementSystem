package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.dto.UserDto;
import com.librarymanagementsystem.entity.User;
import com.librarymanagementsystem.enums.Role;
import com.librarymanagementsystem.services.UserDetailsServiceImpl;
import com.librarymanagementsystem.services.UserService;
import com.librarymanagementsystem.utilis.JwtUtil;
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

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;

    public PublicController(UserService userService, AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

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
