package com.routinenyang.backend.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SurveyResponse {
    private Integer totalCount;
    private List<SurveyDto> savedSurveys;
}
