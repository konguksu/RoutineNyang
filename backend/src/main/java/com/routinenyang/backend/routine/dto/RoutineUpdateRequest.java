package com.routinenyang.backend.routine.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

@Builder
@Getter
public class RoutineUpdateRequest {
    private String name;
    private Set<DayOfWeek> repeatDays;
    private String preferredTime;
    private LocalDate endDate;
    private String color;
    private Long groupId;
}
