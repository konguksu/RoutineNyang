package com.routinenyang.backend.global.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import com.routinenyang.backend.global.exception.CustomException;

import static com.routinenyang.backend.global.exception.ErrorCode.USER_NOT_FOUND;

public class SecurityUtils {

    private SecurityUtils() {}

    public static String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        throw new CustomException(USER_NOT_FOUND);
    }
}