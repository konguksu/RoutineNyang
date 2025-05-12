package com.routinenyang.backend.routine.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

@Builder
@Getter
public class RoutineRequest {
    private String name;
    private Set<DayOfWeek> repeatDays;
    private Integer timesPerDay;
    private String preferredTime;
    private LocalDate startDate;
    private LocalDate endDate;
    private String color;
    private Long groupId;
}
