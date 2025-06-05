package com.routinenyang.backend.store.repository;

import com.routinenyang.backend.store.entity.Coin;
import com.routinenyang.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoinRepository extends JpaRepository<Coin, Long> {
    Optional<Coin> findByUser(User user);
}