package com.routinenyang.backend.report.service;

import com.routinenyang.backend.global.exception.CustomException;
import com.routinenyang.backend.report.dto.*;
import com.routinenyang.backend.routine.entity.Routine;
import com.routinenyang.backend.routine.repository.RoutineExecutionRepository;
import com.routinenyang.backend.routine.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

import static com.routinenyang.backend.global.exception.ErrorCode.ROUTINE_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class RoutineReportService {

    private static final int MONTH_RANGE = 6;

    private final RoutineRepository routineRepository;
    private final RoutineExecutionRepository routineExecutionRepository;

    // 6개월치 월별 수행 횟수 반환
    public List<RoutineMonthlyReportResponse> getMonthlyReport(Long userId, Long routineId, YearMonth startYm) {
        validateRoutine(userId, routineId);
        List<RoutineMonthlyReportResponse> results = new ArrayList<>();
        if (startYm == null) {
            // 시작월 지정되지 않았으면 기본값 할당(현재 기준)
            startYm = YearMonth.now().minusMonths(MONTH_RANGE - 1);
        }

        for (int i = 0; i < MONTH_RANGE; i++) {
            YearMonth ym = startYm.plusMonths(i);
            int count = routineExecutionRepository.countByRoutineIdAndCompletedIsTrueAndDateBetween(
                    routineId, ym.atDay(1), ym.atEndOfMonth()
            );
            results.add(RoutineMonthlyReportResponse.builder()
                            .year(ym.getYear())
                            .month(ym.getMonthValue())
                            .successCount(count)
                            .build());
        }
        return results;
    }

    public RoutineStreakReportResponse getStreakReport(Long userId, Long routineId) {
        Routine routine = validateRoutine(userId, routineId);
        Set<DayOfWeek> repeatDays = routine.getRepeatDays();

        // 과거 180일까지 수행 기록 확인 (성능/현실성 고려)
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(180);

        // 성공한 날짜 목록
        Set<LocalDate> completedDates = new HashSet<>(
                routineExecutionRepository.findAllCompletedDatesByRoutineIdBetween(routineId, startDate, endDate)
        );

        // 수행 예정일 생성 (최근 → 과거 방향)
        List<LocalDate> scheduledDates = new ArrayList<>();
        LocalDate cursor = endDate;
        while (!cursor.isBefore(startDate)) {
            if (repeatDays.contains(cursor.getDayOfWeek())) {
                scheduledDates.add(cursor);
            }
            cursor = cursor.minusDays(1);
        }

        int bestStreak = 0;
        int tempStreak = 0;
        LocalDate bestStart = null;
        LocalDate bestEnd = null;
        LocalDate tempStart = null;

        for (LocalDate date : scheduledDates) {
            if (completedDates.contains(date)) {
                tempStreak++;
                if (tempStart == null) tempStart = date;
            } else {
                if (tempStreak > bestStreak) {
                    bestStreak = tempStreak;
                    bestStart = tempStart;
                    bestEnd = tempStart.minusDays(tempStreak - 1);
                }
                tempStreak = 0;
                tempStart = null;
            }
        }

        // 루프 종료 후 마지막 streak가 최대인지 확인
        if (tempStreak > bestStreak) {
            bestStreak = tempStreak;
            bestStart = tempStart;
            bestEnd = tempStart.minusDays(tempStreak - 1);
        }

        // 현재 streak 계산
        int currentStreak = 0;
        for (LocalDate date : scheduledDates) {
            if (completedDates.contains(date)) {
                currentStreak++;
            } else {
                break;
            }
        }

        return new RoutineStreakReportResponse(currentStreak, bestStreak, bestEnd, bestStart);
    }


    public List<RoutineWeeklyCompletionRateResponse> getRecentWeeklyCompletionRates(Long userId, Long routineId) {
        Routine routine = validateRoutine(userId, routineId);
        Set<DayOfWeek> repeatDays = routine.getRepeatDays();

        LocalDate today = LocalDate.now();
        List<RoutineWeeklyCompletionRateResponse> results = new ArrayList<>();

        for (int i = 4; i >= 0; i--) {
            LocalDate start = today.with(DayOfWeek.MONDAY).minusWeeks(i);
            LocalDate end = start.plusDays(6);

            // 예정된 날짜 리스트
            List<LocalDate> scheduledDates = start.datesUntil(end.plusDays(1))
                    .filter(d -> repeatDays.contains(d.getDayOfWeek()))
                    .toList();

            int totalScheduled = scheduledDates.size();

            // 성공한 날짜 조회
            List<LocalDate> completedDates = routineExecutionRepository
                    .findAllCompletedDatesByRoutineIdBetween(routineId, start, end);

            int completed = (int) scheduledDates.stream()
                    .filter(completedDates::contains)
                    .count();

            double rate = totalScheduled == 0 ? 0.0 : (double) completed / totalScheduled;

            results.add(new RoutineWeeklyCompletionRateResponse(
                    start, totalScheduled, completed, rate
            ));
        }
        return results;
    }

    public RoutineDateListResponse getExecutedDatesMonthly(Long userId, Long routineId, int year, int month) {
        validateRoutine(userId, routineId);
        YearMonth ym = YearMonth.of(year, month);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        List<LocalDate> dates = routineExecutionRepository
                .findAllCompletedDatesByRoutineIdBetween(routineId, start, end);

        return new RoutineDateListResponse(dates);
    }

    public RoutineDateListResponse getExecutedDatesWeekly(Long userId, Long routineId, LocalDate startDate) {
        validateRoutine(userId, routineId);

        LocalDate monday = startDate.with(DayOfWeek.MONDAY);
        LocalDate sunday = monday.plusDays(6);

        List<LocalDate> completedDates = routineExecutionRepository
                .findAllCompletedDatesByRoutineIdBetween(routineId, monday, sunday);

        return new RoutineDateListResponse(completedDates);
    }



    public RoutineDateListResponse getExecutedDatesYearly(Long userId, Long routineId, int year) {
        validateRoutine(userId, routineId);
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);

        List<LocalDate> dates = routineExecutionRepository
                .findAllCompletedDatesByRoutineIdBetween(routineId, start, end);

        return new RoutineDateListResponse(dates);
    }

    public RoutineDayOfWeekResponse getDayOfWeekCount(Long userId, Long routineId) {
        validateRoutine(userId, routineId);

        // 성공한 날짜 목록
        Set<LocalDate> completedDates = new HashSet<>(
                routineExecutionRepository.findAllCompletedDatesByRoutineIdOrderedDesc(routineId)
        );

        Map<DayOfWeek, Integer> dayOfWeekCountMap = new HashMap<>();

        for (LocalDate completedDate : completedDates) {
            for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
                if (completedDate.getDayOfWeek().equals(dayOfWeek)) {
                    dayOfWeekCountMap.put(dayOfWeek, dayOfWeekCountMap.getOrDefault(dayOfWeek, 0) + 1);
                }
            }
        }

        return new RoutineDayOfWeekResponse(dayOfWeekCountMap);
    }

    private Routine validateRoutine(Long userId, Long routineId) {
        return routineRepository.findByIdAndUserIdAndDeletedFalse(routineId, userId)
                .orElseThrow(() -> new CustomException(ROUTINE_NOT_FOUND));
    }
}
