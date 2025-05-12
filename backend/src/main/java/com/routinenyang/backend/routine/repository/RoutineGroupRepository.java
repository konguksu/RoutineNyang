package com.routinenyang.backend.routine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.routinenyang.backend.routine.entity.RoutineGroup;

import java.util.List;
import java.util.Optional;

public interface RoutineGroupRepository extends JpaRepository<RoutineGroup, Long> {
    List<RoutineGroup> findByUserIdOrderByNameAsc(Long userId);

    Optional<RoutineGroup> findByUserIdAndName(Long id, String name);
}
