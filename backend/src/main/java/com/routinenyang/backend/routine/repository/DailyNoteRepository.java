package com.routinenyang.backend.routine.repository;

import com.routinenyang.backend.routine.entity.DailyNote;
import com.routinenyang.backend.routine.entity.Routine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyNoteRepository extends JpaRepository<DailyNote, Long> {
    Optional<DailyNote> findByRoutineAndDate(Routine routine, LocalDate date);
}
