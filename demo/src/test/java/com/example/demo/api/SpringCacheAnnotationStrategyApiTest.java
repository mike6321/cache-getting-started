package com.example.demo.api;

import com.example.demo.common.cache.CacheStrategy;
import com.example.demo.model.ItemCreateRequest;
import com.example.demo.model.ItemUpdateRequest;
import com.example.demo.service.response.ItemPageResponse;
import com.example.demo.service.response.ItemResponse;
import org.junit.jupiter.api.Test;

public class SpringCacheAnnotationStrategyApiTest {

    static final CacheStrategy CACHE_STRATEGY = CacheStrategy.SPRING_CACHE_ANNOTATION;

    @Test
    void createAndReadAndReadAllAndUpdateAndDelete() {
        ItemResponse item1 = ItemApiTestUtils.create(CACHE_STRATEGY, new ItemCreateRequest("item1"));
        ItemResponse item2 = ItemApiTestUtils.create(CACHE_STRATEGY, new ItemCreateRequest("item2"));
        ItemResponse item3 = ItemApiTestUtils.create(CACHE_STRATEGY, new ItemCreateRequest("item3"));
        System.out.println("item1 = " + item1);
        System.out.println("item2 = " + item2);
        System.out.println("item3 = " + item3);

        ItemResponse item1Read1 = ItemApiTestUtils.read(CACHE_STRATEGY, item1.itemId());
        ItemResponse item1Read2 = ItemApiTestUtils.read(CACHE_STRATEGY, item1.itemId());
        ItemResponse item1Read3 = ItemApiTestUtils.read(CACHE_STRATEGY, item1.itemId());
        System.out.println("item1Read1 = " + item1Read1);
        System.out.println("item1Read2 = " + item1Read2);
        System.out.println("item1Read3 = " + item1Read3);

        ItemResponse item2Read1 = ItemApiTestUtils.read(CACHE_STRATEGY, item2.itemId());
        ItemResponse item2Read2 = ItemApiTestUtils.read(CACHE_STRATEGY, item2.itemId());
        ItemResponse item2Read3 = ItemApiTestUtils.read(CACHE_STRATEGY, item2.itemId());
        System.out.println("item2Read1 = " + item2Read1);
        System.out.println("item2Read2 = " + item2Read2);
        System.out.println("item2Read3 = " + item2Read3);

        ItemResponse item3Read1 = ItemApiTestUtils.read(CACHE_STRATEGY, item3.itemId());
        ItemResponse item3Read2 = ItemApiTestUtils.read(CACHE_STRATEGY, item3.itemId());
        ItemResponse item3Read3 = ItemApiTestUtils.read(CACHE_STRATEGY, item3.itemId());
        System.out.println("item3Read1 = " + item3Read1);
        System.out.println("item3Read2 = " + item3Read2);
        System.out.println("item3Read3 = " + item3Read3);

        ItemPageResponse readAll1 = ItemApiTestUtils.readAll(CACHE_STRATEGY, 1, 2);
        ItemPageResponse readAll2 = ItemApiTestUtils.readAll(CACHE_STRATEGY, 1, 2);
        System.out.println("readAll1 = " + readAll1);
        System.out.println("readAll2 = " + readAll2);

        ItemPageResponse readAllInfiniteScroll1 = ItemApiTestUtils.readAllInfiniteScroll(CACHE_STRATEGY, null, 2L);
        ItemPageResponse readAllInfiniteScroll2 = ItemApiTestUtils.readAllInfiniteScroll(CACHE_STRATEGY, null, 2L);
        System.out.println("readAllInfiniteScroll1 = " + readAllInfiniteScroll1);


        ItemApiTestUtils.update(CACHE_STRATEGY, item1.itemId(), new ItemUpdateRequest("item1-updated"));
        ItemResponse updatedRead = ItemApiTestUtils.read(CACHE_STRATEGY, item1.itemId());
        System.out.println("updatedRead = " + updatedRead);

        ItemApiTestUtils.delete(CACHE_STRATEGY, item1.itemId());
        try {
            ItemApiTestUtils.read(CACHE_STRATEGY, item1.itemId());
        } catch (Exception e) {
            System.out.println("item1 deleted successfully, read failed: " + e.getMessage());
        }
    }

}
