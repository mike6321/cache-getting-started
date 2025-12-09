package com.example.demo.service.response;

import com.example.demo.model.Item;

public record ItemResponse(Long itemId, String data) {

    public static ItemResponse from(Item item) {
        return new ItemResponse(item.getItemId(), item.getData());
    }

}
