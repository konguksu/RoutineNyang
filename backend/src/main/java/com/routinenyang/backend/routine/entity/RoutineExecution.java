package com.routinenyang.backend.routine.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.routinenyang.backend.global.base.BaseEntity;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "routine_execution",
        indexes = {
                @Index(name = "idx_routine_execution_routine_id", columnList = "routine_id"),
                @Index(name = "idx_routine_execution_date", columnList = "date")
        },
        uniqueConstraints = {
                // 루틴 id + 날짜 조합은 유일해야한다는 제약조건 설정
                @UniqueConstraint(columnNames = {"routine_id", "date"})
        }
)
public class RoutineExecution extends BaseEntity {

    @Id @Column(name = "execution_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    // 기록 대상 루틴
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id")
    private Routine routine;

    // 수행 날짜
    private LocalDate date;

    // 성공 여부
    @Column(nullable = false)
    @Builder.Default
    private boolean completed = false;

    // 성공 여부 반대로 변환
    public void toggle() {
        this.completed = !this.completed;
    }
}
