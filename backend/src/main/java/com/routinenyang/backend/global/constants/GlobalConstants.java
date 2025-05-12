package com.routinenyang.backend.global.constants;

public class GlobalConstants {

    public static final String[] APPOINTED_URIS = {
            "/",                      // 루트
            "/loginPage",            // 로그인 페이지
            "/csrf",                 // CSRF 엔드포인트
            "/error",                // 에러 페이지

            "/api-docs",              // 사용자 지정 OpenAPI JSON 경로
            "/api-docs/swagger-config",
            "/v3/api-docs",           // springdoc 기본 API 문서 경로
            "/v3/api-docs/**",        // 내부 Swagger config 요청 포함
            "/swagger-ui/**",         // Swagger UI HTML, JS 등 정적 리소스
            "/api-test/**"            // 커스텀 Swagger UI 경로
    };
}