package com.routinenyang.backend.routine.service;

import com.routinenyang.backend.global.exception.CustomException;
import com.routinenyang.backend.routine.dto.*;
import com.routinenyang.backend.routine.entity.Routine;
import com.routinenyang.backend.routine.entity.RoutineGroup;
import com.routinenyang.backend.routine.entity.RoutineRepeatHistory;
import com.routinenyang.backend.routine.repository.RoutineGroupRepository;
import com.routinenyang.backend.routine.repository.RoutineRepeatHistoryRepository;
import com.routinenyang.backend.routine.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.routinenyang.backend.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class RoutineService {
    private final RoutineRepository routineRepository;
    private final RoutineGroupRepository routineGroupRepository;
    private final RoutineRepeatHistoryRepository routineRepeatHistoryRepository;

    private final RoutineExecutionService routineExecutionService;

    public RoutineSummaryResponse create(Long userId, RoutineRequest request) {
        RoutineGroup routineGroup = routineGroupRepository.findById(request.getGroupId()).orElseThrow(
                () -> new CustomException(ROUTINE_GROUP_NOT_FOUND));

        Routine savedRoutine = routineRepository.save(Routine.builder()
                .userId(userId)
                .name(request.getName())
                .repeatDays(request.getRepeatDays())
                .preferredTime(request.getPreferredTime())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .color(request.getColor())
                .group(routineGroup)
                .build());

        for (DayOfWeek day : request.getRepeatDays()) {
            routineRepeatHistoryRepository.save(RoutineRepeatHistory.builder()
                    .routine(savedRoutine)
                    .dayOfWeek(day)
                    .startDate(request.getStartDate())
                    .endDate(null)
                    .build());
        }

        return RoutineSummaryResponse.from(savedRoutine);
    }

    public RoutineSummaryResponse updateById(Long routineId, RoutineUpdateRequest request) {
        Routine routine = routineRepository.findById(routineId).orElseThrow(
                () -> new CustomException(ROUTINE_NOT_FOUND));
        RoutineGroup group = routineGroupRepository.findById(request.getGroupId()).orElseThrow(
                () -> new CustomException(ROUTINE_GROUP_NOT_FOUND));
        routine.update(request.getName(), request.getPreferredTime(), request.getEndDate(), request.getColor(), group);

        // 반복 요일 변경 감지 및 이력 처리
        Set<DayOfWeek> newRepeatDays = request.getRepeatDays();
        if (!routine.isSameRepeatDays(newRepeatDays)) {
            // 기존 이력 종료
            routineRepeatHistoryRepository.closeAllActiveByRoutineId(routineId, LocalDate.now().minusDays(1));

            // 새 이력 삽입
            for (DayOfWeek newDay : newRepeatDays) {
                routineRepeatHistoryRepository.save(RoutineRepeatHistory.builder()
                        .routine(routine)
                        .dayOfWeek(newDay)
                        .startDate(LocalDate.now())
                        .endDate(null)
                        .build());
            }

            // 현재 상태값도 갱신
            routine.updateRepeatDays(newRepeatDays);
        }

        return RoutineSummaryResponse.from(routine);
    }

    public Page<RoutineSummaryResponse> findAllWithFilter(Long userId, Long groupId, boolean isActive, Pageable pageable) {
        LocalDate today = LocalDate.now();
        Page<Routine> routines =
                (groupId != null && isActive)
                    ? routineRepository.findByUserIdAndGroupIdAndEndDateAfterAndDeletedFalse(userId, groupId, today.minusDays(1), pageable)
                : (groupId != null)
                    ? routineRepository.findByUserIdAndGroupIdAndEndDateBeforeAndDeletedFalse(userId, groupId, today, pageable)
                : (isActive)
                    ? routineRepository.findByUserIdAndEndDateAfterAndDeletedFalse(userId, today.minusDays(1), pageable)
                : routineRepository.findByUserIdAndEndDateBeforeAndDeletedFalse(userId, today, pageable);
        return routines.map(RoutineSummaryResponse::from);
    }

    public List<RoutineStatusResponse> findAllStatusByDate(Long userId, LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek(); // 파라미터로 받은 날짜의 요일

        // 해당 날짜에 수행해야하는 루틴 조회
        List<Routine> routines = routineRepository.findByUserIdAndCycleDayAndDateRange(userId, dayOfWeek, date);

        // 완료 여부 map 조회
        Map<Long, Boolean> completionMap = routineExecutionService.getCompletionMap(routines, date);

        return routines.stream()
                .map(routine -> RoutineStatusResponse.from(routine, completionMap.getOrDefault(routine.getId(), false)))
                .toList();
    }

    public RoutineResponse findById(Long userId, Long routineId) {
        Routine routine = routineRepository.findByIdAndUserIdAndDeletedFalse(routineId, userId).orElseThrow(
                () -> new CustomException(ROUTINE_NOT_FOUND)
        );
        return RoutineResponse.from(routine);
    }

    public void deleteById(Long routineId) {
        Routine routine = routineRepository.findById(routineId).orElseThrow(
                () -> new CustomException(ROUTINE_NOT_FOUND)
        );
        if (routine.isDeleted()) throw new CustomException(ROUTINE_ALREADY_DELETED);
        routine.setAsDeleted();
    }
}
