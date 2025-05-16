package com.routinenyang.backend.routine.service;

import com.routinenyang.backend.global.exception.CustomException;
import com.routinenyang.backend.routine.dto.*;
import com.routinenyang.backend.routine.entity.Routine;
import com.routinenyang.backend.routine.entity.RoutineGroup;
import com.routinenyang.backend.routine.repository.RoutineGroupRepository;
import com.routinenyang.backend.routine.repository.RoutineRepository;
import com.routinenyang.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.routinenyang.backend.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class RoutineService {
    private final RoutineRepository routineRepository;
    private final RoutineGroupRepository routineGroupRepository;

    private final RoutineExecutionService routineExecutionService;

    public RoutineSummaryResponse create(User user, RoutineRequest request) {
        RoutineGroup routineGroup = routineGroupRepository.findById(request.getGroupId()).orElseThrow(
                () -> new CustomException(ROUTINE_GROUP_NOT_FOUND));

        Routine savedRoutine = routineRepository.save(Routine.builder()
                .userId(user.getId())
                .name(request.getName())
                .repeatDays(request.getRepeatDays())
                .preferredTime(request.getPreferredTime())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .color(request.getColor())
                .group(routineGroup)
                .build());

        return RoutineSummaryResponse.from(savedRoutine);
    }

    public RoutineSummaryResponse updateById(Long routineId, RoutineUpdateRequest request) {
        Routine routine = routineRepository.findById(routineId).orElseThrow(
                () -> new CustomException(ROUTINE_NOT_FOUND));
        RoutineGroup group = routineGroupRepository.findById(request.getGroupId()).orElseThrow(
                () -> new CustomException(ROUTINE_GROUP_NOT_FOUND));
        routine.update(request.getName(), request.getRepeatDays(), request.getPreferredTime(),
                request.getEndDate(), request.getColor(), group);

        return RoutineSummaryResponse.from(routine);
    }

    public Page<RoutineSummaryResponse> findAllWithFilter(User user, Long groupId, boolean activeOnly, Pageable pageable) {
        LocalDate today = LocalDate.now();
        Long userId = user.getId();
        Page<Routine> routines =
                (groupId != null && activeOnly)
                    ? routineRepository.findByUserIdAndGroupIdAndEndDateAfterAndDeletedFalse(userId, groupId, today, pageable)
                : (groupId != null)
                    ? routineRepository.findByUserIdAndGroupIdAndDeletedFalse(userId, groupId, pageable)
                : (activeOnly)
                    ? routineRepository.findByUserIdAndEndDateAfterAndDeletedFalse(userId, today, pageable)
                : routineRepository.findByUserIdAndDeletedFalse(userId, pageable);
        return routines.map(RoutineSummaryResponse::from);
    }

    public List<RoutineStatusResponse> findAllStatusByDate(User user, LocalDate date) {
        Long userId = user.getId();
        DayOfWeek dayOfWeek = date.getDayOfWeek(); // 파라미터로 받은 날짜의 요일

        // 해당 날짜에 수행해야하는 루틴 조회
        List<Routine> routines = routineRepository.findByUserIdAndCycleDayAndDateRange(userId, dayOfWeek, date);

        // 완료 여부 map 조회
        Map<Long, Boolean> completionMap = routineExecutionService.getCompletionMap(routines, date);

        return routines.stream()
                .map(routine -> RoutineStatusResponse.from(routine, completionMap.getOrDefault(routine.getId(), false)))
                .toList();
    }

    public RoutineResponse findById(Long routineId) {
        Routine routine = routineRepository.findById(routineId).orElseThrow(
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
