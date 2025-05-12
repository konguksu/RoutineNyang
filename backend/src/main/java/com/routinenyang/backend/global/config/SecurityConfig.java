package com.routinenyang.backend.global.config;

import com.routinenyang.backend.auth.jwt.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.routinenyang.backend.auth.oauth2.CustomOauth2UserDetailsService;
import com.routinenyang.backend.auth.oauth2.OAuth2SuccessHandler;
import com.routinenyang.backend.auth.jwt.JwtAuthenticationFilter;

import static com.routinenyang.backend.global.constants.GlobalConstants.APPOINTED_URIS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomOauth2UserDetailsService customOauth2UserDetailsService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {}) // CORS 설정은 WebConfig에서 처리
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .oauth2Login(oauth -> oauth
                        .loginPage("/loginPage")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOauth2UserDetailsService))
                        .successHandler(oAuth2SuccessHandler) // JWT 발급 후 리턴
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(APPOINTED_URIS).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}