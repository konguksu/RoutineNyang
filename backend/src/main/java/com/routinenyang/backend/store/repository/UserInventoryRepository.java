package com.routinenyang.backend.store.repository;

import com.routinenyang.backend.store.entity.Item;
import com.routinenyang.backend.store.entity.UserInventory;
import com.routinenyang.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserInventoryRepository extends JpaRepository<UserInventory, Long> {
    List<UserInventory> findByUser(User user);
    Optional<UserInventory> findByUserAndItem(User user, Item item);
}
