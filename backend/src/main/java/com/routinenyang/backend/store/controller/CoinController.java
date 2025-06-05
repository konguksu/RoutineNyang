package com.routinenyang.backend.store.controller;

import com.routinenyang.backend.auth.resolver.CurrentUser;
import com.routinenyang.backend.global.response.ApiResponse;
import com.routinenyang.backend.global.response.ResponseFactory;
import com.routinenyang.backend.store.dto.CoinResponse;
import com.routinenyang.backend.store.service.CoinService;
import com.routinenyang.backend.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/coin")
@Tag(name = "Coin", description = "냥코인 API")
public class CoinController {

    private final CoinService coinService;

    @GetMapping
    @Operation(summary = "냥코인 조회", description = "유저가 현재 보유한 냥코인 조회")
    public ResponseEntity<ApiResponse<CoinResponse>> getAmountByUser(
            @Parameter(hidden = true) @CurrentUser User user
    ) {
        return ResponseFactory.ok(coinService.getAmountByUserId(user.getId()));
    }

}
