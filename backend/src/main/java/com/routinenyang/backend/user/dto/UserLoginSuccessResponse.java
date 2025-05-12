package com.routinenyang.backend.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "구글 로그인 성공 후 응답")
@Builder
@Getter
public class UserLoginSuccessResponse {
    private Boolean onboardingFinished;
    private String userName;
    private String token;
}
