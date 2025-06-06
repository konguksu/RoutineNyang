package com.routinenyang.backend.store.dto;

import com.routinenyang.backend.store.entity.Item;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ItemAdminResponse {
    private Long itemId;
    private String name;
    private int price;
    private String imgUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ItemAdminResponse from(Item item) {
        return ItemAdminResponse.builder()
                .itemId(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .imgUrl(item.getImgUrl())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }
}
