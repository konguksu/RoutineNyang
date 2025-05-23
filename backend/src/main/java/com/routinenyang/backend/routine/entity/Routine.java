package com.routinenyang.backend.routine.entity;

import jakarta.persistence.*;
import lombok.*;
import com.routinenyang.backend.global.base.BaseEntity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @OneToMany(mappedBy = "routine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoutineRepeatHistory> repeatHistories;

    // 소프트 딜리트 여부
    @Builder.Default
    private boolean deleted = false;

    public void update(String name, String preferredTime, LocalDate endDate, String color, RoutineGroup group) {
        this.name = name;
        this.preferredTime = preferredTime;
        this.endDate = endDate;
        this.color = color;
        this.group = group;
    }

    public void updateRepeatDays(Set<DayOfWeek> repeatDays) {
        this.repeatDays.clear();
        this.repeatDays.addAll(repeatDays);
    }

    public void moveToGroup(RoutineGroup group) {
        this.group = group;
    }

    public void setAsDeleted() {
        this.deleted = true;
    }

    public boolean isSameRepeatDays(Set<DayOfWeek> other) {
        return this.repeatDays != null && new HashSet<>(this.repeatDays).equals(other);
    }
}