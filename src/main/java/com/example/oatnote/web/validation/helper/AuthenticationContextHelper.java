package com.example.oatnote.web.validation.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.oatnote.web.controller.exception.auth.OatAuthorizationException;

public class AuthenticationContextHelper {

    public static String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof String userId) {
            return userId;
        }
        throw OatAuthorizationException.withDetail("User ID not found in security context");
    }
}
