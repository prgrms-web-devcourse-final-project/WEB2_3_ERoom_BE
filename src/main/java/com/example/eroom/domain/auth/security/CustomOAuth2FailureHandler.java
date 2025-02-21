package com.example.eroom.domain.auth.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomOAuth2FailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        if (exception.getCause() instanceof OAuth2RedirectException) {
            String redirectUrl = ((OAuth2RedirectException) exception.getCause()).getRedirectUrl();
            response.sendRedirect(redirectUrl);
        } else {
            response.sendRedirect("/auth/login?error"); // 일반적인 로그인 실패
        }
    }
}

