package com.routinenyang.backend.store.controller;

import com.routinenyang.backend.auth.resolver.CurrentUser;
import com.routinenyang.backend.global.response.ApiResponse;
import com.routinenyang.backend.global.response.ResponseFactory;
import com.routinenyang.backend.store.dto.ItemAdminResponse;
import com.routinenyang.backend.store.dto.ItemRequest;
import com.routinenyang.backend.store.dto.ItemResponse;
import com.routinenyang.backend.store.service.ItemService;
import com.routinenyang.backend.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/items")
@Tag(name = "Item", description = "방꾸미기 아이템 API")
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    @Operation(summary = "아이템 목록 조회", description = "상점에서 판매하는 아이템 목록 조회. 로그인한 유저의 구매와 장착 여부 포함")
    public ResponseEntity<ApiResponse<List<ItemResponse>>> findAll(
            @Parameter(hidden = true) @CurrentUser User user
    ) {
        return ResponseFactory.ok(itemService.findAll(user));
    }

    @PostMapping("/purchase/{item-id}")
    @Operation(summary = "아이템 구매", description = "판매하는 아이템 구매")
    public ResponseEntity<ApiResponse<ItemResponse>> purchaseByUser(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable("item-id") Long itemId
    ) {
        return ResponseFactory.ok(itemService.purchase(user, itemId));
    }

    @PatchMapping("/equip-toggle/{item-id}")
    @Operation(summary = "아이템 장착 여부 토글", description = "아이템 장착 여부를 토글")
    public ResponseEntity<ApiResponse<ItemResponse>> toggleEquipped(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable("item-id") Long itemId
    ) {
        return ResponseFactory.ok(itemService.toggleEquipped(user, itemId));
    }

    @GetMapping("/admin")
    @Operation(summary = "[관리자] 아이템 목록 조회", description = "상점에서 판매하는 모든 아이템 목록 조회")
    public ResponseEntity<ApiResponse<List<ItemAdminResponse>>> findAllForAdmin() {
        return ResponseFactory.ok(itemService.findAllForAdmin());
    }


    @PostMapping("/admin")
    @Operation(summary = "[관리자] 아이템 추가", description = "상점에서 판매하는 아이템 추가")
    public ResponseEntity<ApiResponse<ItemAdminResponse>> create(
            @RequestBody ItemRequest request
            ) {
        return ResponseFactory.ok(itemService.create(request));
    }

    @PutMapping("/admin/{item-id}")
    @Operation(summary = "[관리자] 아이템 수정", description = "상점에서 판매하는 아이템 수정")
    public ResponseEntity<ApiResponse<ItemAdminResponse>> update(
            @PathVariable("item-id") Long itemId,
            @RequestBody ItemRequest request
            ) {
        return ResponseFactory.ok(itemService.update(itemId, request));
    }

    @DeleteMapping("/admin/{item-id}")
    @Operation(summary = "[관리자] 아이템 삭제", description = "상점에서 판매하는 아이템 삭제")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable("item-id") Long itemId
    ) {
        itemService.delete(itemId);
        return ResponseFactory.ok(null);
    }
}
