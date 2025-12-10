package com.example.demo.api;

import com.example.demo.common.cache.CacheStrategy;
import com.example.demo.model.ItemCreateRequest;
import com.example.demo.model.ItemUpdateRequest;
import com.example.demo.service.response.ItemResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

class NoneStrategyApiTest {

    static final CacheStrategy CACHE_STRATEGY = CacheStrategy.NONE;

    @Test
    void createAndReadAndUpdateAndDeleteItem() {
        ItemResponse created = ItemApiTestUtils.create(CACHE_STRATEGY, new ItemCreateRequest("data"));
        System.out.println("created = " + created);

        ItemResponse read1 = ItemApiTestUtils.read(CacheStrategy.NONE, created.itemId());
        System.out.println("read = " + read1);

        ItemResponse update = ItemApiTestUtils.update(CacheStrategy.NONE, created.itemId(), new ItemUpdateRequest("updated data"));
        System.out.println("update = " + update);

        ItemResponse read2 = ItemApiTestUtils.read(CacheStrategy.NONE, created.itemId());
        System.out.println("read2 = " + read2);

        ResponseEntity<Void> delete = ItemApiTestUtils.delete(CacheStrategy.NONE, created.itemId());
        System.out.println("delete = " + delete);
    }

    @Test
    void readAll() {
        for (int i = 0; i < 3; i++) {
            ItemApiTestUtils.create(CACHE_STRATEGY, new ItemCreateRequest("data" + i));
        }

        var itemPage1 = ItemApiTestUtils.readAll(CACHE_STRATEGY, 1L, 2L);
        System.out.println("itemPage1 = " + itemPage1);

        var itemPage2 = ItemApiTestUtils.readAll(CACHE_STRATEGY, 2L, 2L);
        System.out.println("itemPage2 = " + itemPage2);
    }

    @Test
    void readAllInfiniteScroll() {
        for (int i = 0; i < 3; i++) {
            ItemApiTestUtils.create(CACHE_STRATEGY, new ItemCreateRequest("data" + i));
        }

        var itemPage1 = ItemApiTestUtils.readAllInfiniteScroll(CACHE_STRATEGY, null, 2L);
        System.out.println("itemPage1 = " + itemPage1);

        var itemPage2 = ItemApiTestUtils.readAllInfiniteScroll(CACHE_STRATEGY, itemPage1.items().getLast().itemId(), 2L);
        System.out.println("itemPage2 = " + itemPage2);
    }

}
