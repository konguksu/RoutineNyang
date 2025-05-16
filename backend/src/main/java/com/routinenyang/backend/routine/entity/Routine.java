package com.routinenyang.backend.routine.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.routinenyang.backend.global.base.BaseEntity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(
        indexes = {
                // userId에 인덱스 추가
                @Index(name = "idx_routine_user_id", columnList = "user_id")
        }
)
public class Routine extends BaseEntity {

    @Id @Column(name = "routine_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "routine_repeat_days", joinColumns = @JoinColumn(name = "routine_id"))
    @Column(name = "day_of_week")
    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> repeatDays;

    private String preferredTime;
    private LocalDate startDate;
    private LocalDate endDate;
    private String color; // 색상 코드

    // 소속 그룹
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private RoutineGroup group;

    // 소프트 딜리트 여부
    @Builder.Default
    private boolean deleted = false;

    public void update(String name, Set<DayOfWeek> repeatDays, String preferredTime, LocalDate endDate, String color, RoutineGroup group) {
        this.name = name;
        this.repeatDays = repeatDays;
        this.preferredTime = preferredTime;
        this.endDate = endDate;
        this.color = color;
        this.group = group;
    }

    public void moveToGroup(RoutineGroup newGroup) {
        this.group = newGroup;
    }

    public void setAsDeleted() {
        deleted = true;
    }
}