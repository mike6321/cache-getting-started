package com.example.demo.api;

import com.example.demo.common.cache.CacheStrategy;
import com.example.demo.service.response.ItemResponse;
import org.junit.jupiter.api.Test;

public class NullObjectPatternStrategyApiTest {

    static final CacheStrategy CACHE_STRATEGY = CacheStrategy.NULL_OBJECT_PATTERN;

    @Test
    void read() {
        for (int i = 0; i  < 3; i++) {
            ItemResponse item = ItemApiTestUtils.read(CACHE_STRATEGY, 999999L);
            System.out.println("item = " + item);
        }
    }

}
