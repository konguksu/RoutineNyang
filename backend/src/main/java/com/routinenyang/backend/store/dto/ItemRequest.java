package com.routinenyang.backend.store.dto;

import com.routinenyang.backend.store.entity.Item;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemRequest {
    private String name;
    private int price;
    private String imgUrl;

    public Item toEntity() {
        return Item.builder()
                .name(this.name)
                .price(this.price)
                .imgUrl(this.imgUrl)
                .build();
    }
}
