package com.routinenyang.backend.routine.entity;

import com.routinenyang.backend.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "routine_repeat_history",
        indexes = {
                @Index(name = "idx_routine_repeat_history_routine_id", columnList = "routine_id"),
                @Index(name = "idx_routine_repeat_history_start_end", columnList = "start_date, end_date")
        }
)
public class RoutineRepeatHistory extends BaseEntity {

    @Id
    @Column(name = "repeat_history_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id", nullable = false)
    private Routine routine;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate; // null이면 현재까지 유효

    public void closeHistory(LocalDate endDate) {
        this.endDate = endDate;
    }
}
