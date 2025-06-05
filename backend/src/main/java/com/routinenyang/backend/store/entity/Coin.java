package com.routinenyang.backend.store.entity;

import com.routinenyang.backend.global.base.BaseEntity;
import com.routinenyang.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Coin extends BaseEntity {

    @Id
    @Column(name = "coin_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Builder.Default
    private int amount = 0;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void addAmount(int value) {
        this.amount += value;
    }

    public void removeAmount(int value) {
        this.amount -= value;
    }
}
