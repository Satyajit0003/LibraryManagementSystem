package com.librarymanagementsystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;


@Component
public class RedisConnectionTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostConstruct
    public void testConnection() {
        try {
            String pong = redisTemplate.getConnectionFactory().getConnection().ping();
            System.out.println("Redis PING Response: " + pong);
        } catch (Exception e) {
            System.err.println("Redis Connection Failed: " + e.getMessage());
        }
    }
}
