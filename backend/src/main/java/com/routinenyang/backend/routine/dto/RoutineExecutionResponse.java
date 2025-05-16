package com.routinenyang.backend.routine.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class RoutineExecutionResponse {
    private LocalDate date;
    private boolean completed;
}
