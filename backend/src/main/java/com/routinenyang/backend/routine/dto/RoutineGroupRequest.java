package com.routinenyang.backend.routine.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoutineGroupRequest {
    private String name;
}