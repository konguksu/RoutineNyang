package com.routinenyang.backend.routine.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.routinenyang.backend.global.base.BaseEntity;
import org.hibernate.annotations.Where;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(
        indexes = {
                @Index(name = "idx_routine_group_user_id", columnList = "user_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_group_name", columnNames = {"user_id", "name"})
        }
)
public class RoutineGroup extends BaseEntity {

    @Id @Column(name = "group_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    private String name;

    @Builder.Default
    private boolean deleted = false;

    public void update(String name) {
        this.name = name;
    }
}