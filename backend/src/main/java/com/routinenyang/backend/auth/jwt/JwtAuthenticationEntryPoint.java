package com.routinenyang.backend.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.routinenyang.backend.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.routinenyang.backend.global.exception.ErrorCode.UNAUTHORIZED_REQUEST;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // ApiResponse 포맷으로 401 응답
        ApiResponse<Void> body = ApiResponse.fail(UNAUTHORIZED_REQUEST);

        response.setStatus(UNAUTHORIZED_REQUEST.getStatus().value());
        response.setContentType("application/json;charset=UTF-8");

        new ObjectMapper().writeValue(response.getWriter(), body);
    }
}
