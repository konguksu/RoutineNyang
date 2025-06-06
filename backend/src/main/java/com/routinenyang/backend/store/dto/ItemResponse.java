package com.routinenyang.backend.store.dto;

import com.routinenyang.backend.store.entity.Item;
import com.routinenyang.backend.store.entity.UserInventory;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemResponse {
    private Long itemId;
    private String name;
    private int price;
    private String imgUrl;
    private boolean purchased;
    private boolean equipped;

    public static ItemResponse from(UserInventory inventory) {
        Item item = inventory.getItem();
        return ItemResponse.builder()
                .itemId(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .imgUrl(item.getImgUrl())
                .purchased(true)
                .equipped(inventory.isEquipped())
                .build();
    }
}
