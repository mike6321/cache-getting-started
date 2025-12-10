package com.example.demo.common.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface JWCacheEvict {

    CacheStrategy cacheStrategy();
    String cacheName();
    String cacheKey();

}
