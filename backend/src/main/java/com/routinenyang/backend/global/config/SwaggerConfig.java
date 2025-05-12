package com.routinenyang.backend.global.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.routinenyang.backend.global.constants.SwaggerConstants.*;

@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        scheme = SECURITY_SCHEME,
        name = SECURITY_SCHEME_NAME,
        bearerFormat = SECURITY_SCHEME_BEARER_FORMAT,
        description = SECURITY_SCHEME_DESCRIPTION
)
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .info(apiInfo())
                .servers(List.of(localServer()));
    }

    private Info apiInfo() {
        return new Info()
                .title(DEFINITION_TITLE) // API의 제목
                .description(DEFINITION_DESCRIPTION) // API에 대한 설명
                .version(DEFINITION_VERSION); // API의 버전
    }

    private Server localServer() {
        return new Server()
                .url(SERVERS_URL_LOCAL)
                .description(SERVERS_DESCRIPTION_LOCAL);
    }
}
