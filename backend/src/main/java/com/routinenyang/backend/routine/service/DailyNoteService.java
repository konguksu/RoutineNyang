package com.routinenyang.backend.routine.service;

import com.routinenyang.backend.global.exception.CustomException;
import com.routinenyang.backend.routine.dto.DailyNoteRequest;
import com.routinenyang.backend.routine.dto.DailyNoteResponse;
import com.routinenyang.backend.routine.entity.DailyNote;
import com.routinenyang.backend.routine.entity.Routine;
import com.routinenyang.backend.routine.repository.DailyNoteRepository;
import com.routinenyang.backend.routine.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static com.routinenyang.backend.global.exception.ErrorCode.ROUTINE_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class DailyNoteService {
    private final RoutineRepository routineRepository;
    private final DailyNoteRepository dailyNoteRepository;

    public void update(Long userId, Long routineId, DailyNoteRequest request) {
        // 루틴 검증
        Routine routine = routineRepository.findByIdAndUserIdAndDeletedFalse(routineId, userId).orElseThrow(
                () -> new CustomException(ROUTINE_NOT_FOUND)
        );

        // dailyNote 찾기
        Optional<DailyNote> optionalDailyNote = dailyNoteRepository
                .findByRoutineAndDate(routine, request.getDate());

        DailyNote dailyNote;
        if (optionalDailyNote.isPresent()) {
            // 이미 존재하면 업데이트
            dailyNote = optionalDailyNote.get();
            dailyNote.update(request.getContent());
        } else {
            // 없으면 새로 생성
            dailyNote = DailyNote.builder()
                    .routine(routine)
                    .date(request.getDate())
                    .content(request.getContent())
                    .build();
        }

        // 저장
        dailyNoteRepository.save(dailyNote);
    }

    public DailyNoteResponse findDailyNoteByRoutineAndDate(Long userId, Long routineId, LocalDate date) {
        Routine routine = routineRepository.findByIdAndUserIdAndDeletedFalse(routineId, userId).orElseThrow(
                () -> new CustomException(ROUTINE_NOT_FOUND)
        );

        String content = dailyNoteRepository.findByRoutineAndDate(routine, date)
                .map(DailyNote::getContent)
                .orElse("");
        return DailyNoteResponse.builder()
                .content(content)
                .build();
    }
}
