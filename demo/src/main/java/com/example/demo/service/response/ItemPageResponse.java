package com.example.demo.service.response;

import com.example.demo.model.Item;

import java.util.List;

public record ItemPageResponse(
        List<ItemResponse> items,
        long count
) {

    public static ItemPageResponse fromResponses(List<ItemResponse> items, long count) {
        return new ItemPageResponse(items, count);
    }

    public static ItemPageResponse from(List<Item> items, long count) {
        return fromResponses(items.stream().map(ItemResponse::from).toList(), count);
    }

}
