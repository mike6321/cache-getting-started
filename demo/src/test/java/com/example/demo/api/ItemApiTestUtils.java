package com.example.demo.api;

import com.example.demo.common.cache.CacheStrategy;
import com.example.demo.model.ItemCreateRequest;
import com.example.demo.model.ItemUpdateRequest;
import com.example.demo.service.response.ItemPageResponse;
import com.example.demo.service.response.ItemResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

public class ItemApiTestUtils {

    static RestClient restClient = RestClient.create("http://localhost:8080");

    static ItemResponse read(CacheStrategy cacheStrategy, Long itemId) {
        return restClient.get()
                .uri(String.format("/cache-strategy/%s/items/%s", cacheStrategy.name(), itemId))
                .retrieve()
                .body(ItemResponse.class);
    }

    static ItemPageResponse readAll(CacheStrategy cacheStrategy, long page, long pageSize) {
        return restClient.get()
                .uri(String.format("/cache-strategy/%s/items?page=%s&pageSize=%s", cacheStrategy.name(), page, pageSize))
                .retrieve()
                .body(ItemPageResponse.class);
    }

    static ItemPageResponse readAllInfiniteScroll(CacheStrategy cacheStrategy, Long lastItemId, long pageSize) {
        return restClient.get()
                .uri(
                        lastItemId == null ? String.format("/cache-strategy/%s/items/infinite-scroll?pageSize=%s", cacheStrategy.name(), pageSize) :
                        String.format("/cache-strategy/%s/items/infinite-scroll?lastItemId=%s&pageSize=%s", cacheStrategy.name(), lastItemId, pageSize))
                .retrieve()
                .body(ItemPageResponse.class);
    }

    static ItemResponse create(CacheStrategy cacheStrategy, ItemCreateRequest itemCreateRequest) {
        return restClient.post()
                .uri(String.format("/cache-strategy/%s/items", cacheStrategy.name()))
                .body(itemCreateRequest)
                .retrieve()
                .body(ItemResponse.class);
    }

    static ItemResponse update(CacheStrategy cacheStrategy, Long itemId, ItemUpdateRequest itemUpdateRequest) {
        return restClient.put()
                .uri(String.format("/cache-strategy/%s/items/%s", cacheStrategy.name(), itemId))
                .body(itemUpdateRequest)
                .retrieve()
                .body(ItemResponse.class);
    }

    static ResponseEntity<Void> delete(CacheStrategy cacheStrategy, Long itemId) {
        return restClient.delete()
                .uri(String.format("/cache-strategy/%s/items/%s", cacheStrategy.name(), itemId))
                .retrieve()
                .toBodilessEntity();
    }

}
