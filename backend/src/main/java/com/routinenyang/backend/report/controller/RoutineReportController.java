package com.routinenyang.backend.report.controller;

import com.routinenyang.backend.auth.resolver.CurrentUser;
import com.routinenyang.backend.global.response.ApiResponse;
import com.routinenyang.backend.global.response.ResponseFactory;
import com.routinenyang.backend.report.dto.*;
import com.routinenyang.backend.report.service.RoutineReportService;
import com.routinenyang.backend.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/routines/{routine-id}/reports")
@Tag(name = "Routine Report", description = "루틴 통계 API")
public class RoutineReportController {

    private final RoutineReportService routineReportService;

    @GetMapping("/monthly")
    @Operation(summary = "Routine 월별 성공 횟수 조회", description = "6개월의 월별 루틴 성공 횟수 조회. 기본은 최근 6개월, 시작 월 지정 가능 ")
    public ResponseEntity<ApiResponse<List<RoutineMonthlyReportResponse>>> getMonthlyReport(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable(name = "routine-id") Long routineId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth startYm
    ) {
        return ResponseFactory.ok(routineReportService.getMonthlyReport(user.getId(), routineId, startYm));
    }

    @GetMapping("/streak")
    @Operation(summary = "Routine 연속 성공 횟수 조회", description = "현재 연속 횟수와 최대 연속을 달성한 기간 및 횟수 조회")
    public ResponseEntity<ApiResponse<RoutineStreakReportResponse>> getStreakReport(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable(name = "routine-id") Long routineId
    ) {
        return ResponseFactory.ok(routineReportService.getStreakReport(user.getId(), routineId));
    }

    @GetMapping("/recent-completion-rates")
    @Operation(summary = "Routine 최근 주별 성공률 조회", description = "최근 5주간의 루틴 성공률(%) 조회. 성공률은 반올림하여 소수점 첫째자리까지만 반환")
    public ResponseEntity<ApiResponse<List<RoutineWeeklyCompletionRateResponse>>> getRecentWeeklyCompletionRates(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable(name = "routine-id") Long routineId
    ) {
        return ResponseFactory.ok(routineReportService.getRecentWeeklyCompletionRates(user.getId(), routineId));
    }

    @GetMapping("/day-of-week-count")
    @Operation(summary = "요일별 성공 횟수 조회", description = "루틴의 요일별 성공 횟수 조회")
    public ResponseEntity<ApiResponse<RoutineDayOfWeekCountResponse>> getDayOfWeekCount(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable(name = "routine-id") Long routineId
    ) {
        return ResponseFactory.ok(routineReportService.getDayOfWeekCount(user.getId(), routineId));
    }


    @GetMapping("/executed-dates/weekly")
    @Operation(summary = "주간 수행 성공일 조회", description = "요청한 startDate를 기준으로 1주일(월~일)의 루틴 수행 성공일 목록 조회")
    public ResponseEntity<ApiResponse<RoutineDateListResponse>> getExecutedDatesByWeek(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable(name = "routine-id") Long routineId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate
    ) {
        return ResponseFactory.ok(
                routineReportService.getExecutedDatesWeekly(user.getId(), routineId, startDate)
        );
    }


    @GetMapping("/executed-dates/monthly")
    @Operation(summary = "월별 수행 성공일 조회", description = "특정 연도와 월 기준으로 루틴 수행 성공일 목룍 조회")
    public ResponseEntity<ApiResponse<RoutineDateListResponse>> getExecutedDatesByMonth(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable(name = "routine-id") Long routineId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return ResponseFactory.ok(
                routineReportService.getExecutedDatesMonthly(user.getId(), routineId, year, month)
        );
    }

    @GetMapping("/executed-dates/yearly")
    @Operation(summary = "연도별 수행 성공일 조회", description = "특정 연도 전체의 루틴 수행 성공일 목록 조회")
    public ResponseEntity<ApiResponse<RoutineDateListResponse>> getExecutedDatesByYear(
            @Parameter(hidden = true) @CurrentUser User user,
            @PathVariable(name = "routine-id") Long routineId,
            @RequestParam int year
    ) {
        return ResponseFactory.ok(
                routineReportService.getExecutedDatesYearly(user.getId(), routineId, year)
        );
    }
}
