package com.routinenyang.backend.routine.dto;

import com.routinenyang.backend.routine.entity.Routine;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoutineSummaryResponse {
    private Long routineId;
    private String name;

    public static RoutineSummaryResponse from(Routine routine) {
        return RoutineSummaryResponse.builder()
                .routineId(routine.getId())
                .name(routine.getName())
                .build();
    }
}
