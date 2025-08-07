package com.libraryManagementSystem.services;

import com.libraryManagementSystem.entity.User;
import com.libraryManagementSystem.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @CachePut(value = "user", key = "#user.userId")
    @CacheEvict(value = "users", allEntries = true)
    public User saveUser(User user) {
        Optional<User> existing = userRepository.findByUserName(user.getUserName());
        if (existing.isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        user.setUserId(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
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
        userRepository.deleteById(id);
    }

    @Cacheable(value = "user", key = "#id")
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Cacheable(value = "user", key = "'name_' + #userName")
    public Optional<User> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
}
