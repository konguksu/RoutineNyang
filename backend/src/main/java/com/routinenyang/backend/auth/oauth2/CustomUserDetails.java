package com.routinenyang.backend.auth.oauth2;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import com.routinenyang.backend.user.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails, OAuth2User {

    private final User user;

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    // 비밀번호 로그인 없음
    @Override
    public String getPassword() {
        return null;
    }

    // username 대신 email 사용
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public String getName() {
        return user.getName();
    }
}
