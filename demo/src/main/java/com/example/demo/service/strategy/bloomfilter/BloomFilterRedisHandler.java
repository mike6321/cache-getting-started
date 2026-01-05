package com.example.demo.service.strategy.bloomfilter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BloomFilterRedisHandler {

    private final StringRedisTemplate redisTemplate;

    public void add(BloomFilter bloomFilter, String value) {
        this.redisTemplate.executePipelined(
                (RedisCallback<?>) action -> {
                    StringRedisConnection connection = (StringRedisConnection) action;
                    String generatedKey = generateKey(bloomFilter);
                    List<Long> hashedIndexes = bloomFilter.hash(value);
                    for (Long hashedIndex : hashedIndexes) {
                        connection.setBit(generatedKey, hashedIndex, true);
                    }
                    return null;
                }
        );
    }

    public boolean mightContain(BloomFilter bloomFilter, String value) {
        return this.redisTemplate.executePipelined(
                        (RedisCallback<?>) action -> {
                            StringRedisConnection connection = (StringRedisConnection) action;
                            String generatedKey = generateKey(bloomFilter);
                            List<Long> hashedIndexes = bloomFilter.hash(value);
                            for (Long hashedIndex : hashedIndexes) {
                                connection.getBit(generatedKey, hashedIndex);
                            }
                            return null;
                        }
                ).stream()
                .map(Boolean.class::cast)
                .allMatch(Boolean.TRUE::equals);
    }

    public void delete(BloomFilter bloomFilter) {
        this.redisTemplate.delete(generateKey(bloomFilter));
    }

    private String generateKey(BloomFilter bloomFilter) {
        return generateKey(bloomFilter.getId());
    }

    private String generateKey(String id) {
        return "bloom-filter:%s".formatted(id);
    }

}
