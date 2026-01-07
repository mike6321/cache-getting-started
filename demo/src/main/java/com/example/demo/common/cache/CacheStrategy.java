package com.example.demo.common.cache;

public enum CacheStrategy {
    NONE,
    SPRING_CACHE_ANNOTATION,
    NULL_OBJECT_PATTERN,
    BLOOM_FILTER,
    SPILT_BLOOM_FILTER
}
