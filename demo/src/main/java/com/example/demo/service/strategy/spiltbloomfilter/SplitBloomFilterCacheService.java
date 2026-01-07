package com.example.demo.service.strategy.spiltbloomfilter;

import com.example.demo.common.cache.CacheStrategy;
import com.example.demo.model.ItemCreateRequest;
import com.example.demo.model.ItemUpdateRequest;
import com.example.demo.service.ItemCacheService;
import com.example.demo.service.ItemService;
import com.example.demo.service.response.ItemPageResponse;
import com.example.demo.service.response.ItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SplitBloomFilterCacheService implements ItemCacheService {

    private final ItemService itemService;
    private final SplitBloomFilterRedisHandler splitBloomFilterRedisHandler;

    private static final SplitBloomFilter splitBloomFilter = SplitBloomFilter.create(
            "item-bloom-filter",
            1000,
            0.01
    );

    @Override
    public ItemResponse read(Long itemId) {
        boolean result = this.splitBloomFilterRedisHandler.mightContain(splitBloomFilter, String.valueOf(itemId));
        if (!result) {
            return null;
        }

        return this.itemService.read(itemId);
    }

    @Override
    public ItemPageResponse readAll(Long page, Long pageSize) {
        return this.itemService.readAll(page, pageSize);
    }

    @Override
    public ItemPageResponse readAllInfiniteScroll(Long lastItemId, Long pageSize) {
        return this.itemService.readAllInfiniteScroll(lastItemId, pageSize);
    }

    @Override
    public ItemResponse create(ItemCreateRequest request) {
        ItemResponse itemResponse = this.itemService.create(request);
        splitBloomFilterRedisHandler.add(splitBloomFilter, String.valueOf(itemResponse.itemId()));
        return itemResponse;
    }

    @Override
    public ItemResponse update(Long itemId, ItemUpdateRequest request) {
        return this.itemService.update(itemId, request);
    }

    @Override
    public void delete(Long itemId) {
        this.itemService.delete(itemId);
    }

    @Override
    public boolean supports(CacheStrategy cacheStrategy) {
        return CacheStrategy.SPILT_BLOOM_FILTER == cacheStrategy;
    }

}
