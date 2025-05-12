package com.routinenyang.backend.routine.dto;

import com.routinenyang.backend.routine.entity.RoutineGroup;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoutineGroupResponse {
    private Long groupId;
    private String name;

    public static RoutineGroupResponse from(RoutineGroup entity) {
        return RoutineGroupResponse.builder()
                .groupId(entity.getId())
                .name(entity.getName())
                .build();
    }

}
