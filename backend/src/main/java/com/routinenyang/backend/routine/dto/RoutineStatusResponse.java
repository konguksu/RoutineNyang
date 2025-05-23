package com.routinenyang.backend.routine.dto;

import com.routinenyang.backend.routine.entity.Routine;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoutineStatusResponse {
    private Long routineId;
    private String name;
    private String color;
    private boolean completed;

    public static RoutineStatusResponse from(Routine routine, boolean completed) {
        return RoutineStatusResponse.builder()
                .routineId(routine.getId())
                .name(routine.getName())
                .color(routine.getColor())
                .completed(completed)
                .build();
    }
}
