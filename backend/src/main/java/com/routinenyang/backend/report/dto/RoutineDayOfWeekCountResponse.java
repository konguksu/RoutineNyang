package com.routinenyang.backend.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.DayOfWeek;
import java.util.Map;

@AllArgsConstructor
@Getter
public class RoutineDayOfWeekCountResponse {
    Map<DayOfWeek, Integer> dayOfWeekCountMap;
}
