package com.routinenyang.backend.store.dto;

import com.routinenyang.backend.store.entity.Coin;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CoinResponse {
    private int amount;

    public static CoinResponse from(Coin coin){
        return CoinResponse.builder()
                .amount(coin.getAmount())
                .build();
    }
}
