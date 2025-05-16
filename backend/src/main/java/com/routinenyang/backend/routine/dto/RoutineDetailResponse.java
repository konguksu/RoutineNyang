package com.routinenyang.backend.routine.dto;

import com.routinenyang.backend.routine.entity.Routine;
import com.routinenyang.backend.routine.entity.RoutineGroup;
import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

@Builder
@Getter
public class RoutineDetailResponse {
    private String name;
    private Set<DayOfWeek> repeatDays;
    private String preferredTime;
    private LocalDate startDate;
    private LocalDate endDate;
    private String color;
    private Long groupId;

    public static RoutineDetailResponse from(Routine routine) {
        return RoutineDetailResponse.builder()
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
