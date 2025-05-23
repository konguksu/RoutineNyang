package com.routinenyang.backend.report.service;

import com.routinenyang.backend.global.exception.CustomException;
import com.routinenyang.backend.report.dto.*;
import com.routinenyang.backend.routine.entity.Routine;
import com.routinenyang.backend.routine.repository.RoutineExecutionRepository;
import com.routinenyang.backend.routine.repository.RoutineRepeatHistoryRepository;
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
    private final RoutineRepeatHistoryRepository routineRepeatHistoryRepository;

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

    // streak 계산 (최신→과거 순으로 검사)
    public RoutineStreakReportResponse getStreakReport(Long userId, Long routineId) {
        Routine routine = validateRoutine(userId, routineId);

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = routine.getStartDate();

        // 성공한 날짜 집합
        Set<LocalDate> completedDates = new HashSet<>(
                routineExecutionRepository.findAllCompletedDatesByRoutineIdBetweenOrderedDesc(routineId, startDate, endDate)
        );

        Map<LocalDate, Set<DayOfWeek>> repeatDayCache = new HashMap<>();
        List<LocalDate> scheduledDates = new ArrayList<>();

        for (LocalDate date = endDate; !date.isBefore(startDate); date = date.minusDays(1)) {
            Set<DayOfWeek> repeatDays = repeatDayCache.computeIfAbsent(date, d -> getValidRepeatDays(routineId, d));
            if (repeatDays.contains(date.getDayOfWeek())) {
                scheduledDates.add(date);
            }
        }

        // 최대 연속 streak 계산
        int bestStreak = 0;
        int tempStreak = 0;
        List<LocalDate> tempStreakDates = new ArrayList<>();
        List<LocalDate> bestStreakDates = new ArrayList<>();

        for (LocalDate date : scheduledDates) {
            if (completedDates.contains(date)) {
                tempStreak++;
                tempStreakDates.add(date);
            } else {
                if (tempStreak > bestStreak) {
                    bestStreak = tempStreak;
                    bestStreakDates = new ArrayList<>(tempStreakDates);
                }
                tempStreak = 0;
                tempStreakDates.clear();
            }
        }

        // 마지막 streak 처리
        if (tempStreak > bestStreak) {
            bestStreak = tempStreak;
            bestStreakDates = new ArrayList<>(tempStreakDates);
        }

        // 현재 streak 계산: 최신부터 연속 성공
        int currentStreak = 0;
        for (LocalDate date : scheduledDates) {
            if (completedDates.contains(date)) {
                currentStreak++;
            } else break;
        }

        LocalDate bestEnd = bestStreakDates.isEmpty() ? null : bestStreakDates.get(bestStreakDates.size() - 1);
        LocalDate bestStart = bestStreakDates.isEmpty() ? null : bestStreakDates.get(0);

        return new RoutineStreakReportResponse(currentStreak, bestStreak, bestEnd, bestStart);
    }

    // 최근 5주 주별 성공률
    public List<RoutineWeeklyCompletionRateResponse> getRecentWeeklyCompletionRates(Long userId, Long routineId) {
        validateRoutine(userId, routineId);

        LocalDate today = LocalDate.now();
        List<RoutineWeeklyCompletionRateResponse> results = new ArrayList<>();
        Map<LocalDate, Set<DayOfWeek>> repeatDayCache = new HashMap<>();

        for (int i = 4; i >= 0; i--) {
            LocalDate start = today.with(DayOfWeek.MONDAY).minusWeeks(i);
            LocalDate end = start.plusDays(6);

            // 이번 주차의 예정 수행일 리스트
            List<LocalDate> scheduledDates = start.datesUntil(end.plusDays(1))
                    .filter(d -> repeatDayCache.computeIfAbsent(d, date ->
                            getValidRepeatDays(routineId, date)
                    ).contains(d.getDayOfWeek()))
                    .toList();

            int totalScheduled = scheduledDates.size();

            // 해당 주차 내 성공한 날짜 조회
            List<LocalDate> completedDates = routineExecutionRepository
                    .findAllCompletedDatesByRoutineIdBetweenOrderedDesc(routineId, start, end);

            int completed = (int) scheduledDates.stream()
                    .filter(completedDates::contains)
                    .count();

            double rate = totalScheduled == 0 ? 0.0 :
                    Math.round(((double) completed / totalScheduled) * 1000) / 10.0;  // 소수점 첫째자리

            results.add(new RoutineWeeklyCompletionRateResponse(
                    start, totalScheduled, completed, rate
            ));
        }
        return results;
    }

    // 월별 성공일자 리스트 (반복 요일은 고려하지 않고 단순 완료일자만 반환함)
    public RoutineDateListResponse getExecutedDatesWeekly(Long userId, Long routineId, LocalDate startDate) {
        validateRoutine(userId, routineId);

        LocalDate monday = startDate.with(DayOfWeek.MONDAY);
        LocalDate sunday = monday.plusDays(6);

        List<LocalDate> completedDates = routineExecutionRepository
                .findAllCompletedDatesByRoutineIdBetweenOrderedDesc(routineId, monday, sunday);

        return new RoutineDateListResponse(completedDates);
    }

    // 주간 성공일자 리스트 (반복 요일은 고려하지 않고 단순 완료일자만 반환함)
    public RoutineDateListResponse getExecutedDatesMonthly(Long userId, Long routineId, int year, int month) {
        validateRoutine(userId, routineId);
        YearMonth ym = YearMonth.of(year, month);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        List<LocalDate> dates = routineExecutionRepository
                .findAllCompletedDatesByRoutineIdBetweenOrderedDesc(routineId, start, end);

        return new RoutineDateListResponse(dates);
    }

    // 연간 성공일자 리스트 (반복 요일은 고려하지 않고 단순 완료일자만 반환함)
    public RoutineDateListResponse getExecutedDatesYearly(Long userId, Long routineId, int year) {
        validateRoutine(userId, routineId);
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);

        List<LocalDate> dates = routineExecutionRepository
                .findAllCompletedDatesByRoutineIdBetweenOrderedDesc(routineId, start, end);

        return new RoutineDateListResponse(dates);
    }

    // 요일별 성공 카운트
    public RoutineDayOfWeekCountResponse getDayOfWeekCount(Long userId, Long routineId) {
        validateRoutine(userId, routineId);

        // 성공한 날짜 목록
        Set<LocalDate> completedDates = new HashSet<>(
                routineExecutionRepository.findAllCompletedDatesByRoutineIdOrderedDesc(routineId)
        );

        Map<DayOfWeek, Integer> dayOfWeekCountMap = new HashMap<>();

        for (LocalDate completedDate : completedDates) {
            DayOfWeek dow = completedDate.getDayOfWeek();
            dayOfWeekCountMap.put(dow, dayOfWeekCountMap.getOrDefault(dow, 0) + 1);
        }

        return new RoutineDayOfWeekCountResponse(dayOfWeekCountMap);
    }

    private Routine validateRoutine(Long userId, Long routineId) {
        return routineRepository.findByIdAndUserIdAndDeletedFalse(routineId, userId)
                .orElseThrow(() -> new CustomException(ROUTINE_NOT_FOUND));
    }

    private Set<DayOfWeek> getValidRepeatDays(Long routineId, LocalDate date) {
        return new HashSet<>(routineRepeatHistoryRepository.findRepeatDaysByRoutineIdAndDate(routineId, date));
    }
}
