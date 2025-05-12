package com.routinenyang.backend.auth.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.routinenyang.backend.auth.jwt.JwtProvider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Map<String, Object> body = new HashMap<>();

        String jwt = jwtProvider.createToken(userDetails.getUsername());
        body.put("token", jwt);
        body.put("onboardingFinished", userDetails.getUser().isOnBoardingFinished());
        body.put("userName", userDetails.getUser().getName());

        request.getSession().setAttribute("loginResult", body); // 세션에 저장
        response.sendRedirect("/api/users/oauth2/login/success"); // 프론트가 fetch로 이 URL 요청
    }
}
