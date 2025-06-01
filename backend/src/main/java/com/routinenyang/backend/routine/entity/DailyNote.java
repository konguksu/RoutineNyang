package com.routinenyang.backend.routine.entity;

import com.routinenyang.backend.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "daily_note",
        indexes = {
                @Index(name = "idx_daily_note_routine_id", columnList = "routine_id"),
                @Index(name = "idx_daily_note_date", columnList = "date")
        },
        uniqueConstraints = {
                // 루틴 id + 날짜 조합은 유일해야한다는 제약조건 설정
                @UniqueConstraint(columnNames = {"routine_id", "date"})
        }
)
public class DailyNote extends BaseEntity {

        @Id @Column(name = "daily_note_id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        // 기록 대상 루틴
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "routine_id")
        private Routine routine;

        private LocalDate date; // 기록 날짜
        private String content;

        public void update(String content) {
                this.content = content;
        }
}
