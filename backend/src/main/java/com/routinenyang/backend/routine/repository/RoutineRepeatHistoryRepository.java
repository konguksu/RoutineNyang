package com.routinenyang.backend.routine.repository;

import com.routinenyang.backend.routine.entity.RoutineRepeatHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public interface RoutineRepeatHistoryRepository extends JpaRepository<RoutineRepeatHistory, Long> {
    @Modifying
    @Query("UPDATE RoutineRepeatHistory h SET h.endDate = :endDate WHERE h.routine.id = :routineId AND h.endDate IS NULL")
    void closeAllActiveByRoutineId(@Param("routineId") Long routineId, @Param("endDate") LocalDate endDate);

    @Query("SELECT h.dayOfWeek FROM RoutineRepeatHistory h " +
            "WHERE h.routine.id = :routineId AND h.startDate <= :date AND (h.endDate IS NULL OR h.endDate >= :date)")
    List<DayOfWeek> findRepeatDaysByRoutineIdAndDate(@Param("routineId") Long routineId,
                                                     @Param("date") LocalDate date);

}
