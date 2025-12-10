package com.example.demo.controller;

import com.example.demo.common.cache.CacheStrategy;
import com.example.demo.model.ItemCreateRequest;
import com.example.demo.model.ItemUpdateRequest;
import com.example.demo.service.ItemCacheService;
import com.example.demo.service.response.ItemPageResponse;
import com.example.demo.service.response.ItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final List<ItemCacheService> itemCacheServices;

    @GetMapping("/cache-strategy/{cacheStrategy}/items/{itemId}")
    public ItemResponse read(@PathVariable CacheStrategy cacheStrategy, @PathVariable Long itemId) {
        return resolveCacheHandler(cacheStrategy).read(itemId);
    }

    @GetMapping("/cache-strategy/{cacheStrategy}/items")
    public ItemPageResponse readAll(@PathVariable CacheStrategy cacheStrategy,
                                    @RequestParam Long page,
                                    @RequestParam Long pageSize) {
        return resolveCacheHandler(cacheStrategy).readAll(page, pageSize);
    }

    @GetMapping("/cache-strategy/{cacheStrategy}/items/infinite-scroll")
    public ItemPageResponse readAllInfiniteScroll(@PathVariable CacheStrategy cacheStrategy,
                                                  @RequestParam(required = false) Long lastItemId,
                                                  @RequestParam Long pageSize) {
        return resolveCacheHandler(cacheStrategy).readAllInfiniteScroll(lastItemId, pageSize);
    }

    @PostMapping("/cache-strategy/{cacheStrategy}/items")
    public ItemResponse create(@PathVariable CacheStrategy cacheStrategy,
                               @RequestBody ItemCreateRequest itemCreateRequest) {
        return resolveCacheHandler(cacheStrategy).create(itemCreateRequest);
    }

    @PutMapping("/cache-strategy/{cacheStrategy}/items/{itemId}")
    public ItemResponse update(@PathVariable CacheStrategy cacheStrategy,
                               @PathVariable Long itemId,
                               @RequestBody ItemUpdateRequest itemUpdateRequest) {
        return resolveCacheHandler(cacheStrategy).update(itemId, itemUpdateRequest);
    }

    @DeleteMapping("/cache-strategy/{cacheStrategy}/items/{itemId}")
    public void delete(@PathVariable CacheStrategy cacheStrategy,
                       @PathVariable Long itemId) {
        resolveCacheHandler(cacheStrategy).delete(itemId);
    }

    private ItemCacheService resolveCacheHandler(CacheStrategy cacheStrategy) {
        return this.itemCacheServices.stream()
                .filter(itemCacheService -> itemCacheService.supports(cacheStrategy))
                .findFirst()
                .orElseThrow();
    }

}
