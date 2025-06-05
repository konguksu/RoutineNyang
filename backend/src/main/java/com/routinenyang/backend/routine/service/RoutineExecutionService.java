package com.routinenyang.backend.routine.service;

import com.routinenyang.backend.global.exception.CustomException;
import com.routinenyang.backend.routine.entity.Routine;
import com.routinenyang.backend.routine.entity.RoutineExecution;
import com.routinenyang.backend.routine.repository.RoutineExecutionRepository;
import com.routinenyang.backend.routine.repository.RoutineRepository;
import com.routinenyang.backend.store.service.CoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.routinenyang.backend.global.exception.ErrorCode.ROUTINE_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class RoutineExecutionService {
    private final RoutineRepository routineRepository;
    private final RoutineExecutionRepository routineExecutionRepository;
    private final CoinService coinService;

    private static final int REWARD_COIN_AMOUNT = 10;

    // 루틴 수행 여부 토글
    public void toggleExecution(Long userId, Long routineId, LocalDate date) {
        Routine routine = routineRepository.findById(routineId).orElseThrow(
                () -> new CustomException(ROUTINE_NOT_FOUND)
        );
        // 루틴 수행 기록 조회, 없으면 생성 & 리워드 획득
        Optional<RoutineExecution> recordOpt = routineExecutionRepository.findByRoutineAndDate(routine, date);
        RoutineExecution record;
        if (recordOpt.isEmpty()) {
            record = routineExecutionRepository.save(RoutineExecution.builder()
                    .routine(routine)
                    .date(date)
                    .build());
            coinService.addCoinByUserId(userId, REWARD_COIN_AMOUNT);
        } else {
            record = recordOpt.get();
        }
        record.toggle(); // 수행 여부 토글
    }

    // 특정 날짜에 루틴들의 수행 완료 여부 Map 반환
    public Map<Long, Boolean> getCompletionMap(List<Routine> routines, LocalDate date) {
        List<RoutineExecution> executions = routineExecutionRepository.findAllByRoutineInAndDate(routines, date);

        Set<Long> completedIds = executions.stream()
                .filter(RoutineExecution::isCompleted)
                .map(exec -> exec.getRoutine().getId())
                .collect(Collectors.toSet());

        return routines.stream()
                .collect(Collectors.toMap(
                        Routine::getId,
                        routine -> completedIds.contains(routine.getId())
                ));
    }
}