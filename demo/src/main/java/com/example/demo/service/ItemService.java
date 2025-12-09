package com.example.demo.service;

import com.example.demo.model.Item;
import com.example.demo.model.ItemCreateRequest;
import com.example.demo.model.ItemUpdateRequest;
import com.example.demo.repository.ItemRepository;
import com.example.demo.service.response.ItemPageResponse;
import com.example.demo.service.response.ItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemResponse read(Long itemId) {
        return itemRepository.read(itemId)
                .map(ItemResponse::from)
                .orElse(null);
    }

    public ItemPageResponse readAll(Long page, Long pageSize) {
        var items = itemRepository.readAll(page, pageSize);
        var count = itemRepository.count();
        return ItemPageResponse.from(items, count);
    }

    public ItemPageResponse readAllInfiniteScroll(Long lastItemId, Long pageSize) {
        var items = itemRepository.readAllInfiniteScroll(lastItemId, pageSize);
        var count = itemRepository.count();
        return ItemPageResponse.from(items, count);
    }

    public ItemResponse create(ItemCreateRequest request) {
        return ItemResponse.from(
                itemRepository.create(Item.create(request))
        );
    }

    public ItemResponse update(Long itemId, ItemUpdateRequest request) {
        Item item = itemRepository.read(itemId).orElseThrow();
        Item updatedItem = item.update(request);

        return ItemResponse.from(
                itemRepository.update(updatedItem)
        );
    }

    public void delete(Long itemId) {
        itemRepository.read(itemId).ifPresent(itemRepository::delete);
    }

    public long count() {
        return itemRepository.count();
    }

}
