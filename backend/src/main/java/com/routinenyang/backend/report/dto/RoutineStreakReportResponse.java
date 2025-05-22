package com.routinenyang.backend.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class RoutineStreakReportResponse {
    private int currentStreak;
    private int maxStreak;
    private LocalDate maxStreakStartDate;
    private LocalDate maxStreakEndDate;
}
