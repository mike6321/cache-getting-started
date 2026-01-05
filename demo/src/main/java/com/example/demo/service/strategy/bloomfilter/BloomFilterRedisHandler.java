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

    /**
     * 최초에 큰 메모리를 할당하려면 블로킹이 발생할 수 있으며, 싱글 스레드 특성 상 다른 연산에 지연이 발생할 수 있다.
     * BloomFilter 활성화 전에 내부 관리 도구에서 점차 메모리를 늘려가는 전략으로 미리 필요한 만큼 할당할 수 있다.
     * */
    public void init(BloomFilter bloomFilter) {
        String generatedKey = generateKey(bloomFilter);
        for (long offset = 0; offset < bloomFilter.getBitSize(); offset += 8L * 1024 * 1024 * 8 /* 8MB */) {
            this.redisTemplate.opsForValue().setBit(generatedKey,offset,false);
        }
    }

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
