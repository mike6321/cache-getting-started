package com.example.demo.service.strategy.none;

import com.example.demo.common.cache.CacheStrategy;
import com.example.demo.common.cache.JWCacheHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemNoneCacheHandler implements JWCacheHandler {

    @Override
    public <T> T fetch(String key, Duration ttl, Supplier<T> datasoruceSupplier, Class<T> clazz) {
        log.info("Cache Strategy None - Key: {}", key);
        return datasoruceSupplier.get();
    }

    @Override
    public void put(String key, Duration ttl, Object value) {
        log.info("Cache Strategy None - Key: {}", key);
    }

    @Override
    public void evict(String key) {
        log.info("Cache Strategy None - Key: {}", key);
    }

    @Override
    public boolean supports(CacheStrategy cacheStrategy) {
        return CacheStrategy.NONE == cacheStrategy;
    }

}
