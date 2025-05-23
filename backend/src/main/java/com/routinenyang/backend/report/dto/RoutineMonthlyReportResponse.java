package com.routinenyang.backend.report.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoutineMonthlyReportResponse {
    private int year;
    private int month;
    private int successCount; // 성공한 횟수
}
