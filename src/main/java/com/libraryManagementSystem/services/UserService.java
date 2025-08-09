package com.libraryManagementSystem.services;

import com.libraryManagementSystem.dto.UserDto;
import com.libraryManagementSystem.entity.User;

import java.util.List;

public interface UserService {

    User saveUser(UserDto userDto, String role);

    List<User> getAllUsers();

    void deleteById(String id);

    User findById(String id);

    User findByUserName(String username);

    User updateUser(UserDto userDto);

    String deleteUser();
}

