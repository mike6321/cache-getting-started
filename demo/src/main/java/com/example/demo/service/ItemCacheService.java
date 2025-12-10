package com.example.demo.service;

import com.example.demo.common.cache.CacheStrategy;
import com.example.demo.model.ItemCreateRequest;
import com.example.demo.model.ItemUpdateRequest;
import com.example.demo.service.response.ItemPageResponse;
import com.example.demo.service.response.ItemResponse;

public interface ItemCacheService {

    ItemResponse read(Long itemId);
    ItemPageResponse readAll(Long page, Long pageSize);
    ItemPageResponse readAllInfiniteScroll(Long lastItemId, Long pageSize);
    ItemResponse create(ItemCreateRequest request);
    ItemResponse update(Long itemId, ItemUpdateRequest request);
    void delete(Long itemId);
    boolean supports(CacheStrategy cacheStrategy);

}
