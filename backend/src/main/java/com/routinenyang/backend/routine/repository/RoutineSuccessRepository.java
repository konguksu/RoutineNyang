package com.routinenyang.backend.routine.repository;

import com.routinenyang.backend.routine.entity.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import com.routinenyang.backend.routine.entity.RoutineSuccess;

import java.util.Optional;

public interface RoutineSuccessRepository extends JpaRepository<RoutineSuccess, Long> {
    Optional<RoutineSuccess> findByRoutine(Routine routine);
}
