package com.routinenyang.backend.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class RoutineDateListResponse {
    private List<LocalDate> completedDates;
}
