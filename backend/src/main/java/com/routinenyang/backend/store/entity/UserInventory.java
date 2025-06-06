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
@Table(
        name = "user_inventory",
        indexes = {
                @Index(name = "idx_user_inventory_item_id", columnList = "item_id"),
                @Index(name = "idx_user_inventory_user_id", columnList = "user_id")
        }
)
public class UserInventory extends BaseEntity {
    @Id
    @Column(name = "user_inventory_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Builder.Default
    private boolean equipped = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public void toggleEquipped() {
        this.equipped = !equipped;
    }
}
