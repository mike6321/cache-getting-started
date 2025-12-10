package com.example.demo.common.cache;

import java.time.Duration;
import java.util.function.Supplier;

public interface JWCacheHandler {

    <T> T fetch(String key, Duration ttl, Supplier<T> datasoruceSupplier, Class<T> clazz);
    void  put(String key, Duration ttl, Object value);
    void evict(String key);
    boolean supports(CacheStrategy cacheStrategy);

}
