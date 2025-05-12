package com.routinenyang.backend.routine.dto;

import lombok.Builder;
import lombok.Getter;
import com.routinenyang.backend.routine.entity.Routine;

@Getter
@Builder
public class RoutineResponse {
    private Long routineId;
    private Long groupId;
    private String name;

    public static RoutineResponse from(Routine routine) {
        return RoutineResponse.builder()
                .routineId(routine.getId())
                .groupId(routine.getGroup().getId())
                .name(routine.getName())
                .build();
    }
}
