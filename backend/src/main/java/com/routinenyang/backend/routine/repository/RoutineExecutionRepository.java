package com.routinenyang.backend.routine.repository;

import com.routinenyang.backend.routine.entity.Routine;
import com.routinenyang.backend.routine.entity.RoutineExecution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoutineExecutionRepository extends JpaRepository<RoutineExecution, Long> {
    Optional<RoutineExecution> findByRoutineAndDate(Routine routine, LocalDate date);

    boolean existsByRoutineAndDateAndCompletedTrue(Routine routine, LocalDate date);

    List<RoutineExecution> findAllByRoutineInAndDate(List<Routine> routines, LocalDate date);
}
