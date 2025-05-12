package com.routinenyang.backend.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import com.routinenyang.backend.user.entity.User;

import java.time.LocalDate;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    private long userId;
    private String email;
    private LocalDate dateOfBirth;
    private String gender;
    private SurveyResponse surveyResponse;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .build();
    }

    public static UserResponse from(User user, SurveyResponse surveyResponse) {
        return UserResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .surveyResponse(surveyResponse)
                .build();
    }
}
