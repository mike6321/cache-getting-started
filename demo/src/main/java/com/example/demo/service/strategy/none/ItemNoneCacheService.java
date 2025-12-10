package com.example.demo.service.strategy.none;

import com.example.demo.common.cache.CacheStrategy;
import com.example.demo.common.cache.JWCacheEvict;
import com.example.demo.common.cache.JWCachePut;
import com.example.demo.common.cache.JWCacheable;
import com.example.demo.model.ItemCreateRequest;
import com.example.demo.model.ItemUpdateRequest;
import com.example.demo.service.ItemCacheService;
import com.example.demo.service.ItemService;
import com.example.demo.service.response.ItemPageResponse;
import com.example.demo.service.response.ItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 아이템 캐시전략 수립 -> ItemService 로직 수행
 * */
@Service
@RequiredArgsConstructor
public class ItemNoneCacheService implements ItemCacheService {

    private final ItemService itemService;

    @Override
    @JWCacheable(
            cacheStrategy = CacheStrategy.NONE,
            cacheName = "item",
            cacheKey = "#itemId",
            ttlSeconds = 5
    )
    public ItemResponse read(Long itemId) {
        return this.itemService.read(itemId);
    }

    @Override
    @JWCacheable(
            cacheStrategy = CacheStrategy.NONE,
            cacheName = "itemList",
            cacheKey = "#page + ':' + #pageSize",
            ttlSeconds = 5
    )
    public ItemPageResponse readAll(Long page, Long pageSize) {
        return this.itemService.readAll(page, pageSize);
    }

    @Override
    @JWCacheable(
            cacheStrategy = CacheStrategy.NONE,
            cacheName = "itemListInfiniteScroll",
            cacheKey = "#lastItemId + ':' + #pageSize",
            ttlSeconds = 5
    )
    public ItemPageResponse readAllInfiniteScroll(Long lastItemId, Long pageSize) {
        return this.itemService.readAllInfiniteScroll(lastItemId, pageSize);
    }

    @Override
    public ItemResponse create(ItemCreateRequest request) {
        return this.itemService.create(request);
    }

    @Override
    @JWCachePut(
            cacheStrategy = CacheStrategy.NONE,
            cacheName = "item",
            cacheKey = "#itemId",
            ttlSeconds = 5
    )
    public ItemResponse update(Long itemId, ItemUpdateRequest request) {
        return this.itemService.update(itemId, request);
    }

    @Override
    @JWCacheEvict(
            cacheStrategy = CacheStrategy.NONE,
            cacheName = "item",
            cacheKey = "#itemId"
    )
    public void delete(Long itemId) {
        this.itemService.delete(itemId);
    }

    @Override
    public boolean supports(CacheStrategy cacheStrategy) {
        return CacheStrategy.NONE == cacheStrategy;
    }

}
