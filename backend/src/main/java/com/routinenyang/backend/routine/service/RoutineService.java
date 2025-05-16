package com.routinenyang.backend.routine.service;

import com.routinenyang.backend.global.exception.CustomException;
import com.routinenyang.backend.routine.dto.RoutineDetailResponse;
import com.routinenyang.backend.routine.dto.RoutineRequest;
import com.routinenyang.backend.routine.dto.RoutineResponse;
import com.routinenyang.backend.routine.dto.RoutineUpdateRequest;
import com.routinenyang.backend.routine.entity.Routine;
import com.routinenyang.backend.routine.entity.RoutineGroup;
import com.routinenyang.backend.routine.repository.RoutineExecutionRepository;
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

import static com.routinenyang.backend.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class RoutineService {
    private final RoutineRepository routineRepository;
    private final RoutineGroupRepository routineGroupRepository;
    private final RoutineExecutionRepository routineExecutionRepository;

    public RoutineResponse create(User user, RoutineRequest request) {
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

        return RoutineResponse.from(savedRoutine);
    }

    public RoutineResponse updateById(Long routineId, RoutineUpdateRequest request) {
        Routine routine = routineRepository.findById(routineId).orElseThrow(
                () -> new CustomException(ROUTINE_NOT_FOUND));
        RoutineGroup group = routineGroupRepository.findById(request.getGroupId()).orElseThrow(
                () -> new CustomException(ROUTINE_GROUP_NOT_FOUND));
        routine.update(request.getName(), request.getRepeatDays(), request.getPreferredTime(),
                request.getEndDate(), request.getColor(), group);

        return RoutineResponse.from(routine);
    }

    public Page<RoutineResponse> findAllWithFilter(User user, Long groupId, boolean activeOnly, Pageable pageable) {
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
        return routines.map(RoutineResponse::from);
    }

    public List<RoutineResponse> findAllByDate(User user, LocalDate date) {
        Long userId = user.getId();
        DayOfWeek dayOfWeek = date.getDayOfWeek(); // 파라미터로 받은 날짜의 요일

        return routineRepository
                .findByUserIdAndCycleDayAndDateRange(userId, dayOfWeek, date)
                .stream()
                .map(RoutineResponse::from)
                .toList();
    }

    public RoutineDetailResponse findById(Long routineId) {
        Routine routine = routineRepository.findById(routineId).orElseThrow(
                () -> new CustomException(ROUTINE_NOT_FOUND)
        );
        return RoutineDetailResponse.from(routine);
    }

    public void deleteById(Long routineId) {
        Routine routine = routineRepository.findById(routineId).orElseThrow(
                () -> new CustomException(ROUTINE_NOT_FOUND)
        );
        if (routine.isDeleted()) throw new CustomException(ROUTINE_ALREADY_DELETED);
        routine.setAsDeleted();
    }
}
