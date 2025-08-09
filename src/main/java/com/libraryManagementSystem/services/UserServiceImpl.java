package com.libraryManagementSystem.services;

import com.libraryManagementSystem.dto.UserDto;
import com.libraryManagementSystem.entity.User;
import com.libraryManagementSystem.exception.UserAlreadyExistsException;
import com.libraryManagementSystem.exception.UserNotFoundException;
import com.libraryManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @CachePut(value = "user", key = "#result.userId")
    @CacheEvict(value = "users", allEntries = true)
    public User saveUser(UserDto userDto, String role) {
        Optional<User> existing = userRepository.findByUserName(userDto.getUserName());
        if (existing.isPresent()) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        User newUser = new User();
        newUser.setUserId(UUID.randomUUID().toString());
        newUser.setUserName(userDto.getUserName());
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        newUser.setEmail(userDto.getEmail());
        newUser.setRoles(List.of(role));
        newUser.setCurrentDate(LocalDateTime.now().toString());
        return userRepository.save(newUser);
    }

    @Cacheable(value = "users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Caching(evict = {
            @CacheEvict(value = "user", key = "#id"),
            @CacheEvict(value = "users", allEntries = true)
    })
    public void deleteById(String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        User user = userOptional.get();
        if (!user.getBookIssuedList().isEmpty()) {
            throw new RuntimeException("User has issued books and cannot be deleted");
        }
        userRepository.deleteById(id);
    }

    @Cacheable(value = "user", key = "#id")
    public User findById(String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        User user = userOptional.get();
        return user;
    }

    @Cacheable(value = "user", key = "'name_' + #userName")
    public User findByUserName(String userName) {
        Optional<User> userOptional = userRepository.findByUserName(userName);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        User user = userOptional.get();
        return user;
    }

    @Cacheable(value = "user", key = "#result.userId")
    @CacheEvict(value = "users", allEntries = true)
    public User updateUser(UserDto userDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Optional<User> userOptional = userRepository.findByUserName(userName);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        User oldUser = userOptional.get();
        oldUser.setUserName(userDto.getUserName());
        oldUser.setPassword(userDto.getPassword());
        oldUser.setEmail(userDto.getEmail());
        userRepository.save(oldUser);
        return oldUser;
    }

    @Caching(evict = {
            @CacheEvict(value = "user", key = "#userName"),
            @CacheEvict(value = "users", allEntries = true)
    })
    public String deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Optional<User> userOptional = userRepository.findByUserName(userName);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        User user = userOptional.get();
        if (!user.getBookIssuedList().isEmpty()) {
            throw new RuntimeException("User has issued books and cannot be deleted");
        }
        userRepository.delete(user);
        return userName;
    }
}
