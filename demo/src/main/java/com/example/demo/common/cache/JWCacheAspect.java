package com.example.demo.common.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Aspect
@RequiredArgsConstructor
@Component
public class JWCacheAspect {

    private final List<JWCacheHandler> cacheHandlers;
    private final JWCacheKeyGenerator cacheKeyGenerator;

    @Around("@annotation(jwCacheable)")
    public Object handleCacheable(ProceedingJoinPoint joinPoint, JWCacheable jwCacheable) {
        CacheStrategy cacheStrategy = jwCacheable.cacheStrategy();
        JWCacheHandler cacheHandler = findCacheHandler(cacheStrategy);

        String cacheKey = cacheKeyGenerator.getKey(joinPoint, cacheStrategy, jwCacheable.cacheName(), jwCacheable.cacheKey());
        Duration ttl = Duration.ofSeconds(jwCacheable.ttlSeconds());
        Supplier<Object> dataSourceSupplier = createDataSourceSupplier(joinPoint);
        Class returnType = findReturnType(joinPoint);

        try {
            log.info("Attempting to fetch from cache with key: {}", cacheKey);
            return cacheHandler.fetch(cacheKey, ttl, dataSourceSupplier, returnType);
        } catch (Exception e) {
            log.error("Cache operation failed for key: {}", cacheKey, e);
            return dataSourceSupplier.get();
        }
    }

    private JWCacheHandler findCacheHandler(CacheStrategy cacheStrategy) {
        return cacheHandlers.stream()
                .filter(handler -> handler.supports(cacheStrategy))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No suitable cache handler found for strategy: " + cacheStrategy));
    }

    private Supplier<Object> createDataSourceSupplier(ProceedingJoinPoint joinPoint) {
        return () -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        };
    }

    private Class findReturnType(ProceedingJoinPoint joinPoint) {
        return ((MethodSignature) joinPoint.getSignature()).getReturnType();
    }

    @AfterReturning(pointcut = "@annotation(jwCachePut)", returning = "result")
    public void handleCachePut(JoinPoint joinPoint, JWCachePut jwCachePut, Object result) {
        CacheStrategy cacheStrategy = jwCachePut.cacheStrategy();
        JWCacheHandler cacheHandler = findCacheHandler(cacheStrategy);

        String cacheKey = cacheKeyGenerator.getKey(joinPoint, cacheStrategy, jwCachePut.cacheName(), jwCachePut.cacheKey());
        Duration ttl = Duration.ofSeconds(jwCachePut.ttlSeconds());

        log.info("Putting value into cache with key: {}", cacheKey);
        cacheHandler.put(cacheKey, ttl, result);
    }

    @AfterReturning(pointcut = "@annotation(jwCacheEvict)")
    public void handleCacheEvict(JoinPoint joinPoint, JWCacheEvict jwCacheEvict) {
        CacheStrategy cacheStrategy = jwCacheEvict.cacheStrategy();
        JWCacheHandler cacheHandler = findCacheHandler(cacheStrategy);

        String cacheKey = cacheKeyGenerator.getKey(joinPoint, cacheStrategy, jwCacheEvict.cacheName(), jwCacheEvict.cacheKey());
        log.info("Evicting cache with key: {}", cacheKey);
        cacheHandler.evict(cacheKey);
    }

}
