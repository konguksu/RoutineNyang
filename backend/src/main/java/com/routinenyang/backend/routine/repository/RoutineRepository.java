package com.routinenyang.backend.routine.repository;

import com.routinenyang.backend.routine.entity.Routine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoutineRepository extends JpaRepository<Routine, Long> {
    Page<Routine> findByUserIdAndGroupIdAndEndDateAfterAndDeletedFalse(Long userId, Long groupId, LocalDate yesterday, Pageable pageable);
    Page<Routine> findByUserIdAndGroupIdAndEndDateBeforeAndDeletedFalse(Long userId, Long groupId, LocalDate today, Pageable pageable);
    Page<Routine> findByUserIdAndEndDateAfterAndDeletedFalse(Long userId, LocalDate yesterday, Pageable pageable);
    Page<Routine> findByUserIdAndEndDateBeforeAndDeletedFalse(Long userId, LocalDate today, Pageable pageable);

    @Query("SELECT r FROM Routine r WHERE r.userId = :userId " +
            "AND :dayOfWeek MEMBER OF r.repeatDays AND r.deleted = false " +
            "AND r.startDate <= :date AND r.endDate >= :date")
    List<Routine> findByUserIdAndCycleDayAndDateRange(Long userId, DayOfWeek dayOfWeek, LocalDate date);

    List<Routine> findByGroupIdAndDeletedFalse(Long groupId);

    Optional<Routine> findByIdAndUserIdAndDeletedFalse(Long routineId, Long userId);
}