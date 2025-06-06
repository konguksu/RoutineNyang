package com.routinenyang.backend.store.entity;

import com.routinenyang.backend.global.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
public class Item extends BaseEntity {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;
    private int price;
    private String imgUrl;

    public void update(String name, int price, String imgUrl) {
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
    }
}
