package com.routinenyang.backend.global.constants;

public class SwaggerConstants {
    /**
     * swagger
     */

    public static final String[] SWAGGER_APPOINTED_PATHS = {
            "/**"
    };
    public static final String DEFINITION_TITLE = "🐈 루틴냥 API 명세서";
    public static final String DEFINITION_DESCRIPTION = "AI based routine Recommendation Service - 루틴냥 Server의 API 명세서입니다.";
    public static final String DEFINITION_VERSION = "v1";

    public static final String SERVERS_URL_LOCAL = "http://localhost:8080";
    public static final String SERVERS_DESCRIPTION_LOCAL = "Local Test Server";
    public static final String SERVERS_URL_DEPLOYED_TEST = "https://routinenyang-service-1002302699919.us-central1.run.app";
    public static final String SERVERS_DESCRIPTION_DEPLOYED_TEST = "Deployed Test Server";

    public static final String SECURITY_SCHEME_NAME = "bearer-key";
    public static final String SECURITY_SCHEME = "bearer";
    public static final String SECURITY_SCHEME_BEARER_FORMAT = "JWT";
    public static final String SECURITY_SCHEME_DESCRIPTION = "JWT 토큰 키를 입력해주세요!";
}
