package com.example.demo.service.strategy.spiltbloomfilter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.LongStream;

@Component
@RequiredArgsConstructor
public class SplitBloomFilterRedisHandler {

    private final StringRedisTemplate redisTemplate;

    public void init(SplitBloomFilter splitBloomFilter) {
        for (long splitIndex = 0; splitIndex < splitBloomFilter.getSplitCount(); splitIndex++) {
            String generatedKey = generateKey(splitBloomFilter, splitIndex);
            long bitSize = splitBloomFilter.calculateSplitBitSize(splitIndex);
            for (long offset = 0; offset < bitSize; offset += 8L * 1024 * 1024 * 8 /* 8MB */) {
                redisTemplate.opsForValue().setBit(generatedKey, splitIndex, false);
            }
        }
    }

    public void add(SplitBloomFilter splitBloomFilter, String value) {
        this.redisTemplate.executePipelined(
                (RedisCallback<?>) action -> {
                    StringRedisConnection connection = (StringRedisConnection) action;
                    List<Long> hashedIndexes = splitBloomFilter.getBloomFilter().hash(value);
                    for (Long hashedIndex : hashedIndexes) {
                        long splitIndex = splitBloomFilter.findSplitIndex(hashedIndex);
                        connection.setBit(
                                generateKey(splitBloomFilter, splitIndex),
                                hashedIndex % SplitBloomFilter.BIT_SPLIT_UNIT,
                                true
                        );
                    }
                    return null;
                }
        );
    }

    public boolean mightContain(SplitBloomFilter splitBloomFilter, String value) {
        return this.redisTemplate.executePipelined(
                        (RedisCallback<?>) action -> {
                            StringRedisConnection connection = (StringRedisConnection) action;
                            List<Long> hashedIndexes = splitBloomFilter.getBloomFilter().hash(value);
                            for (Long hashedIndex : hashedIndexes) {
                                long splitIndex = splitBloomFilter.findSplitIndex(hashedIndex);
                                connection.getBit(
                                        generateKey(splitBloomFilter, splitIndex),
                                        hashedIndex % SplitBloomFilter.BIT_SPLIT_UNIT
                                );
                            }
                            return null;
                        }
                ).stream()
                .map(Boolean.class::cast)
                .allMatch(Boolean.TRUE::equals);
    }

    public void delete(SplitBloomFilter splitBloomFilter) {
        this.redisTemplate.executePipelined(
                (RedisCallback<?>) action -> {
                    StringRedisConnection connection = (StringRedisConnection) action;
                    generateKeys(splitBloomFilter)
                            .forEach(connection::del);
                    return null;
                }
        );
    }

    private List<String> generateKeys(SplitBloomFilter splitBloomFilter) {
        return LongStream.range(0, splitBloomFilter.getSplitCount())
                .mapToObj(splitIndex -> generateKey(splitBloomFilter, splitIndex))
                .toList();
    }

    private String generateKey(SplitBloomFilter splitBloomFilter, long splitIndex) {
        return "split-bloom-filter:%s:split:%s".formatted(splitBloomFilter.getId(), splitIndex);
    }

}
