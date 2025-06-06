package com.routinenyang.backend.store.service;

import com.routinenyang.backend.global.exception.CustomException;
import com.routinenyang.backend.store.dto.ItemAdminResponse;
import com.routinenyang.backend.store.dto.ItemRequest;
import com.routinenyang.backend.store.dto.ItemResponse;
import com.routinenyang.backend.store.entity.Item;
import com.routinenyang.backend.store.entity.UserInventory;
import com.routinenyang.backend.store.repository.ItemRepository;
import com.routinenyang.backend.store.repository.UserInventoryRepository;
import com.routinenyang.backend.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.routinenyang.backend.global.exception.ErrorCode.ITEM_NOT_FOUND;
import static com.routinenyang.backend.global.exception.ErrorCode.ITEM_NOT_PURCHASED;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserInventoryRepository userInventoryRepository;
    private final CoinService coinService;

    public List<ItemResponse> findAll(User user) {

        // 전체 아이템 조회
        List<Item> itemList = itemRepository.findAll();

        // 유저가 소유한 아이템 목록 조회
        List<UserInventory> userItemList = userInventoryRepository.findByUser(user);

        // 유저가 소유한 아이템 목록을 itemId 기준 Map으로 변환
        Map<Long, UserInventory> userItemMap = userItemList.stream()
                .collect(Collectors.toMap(
                        userInventory -> userInventory.getItem().getId(),
                        userInventory -> userInventory
                ));

        // Item 리스트 순회하며 ItemResponse 생성
        return itemList.stream()
                .map(item -> {
                    UserInventory userItem = userItemMap.get(item.getId());
                    boolean purchased = userItem != null;
                    boolean equipped = purchased && userItem.isEquipped();

                    return ItemResponse.builder()
                            .itemId(item.getId())
                            .name(item.getName())
                            .price(item.getPrice())
                            .imgUrl(item.getImgUrl())
                            .purchased(purchased)
                            .equipped(equipped)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public List<ItemResponse> findAllByUser(User user) {
        // 유저가 소유한 아이템 목록 조회
        List<UserInventory> userItemList = userInventoryRepository.findByUser(user);

        return userItemList.stream()
                .map(ItemResponse::from)
                .collect(Collectors.toList());
    }

    public ItemResponse purchase(User user, Long itemId) {
        // id로 아이템 찾기
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new CustomException(ITEM_NOT_FOUND)
        );
        // 아이템 가격만큼 유저가 보유한 코인 감소
        coinService.removeCoinByUserId(user.getId(), item.getPrice());

        return ItemResponse.from(userInventoryRepository.save(UserInventory.builder()
                .user(user)
                .item(item)
                .build()));
    }

    public ItemResponse toggleEquipped(User user, Long itemId) {
        // id로 아이템 찾기
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new CustomException(ITEM_NOT_FOUND)
        );

        UserInventory inventory = userInventoryRepository.findByUserAndItem(user, item).orElseThrow(
                () -> new CustomException(ITEM_NOT_PURCHASED)
        );

        inventory.toggleEquipped(); // 장착 여부 반대로
        return ItemResponse.from(inventory);
    }

    public ItemAdminResponse create(ItemRequest request) {
        return ItemAdminResponse.from(itemRepository.save(request.toEntity()));
    }

    public List<ItemAdminResponse> findAllForAdmin() {
        return itemRepository.findAll().stream()
                .map(ItemAdminResponse::from)
                .collect(Collectors.toList());
    }

    public ItemAdminResponse update(Long itemId, ItemRequest request) {
        // id로 아이템 찾기
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new CustomException(ITEM_NOT_FOUND)
        );
        // 아이템 정보 수정
        item.update(request.getName(), request.getPrice(), request.getImgUrl());

        return ItemAdminResponse.from(item);
    }

    public void delete(Long itemId) {
        // id로 아이템 찾기
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new CustomException(ITEM_NOT_FOUND)
        );
        itemRepository.delete(item);
    }
}
