package com.librarymanagementsystem.services;

import com.librarymanagementsystem.dto.UserDto;
import com.librarymanagementsystem.entity.User;

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

