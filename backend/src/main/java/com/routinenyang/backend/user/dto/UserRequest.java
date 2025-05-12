package com.routinenyang.backend.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class UserRequest {
    private String name;
    private LocalDate dateOfBirth;
    private String gender;
    private List<SurveyDto> surveys;
}
