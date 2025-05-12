package com.routinenyang.backend.routine.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.routinenyang.backend.routine.entity.Routine;
import org.springframework.data.jpa.repository.Query;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface RoutineRepository extends JpaRepository<Routine, Long> {
    Page<Routine> findByUserIdAndGroupIdAndEndDateAfterAndDeletedFalse(Long userId, Long groupId, LocalDate today, Pageable pageable);
    Page<Routine> findByUserIdAndGroupIdAndDeletedFalse(Long userId, Long groupId, Pageable pageable);
    Page<Routine> findByUserIdAndEndDateAfterAndDeletedFalse(Long userId, LocalDate today, Pageable pageable);
    Page<Routine> findByUserIdAndDeletedFalse(Long userId, Pageable pageable);

    @Query("SELECT r FROM Routine r WHERE r.userId = :userId " +
            "AND :dayOfWeek MEMBER OF r.repeatDays AND r.deleted = false " +
            "AND r.startDate <= :date AND r.endDate >= :date")
    List<Routine> findByUserIdAndCycleDayAndDateRange(Long userId, DayOfWeek dayOfWeek, LocalDate date);

    List<Routine> findByGroupIdAndDeletedFalse(Long groupId);
}
