package com.example.demo.model;

import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicLong;

@ToString
@Getter
public class Item {

    private Long itemId;
    private String data;

    private static final AtomicLong NEXT_ID = new AtomicLong();

    public static Item create(ItemCreateRequest request) {
        Item item = new Item();
        item.itemId = NEXT_ID.incrementAndGet();
        item.data = request.data();
        return item;
    }

    public Item update(ItemUpdateRequest request) {
        this.data = request.data();
        return this;
    }

}
