package com.routinenyang.backend.routine.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DailyNoteRequest {
    private LocalDate date;
    private String content;
}
