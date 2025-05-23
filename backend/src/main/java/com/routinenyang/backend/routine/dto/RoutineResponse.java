package com.routinenyang.backend.routine.dto;

import com.routinenyang.backend.routine.entity.Routine;
import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

@Builder
@Getter
public class RoutineResponse {
    private String name;
    private Set<DayOfWeek> repeatDays;
    private String preferredTime;
    private LocalDate startDate;
    private LocalDate endDate;
    private String color;
    private Long groupId;

    public static RoutineResponse from(Routine routine) {
        return RoutineResponse.builder()
                .name(routine.getName())
                .repeatDays(routine.getRepeatDays())
                .preferredTime(routine.getPreferredTime())
                .startDate(routine.getStartDate())
                .endDate(routine.getEndDate())
                .color(routine.getColor())
                .groupId(routine.getGroup().getId())
                .build();
    }
}
