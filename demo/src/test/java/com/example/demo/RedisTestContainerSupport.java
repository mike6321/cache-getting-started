package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

public abstract class RedisTestContainerSupport {

    @Autowired
    protected StringRedisTemplate redisTemplate;

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", () -> "localhost");
        registry.add("spring.data.redis.port", () -> 6379);
    }

    @BeforeEach
    void beforeEach() {
        Assertions.assertNotNull(this.redisTemplate.getConnectionFactory());
        this.redisTemplate.getConnectionFactory().getConnection().serverCommands().flushDb();
    }

}
