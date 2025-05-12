package com.routinenyang.backend.user.dto;

import lombok.Builder;
import lombok.Getter;
import com.routinenyang.backend.user.entity.UserSurvey;

@Getter
@Builder
public class SurveyDto {
    private String question;
    private String answer;

    public UserSurvey toEntity() {
        return UserSurvey.builder()
                .question(question)
                .answer(answer)
                .build();
    }

    public static SurveyDto from(UserSurvey survey) {
        return SurveyDto.builder()
                .question(survey.getQuestion())
                .answer(survey.getAnswer())
                .build();
    }
}
