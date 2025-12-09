package com.example.demo.repository;


import com.example.demo.model.Item;
import com.example.demo.model.ItemCreateRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

class ItemRepositoryTest {

    ItemRepository itemRepository = new ItemRepository();

    @Test
    void readAll() {
        List<Item> items = IntStream.range(0, 3)
                .mapToObj(idx -> itemRepository.create(Item.create(new ItemCreateRequest("data " + idx))))
                .toList();

        List<Item> firstPage = itemRepository.readAll(1L, 2L);
        List<Item> secondPage = itemRepository.readAll(2L, 2L);

        assertThat(firstPage).hasSize(2);
        assertThat(firstPage.getFirst().getItemId()).isEqualTo(items.get(2).getItemId());
        assertThat(firstPage.get(1).getItemId()).isEqualTo(items.get(1).getItemId());

        assertThat(secondPage).hasSize(1);
        assertThat(secondPage.getFirst().getItemId()).isEqualTo(items.getFirst().getItemId());
    }

    @Test
    void readAllInfiniteScroll() {
        List<Item> items = IntStream.range(0, 5)
                .mapToObj(idx -> itemRepository.create(Item.create(new ItemCreateRequest("data " + idx))))
                .toList();

        List<Item> firstPage = itemRepository.readAllInfiniteScroll(null, 2L);
        List<Item> secondPage = itemRepository.readAllInfiniteScroll(firstPage.getLast().getItemId(), 2L);
        List<Item> thirdPage = itemRepository.readAllInfiniteScroll(secondPage.getLast().getItemId(), 2L);

        assertThat(firstPage).hasSize(2);
        assertThat(firstPage.getFirst().getItemId()).isEqualTo(items.get(4).getItemId());
        assertThat(firstPage.get(1).getItemId()).isEqualTo(items.get(3).getItemId());

        assertThat(secondPage).hasSize(2);
        assertThat(secondPage.getFirst().getItemId()).isEqualTo(items.get(2).getItemId());
        assertThat(secondPage.get(1).getItemId()).isEqualTo(items.get(1).getItemId());

        assertThat(thirdPage).hasSize(1);
        assertThat(thirdPage.getFirst().getItemId()).isEqualTo(items.get(0).getItemId());
    }

}
