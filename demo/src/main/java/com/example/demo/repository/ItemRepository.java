package com.example.demo.repository;

import com.example.demo.model.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;

@Slf4j
@Repository
public class ItemRepository {

    private final ConcurrentSkipListMap<Long, Item> database = new ConcurrentSkipListMap<>(Comparator.reverseOrder());

    public Optional<Item> read(Long itemId) {
        log.info("read itemId: {}", itemId);
        return Optional.ofNullable(database.get(itemId));
    }

    public List<Item> readAll(Long page, Long pageSize) {
        log.info("read all items page: {}, pageSize: {}", page, pageSize);
        return database.values().stream()
                .skip((page - 1) * pageSize)
                .limit(pageSize)
                .toList();
    }

    public List<Item> readAllInfiniteScroll(Long lastItemId, Long pageSize) {
        log.info("read all items lastItemId: {}, pageSize: {}", lastItemId, pageSize);
        if (lastItemId == null) {
            return database.values().stream()
                    .limit(pageSize)
                    .toList();
        }
        return database.tailMap(lastItemId, false).values().stream()
                .limit(pageSize)
                .toList();
    }

    public Item create(Item item) {
        log.info("create item: {}", item);
        database.put(item.getItemId(), item);
        return item;
    }

    public Item update(Item item) {
        log.info("update item: {}", item);
        database.put(item.getItemId(), item);
        return item;
    }

    public void delete(Item item) {
        log.info("remove item: {}", item);
        database.remove(item.getItemId());
    }

    public long count() {
        return database.size();
    }

}
