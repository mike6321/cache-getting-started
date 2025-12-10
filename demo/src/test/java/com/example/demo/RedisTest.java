package com.example.demo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class RedisTest extends RedisTestContainerSupport {

    @Test
    void test01() {
        this.redisTemplate.opsForValue().set("test-key", "test-value");
        String value = this.redisTemplate.opsForValue().get("test-key");
        assertThat(value).isEqualTo("test-value");
    }

    @Test
    void test02() {
//        this.redisTemplate.opsForValue().set("test-key", "test-value");
        String value = this.redisTemplate.opsForValue().get("test-key");
        assertThat(value).isEqualTo("test-value");
    }

    @Test
    void test03() {
//        this.redisTemplate.opsForValue().set("test-key", "test-value");
        String value = this.redisTemplate.opsForValue().get("test-key");
        assertThat(value).isEqualTo("test-value");
    }



}
