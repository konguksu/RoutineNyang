package com.routinenyang.backend.routine.repository;

import com.routinenyang.backend.routine.entity.Routine;
import com.routinenyang.backend.routine.entity.RoutineExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoutineExecutionRepository extends JpaRepository<RoutineExecution, Long> {
    Optional<RoutineExecution> findByRoutineAndDate(Routine routine, LocalDate date);

    boolean existsByRoutineAndDateAndCompletedTrue(Routine routine, LocalDate date);

    List<RoutineExecution> findAllByRoutineInAndDate(List<Routine> routines, LocalDate date);

    int countByRoutineIdAndCompletedIsTrueAndDateBetween(Long routineId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT e.date FROM RoutineExecution e " +
            "WHERE e.routine.id = :routineId AND e.completed = true " +
            "ORDER BY e.date DESC")
    List<LocalDate> findAllCompletedDatesByRoutineIdOrderedDesc(@Param("routineId") Long routineId);

    @Query("SELECT e.date FROM RoutineExecution e WHERE e.routine.id = :routineId AND e.completed = true AND e.date BETWEEN :start AND :end")
    List<LocalDate> findAllCompletedDatesByRoutineIdBetween(@Param("routineId") Long routineId,
                                                            @Param("start") LocalDate start,
                                                            @Param("end") LocalDate end);
}
