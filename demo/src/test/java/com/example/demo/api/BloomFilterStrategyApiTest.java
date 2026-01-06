package com.example.demo.api;

import com.example.demo.common.cache.CacheStrategy;
import com.example.demo.model.ItemCreateRequest;
import org.junit.jupiter.api.Test;

public class BloomFilterStrategyApiTest {

    static final CacheStrategy CACHE_STRATEGY = CacheStrategy.BLOOM_FILTER;


    @Test
    void test() {
        for (int i = 0; i < 1000; i++) {
            ItemApiTestUtils.create(CACHE_STRATEGY, new ItemCreateRequest("data" + i));
        }

        for (long itemId = 10000; itemId < 20000; itemId++) {
            ItemApiTestUtils.read(CACHE_STRATEGY, itemId);
        }
    }

}
