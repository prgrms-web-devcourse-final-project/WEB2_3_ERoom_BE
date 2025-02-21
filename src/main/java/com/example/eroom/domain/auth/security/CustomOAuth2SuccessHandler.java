package com.example.eroom.domain.auth.security;

import com.example.eroom.domain.entity.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final CustomOAuth2MemberService customOAuth2MemberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        HttpSession session = request.getSession();
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // GUEST 사용자라면 회원가입 페이지로 리디렉트
        if (oAuth2User.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_GUEST"))) {
            response.sendRedirect("/auth/signup");
            return;
        }

        // 기존 회원이면 세션 저장 후 메인 페이지로
        if (oAuth2User instanceof CustomOAuth2Member) {
            CustomOAuth2Member customUser = (CustomOAuth2Member) oAuth2User;
            session.setAttribute("member", customUser.getMember());
        }

        response.sendRedirect("/project/list");
    }

}
