package com.routinenyang.backend.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class RoutineWeeklyCompletionRateResponse {
    private LocalDate weekStartDate;
    private int totalScheduled;
    private int completed;
    private double rate;
}
