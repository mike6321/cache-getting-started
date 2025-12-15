package com.example.demo.service.strategy.springcacheannotaion;

import com.example.demo.common.cache.CacheStrategy;
import com.example.demo.model.ItemCreateRequest;
import com.example.demo.model.ItemUpdateRequest;
import com.example.demo.service.ItemCacheService;
import com.example.demo.service.ItemService;
import com.example.demo.service.response.ItemPageResponse;
import com.example.demo.service.response.ItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemSpringCacheAnnotationCacheService implements ItemCacheService {

    private final ItemService itemService;

    @Override
    @Cacheable(cacheNames = "item", key = "#itemId")
    public ItemResponse read(Long itemId) {
        return this.itemService.read(itemId);
    }

    @Override
    @Cacheable(cacheNames = "itemList", key = "#page + ':' + #pageSize")
    public ItemPageResponse readAll(Long page, Long pageSize) {
        return this.itemService.readAll(page, pageSize);
    }

    @Override
    @Cacheable(cacheNames = "itemListInfiniteScroll", key = "#lastItemId + ':' + #pageSize")
    public ItemPageResponse readAllInfiniteScroll(Long lastItemId, Long pageSize) {
        return this.itemService.readAllInfiniteScroll(lastItemId, pageSize);
    }

    @Override
    public ItemResponse create(ItemCreateRequest request) {
        return this.itemService.create(request);
    }

    @Override
    @CachePut(cacheNames = "item", key = "#itemId")
    public ItemResponse update(Long itemId, ItemUpdateRequest request) {
        return this.itemService.update(itemId, request);
    }

    @Override
    @CacheEvict(cacheNames = "item", key = "#itemId")
    public void delete(Long itemId) {
        this.itemService.delete(itemId);
    }

    @Override
    public boolean supports(CacheStrategy cacheStrategy) {
        return CacheStrategy.SPRING_CACHE_ANNOTATION == cacheStrategy;
    }

}
